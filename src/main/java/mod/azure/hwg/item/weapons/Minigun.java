package mod.azure.hwg.item.weapons;

import io.netty.buffer.Unpooled;
import mod.azure.hwg.HWGMod;
import mod.azure.hwg.client.ClientInit;
import mod.azure.hwg.entity.HWGEntity;
import mod.azure.hwg.entity.projectiles.BulletEntity;
import mod.azure.hwg.util.registry.HWGItems;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Minigun extends HWGGunBase {

	public Minigun() {
		super(new Item.Settings().group(HWGMod.WeaponItemGroup).maxCount(1).maxDamage(101));
	}

	@Override
	public void usageTick(World worldIn, LivingEntity entityLiving, ItemStack stack, int count) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity) entityLiving;
			if (stack.getDamage() < (stack.getMaxDamage() - 1)
					&& !playerentity.getItemCooldownManager().isCoolingDown(this)) {
				playerentity.getItemCooldownManager().set(this, 0);
				if (!worldIn.isClient) {
					BulletEntity abstractarrowentity = createArrow(worldIn, stack, playerentity);
					abstractarrowentity.setProperties(playerentity, playerentity.getPitch(), playerentity.getYaw(),
							0.0F, 1.0F * 3.0F, 1.0F);
					abstractarrowentity.refreshPositionAndAngles(entityLiving.getX(), entityLiving.getBodyY(0.85),
							entityLiving.getZ(), 0, 0);

					abstractarrowentity.setDamage(1.25);
					abstractarrowentity.age = 35;
					abstractarrowentity.hasNoGravity();

					stack.damage(1, entityLiving, p -> p.sendToolBreakStatus(entityLiving.getActiveHand()));
					worldIn.spawnEntity(abstractarrowentity);
				}
				worldIn.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(),
						SoundEvents.ENTITY_SHULKER_SHOOT, SoundCategory.PLAYERS, 1.0F,
						1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + 1F * 0.5F);
			}
		}
	}

	public static float getArrowVelocity(int charge) {
		float f = (float) charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		user.setCurrentHand(hand);
		return TypedActionResult.consume(itemStack);
	}

	public void reload(PlayerEntity user, Hand hand) {
		if (user.getStackInHand(hand).getItem() instanceof Minigun) {
			while (user.getStackInHand(hand).getDamage() != 0 && user.getInventory().count(HWGItems.BULLETS) > 0) {
				removeAmmo(HWGItems.BULLETS, user);
				user.getStackInHand(hand).damage(-50, user, s -> user.sendToolBreakStatus(hand));
				user.getStackInHand(hand).setCooldown(3);
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient) {
			if (((PlayerEntity) entity).getMainHandStack().getItem() instanceof Minigun && ClientInit.reload.isPressed()
					&& selected) {
				PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
				passedData.writeBoolean(true);
				ClientPlayNetworking.send(HWGMod.MINIGUN, passedData);
			}
		}
		if (!(entity instanceof HWGEntity) && selected) {
			((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 1));
		}
	}

	public BulletEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
		BulletEntity arrowentity = new BulletEntity(worldIn, shooter);
		return arrowentity;
	}

	public static float getPullProgress(int useTicks) {
		float f = (float) useTicks / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}
}
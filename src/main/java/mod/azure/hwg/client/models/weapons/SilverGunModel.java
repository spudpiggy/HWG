package mod.azure.hwg.client.models.weapons;

import mod.azure.hwg.HWGMod;
import mod.azure.hwg.item.weapons.SilverGunItem;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.GeoModel;

public class SilverGunModel extends GeoModel<SilverGunItem> {
	@Override
	public ResourceLocation getModelResource(SilverGunItem object) {
		return new ResourceLocation(HWGMod.MODID, "geo/pistol.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(SilverGunItem object) {
		return new ResourceLocation(HWGMod.MODID, "textures/item/silvergun.png");
	}

	@Override
	public ResourceLocation getAnimationResource(SilverGunItem animatable) {
		return new ResourceLocation(HWGMod.MODID, "animations/pistol.animation.json");
	}
}
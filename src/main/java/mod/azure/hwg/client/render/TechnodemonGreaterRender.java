package mod.azure.hwg.client.render;

import mod.azure.hwg.client.models.TechnodemonGreaterModel;
import mod.azure.hwg.entity.TechnodemonGreaterEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class TechnodemonGreaterRender extends GeoEntityRenderer<TechnodemonGreaterEntity> {

	private ItemStack itemStack;
	private VertexConsumerProvider rtb;
	private Identifier whTexture;

	public TechnodemonGreaterRender(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn, new TechnodemonGreaterModel());
		this.shadowRadius = 0.7F;
	}

	@Override
	public RenderLayer getRenderType(TechnodemonGreaterEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(TechnodemonGreaterEntity animatable, MatrixStack stackIn, float ticks,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		this.itemStack = animatable.getEquippedStack(EquipmentSlot.MAINHAND);
		this.rtb = renderTypeBuffer;
		this.whTexture = this.getTextureLocation(animatable);
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
				red, green, blue, partialTicks);
	}

	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn,
			int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (bone.getName().equals("rArmRuff")) {
			stack.push();
			stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-75));
			stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(0));
			stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(0));
			stack.translate(0.4D, 0.3D, 0.6D);
			stack.scale(1.0f, 1.0f, 1.0f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, Mode.THIRD_PERSON_RIGHT_HAND,
					packedLightIn, packedOverlayIn, stack, this.rtb);
			stack.pop();
			bufferIn = rtb.getBuffer(RenderLayer.getEntitySmoothCutout(whTexture));
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

}
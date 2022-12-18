package mod.azure.hwg.client.render.projectiles;

import mod.azure.hwg.entity.projectiles.BaseFlareEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

public class BaseFlareRender extends EntityRenderer<BaseFlareEntity> {

	public BaseFlareRender(EntityRendererFactory.Context dispatcher) {
		super(dispatcher);
	}

	public void render(BaseFlareEntity fireworkRocketEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.multiply(this.dispatcher.getRotation());
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
		matrixStack.scale(fireworkRocketEntity.age > 2 ? 0.5F : 0.0F, fireworkRocketEntity.age > 2 ? 0.5F : 0.0F,
				fireworkRocketEntity.age > 2 ? 0.5F : 0.0F);

		matrixStack.pop();
		super.render(fireworkRocketEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	public Identifier getTexture(BaseFlareEntity fireworkRocketEntity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}

	@Override
	protected int getBlockLight(BaseFlareEntity entity, BlockPos blockPos) {
		return 15;
	}
}
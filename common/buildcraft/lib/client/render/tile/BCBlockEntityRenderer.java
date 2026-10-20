package buildcraft.lib.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/** Bridges legacy {@code render(..., MultiBufferSource)} BERs to the 1.21.11 render-state API. */
@OnlyIn(Dist.CLIENT)
public abstract class BCBlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState>
        implements BlockEntityRenderer<T, S> {

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, Vec3 cameraPos,
            net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderState.extractBase(blockEntity, renderState, overlay);
        extractAdditionalState(blockEntity, renderState, partialTick);
    }

    protected void extractAdditionalState(T blockEntity, S renderState, float partialTick) {
    }

    @Override
    public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState cameraRenderState) {
        T blockEntity = getBlockEntity(renderState);
        if (blockEntity == null) {
            return;
        }
        int light = renderState.lightCoords;
        int overlay = net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;
        collector.submitCustomGeometry(poseStack, RenderType.translucent(), (pose, vertexConsumer) -> {
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(
                    new com.mojang.blaze3d.vertex.ByteBufferBuilder(256));
            renderLegacy(blockEntity, 0, poseStack, bufferSource, light, overlay);
            bufferSource.endBatch();
        });
    }

    protected abstract T getBlockEntity(S renderState);

    protected abstract void renderLegacy(T blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int combinedLight, int combinedOverlay);
}

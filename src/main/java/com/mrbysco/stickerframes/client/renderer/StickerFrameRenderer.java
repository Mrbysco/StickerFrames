package com.mrbysco.stickerframes.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.stickerframes.client.state.StickerFrameRenderState;
import com.mrbysco.stickerframes.entity.StickerFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;

public class StickerFrameRenderer<T extends StickerFrame> extends EntityRenderer<T, StickerFrameRenderState> {
	private final ItemModelResolver itemModelResolver;
	private final MapRenderer mapRenderer;
	private final BlockRenderDispatcher blockRenderer;

	public StickerFrameRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemModelResolver = context.getItemModelResolver();
		this.mapRenderer = context.getMapRenderer();
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	protected int getBlockLightLevel(T stickerFrame, BlockPos pos) {
		return stickerFrame.isGlowing() ? Math.max(5, super.getBlockLightLevel(stickerFrame, pos)) : super.getBlockLightLevel(stickerFrame, pos);
	}

	@Override
	public void render(StickerFrameRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(renderState, poseStack, bufferSource, packedLight);
		poseStack.pushPose();
		Direction direction = renderState.direction;
		Vec3 vec3 = this.getRenderOffset(renderState);
		poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
		poseStack.translate((double) direction.getStepX() * 0.46875D, (double) direction.getStepY() * 0.46875D, (double) direction.getStepZ() * 0.46875D);
		float f;
		float f1;
		if (direction.getAxis().isHorizontal()) {
			f = 0.0F;
			f1 = 180.0F - direction.toYRot();
		} else {
			f = (float) (-90 * direction.getAxisDirection().getStep());
			f1 = 180.0F;
		}

		poseStack.mulPose(Axis.XP.rotationDegrees(f));
		poseStack.mulPose(Axis.YP.rotationDegrees(f1));
		if (!renderState.isInvisible) {
			ModelManager modelmanager = this.blockRenderer.getBlockModelShaper().getModelManager();
			ModelResourceLocation modelresourcelocation = getFrameModelResourceLocation(renderState);
			poseStack.pushPose();
			poseStack.translate(-0.5F, -0.5F, -0.5F);
			this.blockRenderer
					.getModelRenderer()
					.renderModel(
							poseStack.last(),
							bufferSource.getBuffer(RenderType.entitySolidZOffsetForward(TextureAtlas.LOCATION_BLOCKS)),
							null,
							modelmanager.getModel(modelresourcelocation),
							1.0F,
							1.0F,
							1.0F,
							packedLight,
							OverlayTexture.NO_OVERLAY
					);
			poseStack.popPose();
		}

		if (renderState.isInvisible) {
			poseStack.translate(0.0F, 0.0F, 0.5F);
		} else {
			poseStack.translate(0.0F, 0.0F, 0.4375F);
		}

		if (renderState.mapId != null) {
			int j = renderState.rotation % 4 * 2;
			poseStack.mulPose(Axis.ZP.rotationDegrees((float) j * 360.0F / 8.0F));
			poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
			poseStack.translate(-64.0F, -64.0F, 0.0F);
			poseStack.translate(0.0F, 0.0F, -1.0F);
			int i = this.getLightCoords(renderState.isGlowFrame, 15728850, packedLight);
			this.mapRenderer.render(renderState.mapRenderState, poseStack, bufferSource, true, i);
		} else if (!renderState.item.isEmpty()) {
			poseStack.mulPose(Axis.ZP.rotationDegrees((float) renderState.rotation * 360.0F / 8.0F));
			int k = this.getLightCoords(renderState.isGlowFrame, 15728880, packedLight);
			poseStack.scale(0.5F, 0.5F, 0.001F);
			if (renderState.item.isGui3d()) {
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			}
			renderState.item.render(poseStack, bufferSource, k, OverlayTexture.NO_OVERLAY);
		}

		poseStack.popPose();
	}

	private int getLightCoords(boolean isGlowFrame, int glowLight, int normalLight) {
		return isGlowFrame ? glowLight : normalLight;
	}

	private static ModelResourceLocation getFrameModelResourceLocation(StickerFrameRenderState renderState) {
		if (renderState.mapId != null) {
			return renderState.isGlowFrame ? BlockStateModelLoader.GLOW_MAP_FRAME_LOCATION : BlockStateModelLoader.MAP_FRAME_LOCATION;
		} else {
			return renderState.isGlowFrame ? BlockStateModelLoader.GLOW_FRAME_LOCATION : BlockStateModelLoader.FRAME_LOCATION;
		}
	}

	@Override
	public Vec3 getRenderOffset(StickerFrameRenderState renderState) {
		return new Vec3((double) ((float) renderState.direction.getStepX() * 0.3F), -0.25, (double) ((float) renderState.direction.getStepZ() * 0.3F));
	}

	@Override
	protected boolean shouldShowName(T entity, double distanceToCameraSq) {
		return Minecraft.renderNames() && this.entityRenderDispatcher.crosshairPickEntity == entity && entity.getItem().getCustomName() != null;
	}

	@Override
	public StickerFrameRenderState createRenderState() {
		return new StickerFrameRenderState();
	}

	@Override
	public void extractRenderState(T frame, StickerFrameRenderState renderState, float partialTick) {
		super.extractRenderState(frame, renderState, partialTick);
		renderState.direction = frame.getDirection();
		ItemStack itemstack = frame.getItem();
		this.itemModelResolver.updateForNonLiving(renderState.item, itemstack, frame.usesGuiDisplay() ? ItemDisplayContext.GUI : ItemDisplayContext.FIXED, frame);
		renderState.rotation = frame.getRotation();
		renderState.isGlowFrame = frame.isGlowing();
		renderState.mapId = null;
		if (!itemstack.isEmpty()) {
			MapId mapid = frame.getFramedMapId(itemstack);
			if (mapid != null) {
				MapItemSavedData mapitemsaveddata = net.minecraft.world.item.MapItem.getSavedData(itemstack, frame.level());
				if (mapitemsaveddata != null) {
					this.mapRenderer.extractRenderState(mapid, mapitemsaveddata, renderState.mapRenderState);
					renderState.mapId = mapid;
				}
			}
		}
	}
}

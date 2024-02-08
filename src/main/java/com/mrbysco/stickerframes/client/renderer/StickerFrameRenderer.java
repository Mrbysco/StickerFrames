package com.mrbysco.stickerframes.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrbysco.stickerframes.entity.StickerFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;

public class StickerFrameRenderer<T extends StickerFrame> extends EntityRenderer<T> {
	private static final ModelResourceLocation FRAME_LOCATION = new ModelResourceLocation("item_frame", "map=false");
	private static final ModelResourceLocation MAP_FRAME_LOCATION = new ModelResourceLocation("item_frame", "map=true");
	private static final ModelResourceLocation GLOW_FRAME_LOCATION = new ModelResourceLocation("glow_item_frame", "map=false");
	private static final ModelResourceLocation GLOW_MAP_FRAME_LOCATION = new ModelResourceLocation("glow_item_frame", "map=true");
	private final ItemRenderer itemRenderer;
	private final BlockRenderDispatcher blockRenderer;

	public StickerFrameRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	protected int getBlockLightLevel(T stickerFrame, BlockPos pos) {
		return stickerFrame.isGlowing() ? Math.max(5, super.getBlockLightLevel(stickerFrame, pos)) : super.getBlockLightLevel(stickerFrame, pos);
	}

	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packetLight) {
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, packetLight);
		poseStack.pushPose();
		Direction direction = entity.getDirection();
		Vec3 vec3 = this.getRenderOffset(entity, partialTicks);
		poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
		poseStack.translate((double) direction.getStepX() * 0.46875D, (double) direction.getStepY() * 0.46875D, (double) direction.getStepZ() * 0.46875D);
		poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entity.getYRot()));
		boolean flag = entity.isInvisible() || !entity.getItem().isEmpty();
		ItemStack itemstack = entity.getItem();
		if (!flag) {
			ModelManager modelmanager = this.blockRenderer.getBlockModelShaper().getModelManager();
			ModelResourceLocation modelresourcelocation = this.getFrameModelResourceLoc(entity, itemstack);
			poseStack.pushPose();
			poseStack.translate(-0.5F, -0.5F, -0.5F);
			this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), (BlockState) null, modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, packetLight, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}

		if (!itemstack.isEmpty()) {
			MapItemSavedData mapitemsaveddata = MapItem.getSavedData(itemstack, entity.getLevel());
			poseStack.translate(0.0F, 0.0F, 0.499F);

			int j = mapitemsaveddata != null ? entity.getRotation() % 4 * 2 : entity.getRotation();
			poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) j * 360.0F / 8.0F));

			if (mapitemsaveddata != null) {
				poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
				poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
				poseStack.translate(-64.0F, -64.0F, 0.0F);
				poseStack.translate(0.0F, 0.0F, -1.0F);
				if (mapitemsaveddata != null) {
					int i = this.getLightVal(entity, 15728850, packetLight);
					Minecraft.getInstance().gameRenderer.getMapRenderer().render(poseStack, bufferSource, entity.getFramedMapId().getAsInt(), mapitemsaveddata, true, i);
				}
			} else {
				int k = this.getLightVal(entity, 15728880, packetLight);
				ItemTransforms.TransformType displayContext = this.getDisplayContext(entity);
				poseStack.scale(0.5F, 0.5F, 0.001F);
				if (displayContext == ItemTransforms.TransformType.GUI) {
					poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
				}
				this.itemRenderer.renderStatic(itemstack, displayContext, k, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.getId());
			}
		}

		poseStack.popPose();
	}

	private ItemTransforms.TransformType getDisplayContext(T entity) {
		return entity.usesGuiDisplay() ? ItemTransforms.TransformType.GUI : ItemTransforms.TransformType.FIXED;
	}

	private int getLightVal(T stickerFrame, int glowLightVal, int regularLightVal) {
		return stickerFrame.isGlowing() ? glowLightVal : regularLightVal;
	}

	private ModelResourceLocation getFrameModelResourceLoc(T stickerFrame, ItemStack stack) {
		boolean flag = stickerFrame.isGlowing();
		if (stack.getItem() instanceof MapItem) {
			return flag ? GLOW_MAP_FRAME_LOCATION : MAP_FRAME_LOCATION;
		} else {
			return flag ? GLOW_FRAME_LOCATION : FRAME_LOCATION;
		}
	}

	public Vec3 getRenderOffset(T entity, float partialTicks) {
		return new Vec3((double) ((float) entity.getDirection().getStepX() * 0.3F), -0.25D, (double) ((float) entity.getDirection().getStepZ() * 0.3F));
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(T entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}

	protected boolean shouldShowName(T entity) {
		if (Minecraft.renderNames() && !entity.getItem().isEmpty() && entity.getItem().hasCustomHoverName() && this.entityRenderDispatcher.crosshairPickEntity == entity) {
			double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
			float f = entity.isDiscrete() ? 32.0F : 64.0F;
			return d0 < (double) (f * f);
		} else {
			return false;
		}
	}

	protected void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.renderNameTag(entity, entity.getItem().getHoverName(), poseStack, bufferSource, packedLight);
	}
}

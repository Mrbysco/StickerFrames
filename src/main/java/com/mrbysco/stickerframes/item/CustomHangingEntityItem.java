package com.mrbysco.stickerframes.item;

import com.mrbysco.stickerframes.entity.GlowGuiStickerFrame;
import com.mrbysco.stickerframes.entity.GlowStickerFrame;
import com.mrbysco.stickerframes.entity.GuiStickerFrame;
import com.mrbysco.stickerframes.entity.StickerFrame;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class CustomHangingEntityItem extends HangingEntityItem {

	private final EntityType<? extends StickerFrame> type;

	public CustomHangingEntityItem(EntityType<? extends StickerFrame> type, Properties properties) {
		super(type, properties);
		this.type = type;
	}

	/**
	 * Called when this item is used when targeting a Block
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos blockpos = context.getClickedPos();
		Direction clickedFace = context.getClickedFace();
		BlockPos relativePos = blockpos.relative(clickedFace);
		Player player = context.getPlayer();
		ItemStack itemstack = context.getItemInHand();
		if (player != null && !this.mayPlace(player, clickedFace, itemstack, relativePos)) {
			return InteractionResult.FAIL;
		} else {
			Level level = context.getLevel();
			HangingEntity hangingentity;
			if (this.type == FrameRegistry.STICKER_FRAME.get()) {
				hangingentity = new StickerFrame(level, relativePos, clickedFace);
			} else if (this.type == FrameRegistry.GUI_STICKER_FRAME.get()) {
				hangingentity = new GuiStickerFrame(level, relativePos, clickedFace);
			} else {
				if (this.type == FrameRegistry.GLOW_STICKER_FRAME.get()) {
					hangingentity = new GlowStickerFrame(level, relativePos, clickedFace);
				} else if (this.type == FrameRegistry.GLOW_GUI_STICKER_FRAME.get()) {
					hangingentity = new GlowGuiStickerFrame(level, relativePos, clickedFace);
				} else {
					return InteractionResult.SUCCESS;
				}
			}

			TypedEntityData<EntityType<?>> typedentitydata = itemstack.get(DataComponents.ENTITY_DATA);
			if (typedentitydata != null && !typedentitydata.getUnsafe().isEmpty()) {
				EntityType.updateCustomEntityTag(level, player, hangingentity, typedentitydata);
			}

			if (hangingentity.survives()) {
				if (!level.isClientSide()) {
					hangingentity.playPlacementSound();
					level.gameEvent(player, GameEvent.ENTITY_PLACE, hangingentity.position());
					level.addFreshEntity(hangingentity);
				}

				itemstack.shrink(1);
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}
	}
}
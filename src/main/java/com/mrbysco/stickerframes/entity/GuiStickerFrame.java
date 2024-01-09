package com.mrbysco.stickerframes.entity;

import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GuiStickerFrame extends StickerFrame {
	public GuiStickerFrame(EntityType<? extends StickerFrame> entityType, Level level) {
		super(entityType, level);
	}

	public GuiStickerFrame(Level level, BlockPos pos, Direction facingDirection) {
		super(FrameRegistry.GUI_STICKER_FRAME.get(), level, pos, facingDirection);
	}

	@Override
	protected ItemStack getFrameItemStack() {
		return new ItemStack(FrameRegistry.GUI_STICKER_FRAME_ITEM.get());
	}

	public boolean usesGuiDisplay() {
		return true;
	}
}

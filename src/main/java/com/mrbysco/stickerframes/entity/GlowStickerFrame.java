package com.mrbysco.stickerframes.entity;

import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GlowStickerFrame extends StickerFrame {
	public GlowStickerFrame(EntityType<? extends StickerFrame> entityType, Level level) {
		super(entityType, level);
	}

	public GlowStickerFrame(Level level, BlockPos pos, Direction facingDirection) {
		super(FrameRegistry.GLOW_STICKER_FRAME.get(), level, pos, facingDirection);
	}

	@Override
	protected ItemStack getFrameItemStack() {
		return new ItemStack(FrameRegistry.GLOW_STICKER_FRAME_ITEM.get());
	}

	public SoundEvent getRemoveItemSound() {
		return SoundEvents.GLOW_ITEM_FRAME_REMOVE_ITEM;
	}

	public SoundEvent getBreakSound() {
		return SoundEvents.GLOW_ITEM_FRAME_BREAK;
	}

	public SoundEvent getPlaceSound() {
		return SoundEvents.GLOW_ITEM_FRAME_PLACE;
	}

	public SoundEvent getAddItemSound() {
		return SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM;
	}

	public SoundEvent getRotateItemSound() {
		return SoundEvents.GLOW_ITEM_FRAME_ROTATE_ITEM;
	}

	public boolean isGlowing() {
		return true;
	}
}

package com.mrbysco.stickerframes.entity;

import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class GuiStickerFrame extends StickerFrame {
	public GuiStickerFrame(EntityType<? extends StickerFrame> entityType, Level level) {
		super(entityType, level);
	}

	public GuiStickerFrame(Level level, BlockPos pos, Direction facingDirection) {
		super(FrameRegistry.GUI_STICKER_FRAME.get(), level, pos, facingDirection);
	}

	public GuiStickerFrame(PlayMessages.SpawnEntity spawnEntity, Level level) {
		this(FrameRegistry.GUI_STICKER_FRAME.get(), level);
	}

	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected ItemStack getFrameItemStack() {
		return new ItemStack(FrameRegistry.GUI_STICKER_FRAME_ITEM.get());
	}

	public boolean usesGuiDisplay() {
		return true;
	}
}

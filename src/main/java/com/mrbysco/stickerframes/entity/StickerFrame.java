package com.mrbysco.stickerframes.entity;

import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class StickerFrame extends ItemFrame implements IEntityAdditionalSpawnData {
	public StickerFrame(EntityType<? extends StickerFrame> entityType, Level level) {
		super(entityType, level);
	}

	public StickerFrame(Level level, BlockPos pos, Direction facingDirection) {
		super(FrameRegistry.STICKER_FRAME.get(), level, pos, facingDirection);
	}

	public StickerFrame(EntityType<? extends StickerFrame> entityType, Level level, BlockPos pos, Direction facingDirection) {
		super(entityType, level, pos, facingDirection);
	}

	public StickerFrame(PlayMessages.SpawnEntity spawnEntity, Level level) {
		this(FrameRegistry.STICKER_FRAME.get(), level);
	}

	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(this.direction.get3DDataValue());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		this.setDirection(Direction.from3DDataValue(additionalData.readInt()));
	}

	@Override
	protected ItemStack getFrameItemStack() {
		return new ItemStack(FrameRegistry.GLOW_STICKER_FRAME_ITEM.get());
	}

	public boolean isGlowing() {
		return false;
	}

	public boolean usesGuiDisplay() {
		return false;
	}
}

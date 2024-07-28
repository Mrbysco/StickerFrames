package com.mrbysco.stickerframes.item;

import com.mrbysco.stickerframes.entity.StickerFrame;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StickerFrameItemCustom extends CustomHangingEntityItem {
	public StickerFrameItemCustom(EntityType<? extends StickerFrame> entityType, Item.Properties properties) {
		super(entityType, properties);
	}

	@Override
	protected boolean mayPlace(Player player, Direction direction, ItemStack hangingEntityStack, BlockPos pos) {
		return !player.level().isOutsideBuildHeight(pos) && player.mayUseItemAt(pos, direction, hangingEntityStack);
	}
}
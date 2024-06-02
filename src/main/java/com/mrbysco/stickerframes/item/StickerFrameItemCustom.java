package com.mrbysco.stickerframes.item;

import com.mrbysco.stickerframes.entity.StickerFrame;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StickerFrameItemCustom extends CustomHangingEntityItem {
	public StickerFrameItemCustom(EntityType<? extends StickerFrame> entityTypeSupplier, Item.Properties pProperties) {
		super(entityTypeSupplier, pProperties);
	}

	@Override
	protected boolean mayPlace(Player pPlayer, Direction pDirection, ItemStack pItemStack, BlockPos pPos) {
		return !pPlayer.level().isOutsideBuildHeight(pPos) && pPlayer.mayUseItemAt(pPos, pDirection, pItemStack);
	}
}
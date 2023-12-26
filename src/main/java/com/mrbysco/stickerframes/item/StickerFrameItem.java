package com.mrbysco.stickerframes.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class StickerFrameItem extends HangingItem {
	public StickerFrameItem(Supplier<EntityType<? extends HangingEntity>> entityTypeSupplier, Item.Properties pProperties) {
		super(entityTypeSupplier, pProperties);
	}

	protected boolean mayPlace(Player pPlayer, Direction pDirection, ItemStack pItemStack, BlockPos pPos) {
		return !pPlayer.level().isOutsideBuildHeight(pPos) && pPlayer.mayUseItemAt(pPos, pDirection, pItemStack);
	}
}
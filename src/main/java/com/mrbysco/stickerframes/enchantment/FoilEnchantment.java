package com.mrbysco.stickerframes.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class FoilEnchantment extends Enchantment {
	public FoilEnchantment(EnchantmentDefinition definition) {
		super(definition);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return true;
	}
}

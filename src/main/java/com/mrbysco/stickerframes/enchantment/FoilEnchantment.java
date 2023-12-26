package com.mrbysco.stickerframes.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FoilEnchantment extends Enchantment {
	public FoilEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
		super(rarity, category, slots);
	}

	public int getMinCost(int level) {
		return 10;
	}

	public int getMaxCost(int level) {
		return 30;
	}
}

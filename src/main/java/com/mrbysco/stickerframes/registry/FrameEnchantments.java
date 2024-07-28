package com.mrbysco.stickerframes.registry;

import com.mrbysco.stickerframes.StickerFrames;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class FrameEnchantments {
	public static final ResourceKey<Enchantment> FOILED = key("foiled");

	public static void bootstrap(BootstrapContext<Enchantment> context) {
		HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

		register(
				context,
				FOILED,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ItemTags.VANISHING_ENCHANTABLE),
								5, 1,
								Enchantment.constantCost(10),
								Enchantment.constantCost(30), 1, EquipmentSlotGroup.ANY)
				)
		);
	}

	private static ResourceKey<Enchantment> key(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(StickerFrames.MOD_ID, path));
	}

	private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> resourceKey,
	                             Enchantment.Builder builder) {
		context.register(resourceKey, builder.build(resourceKey.location()));
	}
}

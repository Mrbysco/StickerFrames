package com.mrbysco.stickerframes.registry;

import com.mrbysco.stickerframes.StickerFrames;
import com.mrbysco.stickerframes.enchantment.FoilEnchantment;
import com.mrbysco.stickerframes.entity.GlowGuiStickerFrame;
import com.mrbysco.stickerframes.entity.GlowStickerFrame;
import com.mrbysco.stickerframes.entity.GuiStickerFrame;
import com.mrbysco.stickerframes.entity.StickerFrame;
import com.mrbysco.stickerframes.item.StickerFrameItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FrameRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StickerFrames.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, StickerFrames.MOD_ID);
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, StickerFrames.MOD_ID);

	public static final EnchantmentCategory ALL = EnchantmentCategory.create(StickerFrames.MOD_ID + ":all", i -> true);

	public static final Supplier<Enchantment> FOILED = ENCHANTMENTS.register("foiled", () -> new FoilEnchantment(Enchantment.Rarity.UNCOMMON, ALL, EquipmentSlot.values()));

	public static final RegistryObject<Item> STICKER_FRAME_ITEM = ITEMS.register("sticker_frame", () -> new StickerFrameItem(FrameRegistry.STICKER_FRAME::get, itemProperties()));
	public static final RegistryObject<Item> GLOW_STICKER_FRAME_ITEM = ITEMS.register("glow_sticker_frame", () -> new StickerFrameItem(FrameRegistry.GLOW_STICKER_FRAME::get, itemProperties()));
	public static final RegistryObject<Item> GUI_STICKER_FRAME_ITEM = ITEMS.register("gui_sticker_frame", () -> new StickerFrameItem(FrameRegistry.GUI_STICKER_FRAME::get, itemProperties()));
	public static final RegistryObject<Item> GLOW_GUI_STICKER_FRAME_ITEM = ITEMS.register("glow_gui_sticker_frame", () -> new StickerFrameItem(FrameRegistry.GLOW_GUI_STICKER_FRAME::get, itemProperties()));

	public static Item.Properties itemProperties() {
		return new Item.Properties().tab(FrameTab.TAB);
	}

	public static final RegistryObject<EntityType<StickerFrame>> STICKER_FRAME = ENTITY_TYPES.register("sticker_frame", () ->
			EntityType.Builder.<StickerFrame>of(StickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.setCustomClientFactory(StickerFrame::new).build("sticker_frame"));

	public static final RegistryObject<EntityType<GlowStickerFrame>> GLOW_STICKER_FRAME = ENTITY_TYPES.register("glow_sticker_frame", () ->
			EntityType.Builder.<GlowStickerFrame>of(GlowStickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.setCustomClientFactory(GlowStickerFrame::new).build("glow_sticker_frame"));

	public static final RegistryObject<EntityType<GuiStickerFrame>> GUI_STICKER_FRAME = ENTITY_TYPES.register("gui_sticker_frame", () ->
			EntityType.Builder.<GuiStickerFrame>of(GuiStickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.setCustomClientFactory(GuiStickerFrame::new).build("gui_sticker_frame"));

	public static final RegistryObject<EntityType<GlowGuiStickerFrame>> GLOW_GUI_STICKER_FRAME = ENTITY_TYPES.register("glow_gui_sticker_frame", () ->
			EntityType.Builder.<GlowGuiStickerFrame>of(GlowGuiStickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.setCustomClientFactory(GlowGuiStickerFrame::new).build("glow_gui_sticker_frame"));
}

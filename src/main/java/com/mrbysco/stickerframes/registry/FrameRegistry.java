package com.mrbysco.stickerframes.registry;

import com.mrbysco.stickerframes.StickerFrames;
import com.mrbysco.stickerframes.enchantment.FoilEnchantment;
import com.mrbysco.stickerframes.entity.GlowGuiStickerFrame;
import com.mrbysco.stickerframes.entity.GlowStickerFrame;
import com.mrbysco.stickerframes.entity.GuiStickerFrame;
import com.mrbysco.stickerframes.entity.StickerFrame;
import com.mrbysco.stickerframes.item.StickerFrameItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class FrameRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StickerFrames.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, StickerFrames.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StickerFrames.MOD_ID);
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, StickerFrames.MOD_ID);

	public static final EnchantmentCategory ALL = EnchantmentCategory.create(StickerFrames.MOD_ID + ":all", i -> true);

	public static final Supplier<Enchantment> FOILED = ENCHANTMENTS.register("foiled", () -> new FoilEnchantment(Enchantment.Rarity.UNCOMMON, ALL, EquipmentSlot.values()));

	public static final DeferredItem<StickerFrameItem> STICKER_FRAME_ITEM = ITEMS.register("sticker_frame", () -> new StickerFrameItem(FrameRegistry.STICKER_FRAME::get, new Item.Properties()));
	public static final DeferredItem<StickerFrameItem> GLOW_STICKER_FRAME_ITEM = ITEMS.register("glow_sticker_frame", () -> new StickerFrameItem(FrameRegistry.GLOW_STICKER_FRAME::get, new Item.Properties()));
	public static final DeferredItem<StickerFrameItem> GUI_STICKER_FRAME_ITEM = ITEMS.register("gui_sticker_frame", () -> new StickerFrameItem(FrameRegistry.GUI_STICKER_FRAME::get, new Item.Properties()));
	public static final DeferredItem<StickerFrameItem> GLOW_GUI_STICKER_FRAME_ITEM = ITEMS.register("glow_gui_sticker_frame", () -> new StickerFrameItem(FrameRegistry.GLOW_GUI_STICKER_FRAME::get, new Item.Properties()));

	public static final Supplier<EntityType<StickerFrame>> STICKER_FRAME = ENTITY_TYPES.register("sticker_frame", () ->
			EntityType.Builder.<StickerFrame>of(StickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.build("sticker_frame"));

	public static final Supplier<EntityType<GlowStickerFrame>> GLOW_STICKER_FRAME = ENTITY_TYPES.register("glow_sticker_frame", () ->
			EntityType.Builder.<GlowStickerFrame>of(GlowStickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.build("glow_sticker_frame"));

	public static final Supplier<EntityType<GuiStickerFrame>> GUI_STICKER_FRAME = ENTITY_TYPES.register("gui_sticker_frame", () ->
			EntityType.Builder.<GuiStickerFrame>of(GuiStickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.build("gui_sticker_frame"));

	public static final Supplier<EntityType<GlowGuiStickerFrame>> GLOW_GUI_STICKER_FRAME = ENTITY_TYPES.register("glow_gui_sticker_frame", () ->
			EntityType.Builder.<GlowGuiStickerFrame>of(GlowGuiStickerFrame::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.build("glow_gui_sticker_frame"));

	public static final Supplier<CreativeModeTab> FRAME_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> FrameRegistry.STICKER_FRAME_ITEM.get().getDefaultInstance())
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.stickerframes"))
			.displayItems((parameters, output) -> {
				List<ItemStack> stacks = FrameRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}

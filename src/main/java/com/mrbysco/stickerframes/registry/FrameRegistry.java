package com.mrbysco.stickerframes.registry;

import com.mrbysco.stickerframes.StickerFrames;
import com.mrbysco.stickerframes.entity.GlowGuiStickerFrame;
import com.mrbysco.stickerframes.entity.GlowStickerFrame;
import com.mrbysco.stickerframes.entity.GuiStickerFrame;
import com.mrbysco.stickerframes.entity.StickerFrame;
import com.mrbysco.stickerframes.item.StickerFrameItemCustom;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class FrameRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StickerFrames.MOD_ID);
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(StickerFrames.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StickerFrames.MOD_ID);

	public static final DeferredItem<StickerFrameItemCustom> STICKER_FRAME_ITEM = ITEMS.registerItem("sticker_frame", (properties) -> new StickerFrameItemCustom(FrameRegistry.STICKER_FRAME.get(), properties));
	public static final DeferredItem<StickerFrameItemCustom> GLOW_STICKER_FRAME_ITEM = ITEMS.registerItem("glow_sticker_frame", (properties) -> new StickerFrameItemCustom(FrameRegistry.GLOW_STICKER_FRAME.get(), properties));
	public static final DeferredItem<StickerFrameItemCustom> GUI_STICKER_FRAME_ITEM = ITEMS.registerItem("gui_sticker_frame", (properties) -> new StickerFrameItemCustom(FrameRegistry.GUI_STICKER_FRAME.get(), properties));
	public static final DeferredItem<StickerFrameItemCustom> GLOW_GUI_STICKER_FRAME_ITEM = ITEMS.registerItem("glow_gui_sticker_frame", (properties) -> new StickerFrameItemCustom(FrameRegistry.GLOW_GUI_STICKER_FRAME.get(), properties));

	public static final Supplier<EntityType<StickerFrame>> STICKER_FRAME = ENTITY_TYPES.registerEntityType("sticker_frame",
			StickerFrame::new,
			MobCategory.MISC,
			builder -> builder
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
	);

	public static final Supplier<EntityType<GlowStickerFrame>> GLOW_STICKER_FRAME = ENTITY_TYPES.registerEntityType("glow_sticker_frame",
			GlowStickerFrame::new,
			MobCategory.MISC,
			builder -> builder
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
	);

	public static final Supplier<EntityType<GuiStickerFrame>> GUI_STICKER_FRAME = ENTITY_TYPES.registerEntityType("gui_sticker_frame",
			GuiStickerFrame::new,
			MobCategory.MISC,
			builder -> builder
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
	);

	public static final Supplier<EntityType<GlowGuiStickerFrame>> GLOW_GUI_STICKER_FRAME = ENTITY_TYPES.registerEntityType("glow_gui_sticker_frame",
			GlowGuiStickerFrame::new,
			MobCategory.MISC,
			builder -> builder
					.sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
	);

	public static final Supplier<CreativeModeTab> FRAME_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> FrameRegistry.STICKER_FRAME_ITEM.get().getDefaultInstance())
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.stickerframes"))
			.displayItems((parameters, output) -> {
				List<ItemStack> stacks = FrameRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}

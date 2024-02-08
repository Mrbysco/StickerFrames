package com.mrbysco.stickerframes.registry;

import com.mrbysco.stickerframes.StickerFrames;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FrameTab {
	public static final CreativeModeTab TAB = new CreativeModeTab(StickerFrames.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(FrameRegistry.STICKER_FRAME_ITEM.get());
		}
	};
}

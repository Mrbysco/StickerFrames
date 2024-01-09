package com.mrbysco.stickerframes;

import com.mojang.logging.LogUtils;
import com.mrbysco.stickerframes.client.ClientHandler;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(StickerFrames.MOD_ID)
public class StickerFrames {
	public static final String MOD_ID = "stickerframes";
	private static final Logger LOGGER = LogUtils.getLogger();

	public StickerFrames(IEventBus eventBus) {
		FrameRegistry.ITEMS.register(eventBus);
		FrameRegistry.ENTITY_TYPES.register(eventBus);
		FrameRegistry.CREATIVE_MODE_TABS.register(eventBus);
		FrameRegistry.ENCHANTMENTS.register(eventBus);

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
		}
	}
}

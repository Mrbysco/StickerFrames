package com.mrbysco.stickerframes;

import com.mojang.logging.LogUtils;
import com.mrbysco.stickerframes.client.ClientHandler;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(StickerFrames.MOD_ID)
public class StickerFrames {
	public static final String MOD_ID = "stickerframes";
	public static final Logger LOGGER = LogUtils.getLogger();

	public StickerFrames(IEventBus eventBus, Dist dist) {
		FrameRegistry.ITEMS.register(eventBus);
		FrameRegistry.ENTITY_TYPES.register(eventBus);
		FrameRegistry.CREATIVE_MODE_TABS.register(eventBus);

		if (dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
		}
	}

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

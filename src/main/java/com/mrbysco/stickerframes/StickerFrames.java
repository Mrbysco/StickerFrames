package com.mrbysco.stickerframes;

import com.mojang.logging.LogUtils;
import com.mrbysco.stickerframes.client.ClientHandler;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(StickerFrames.MOD_ID)
public class StickerFrames {
	public static final String MOD_ID = "stickerframes";
	private static final Logger LOGGER = LogUtils.getLogger();

	public StickerFrames() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		FrameRegistry.ITEMS.register(eventBus);
		FrameRegistry.ENTITY_TYPES.register(eventBus);
		FrameRegistry.CREATIVE_MODE_TABS.register(eventBus);
		FrameRegistry.ENCHANTMENTS.register(eventBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerEntityRenders);
		});
	}
}

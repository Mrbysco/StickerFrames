package com.mrbysco.stickerframes.client;

import com.mrbysco.stickerframes.client.renderer.StickerFrameRenderer;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(FrameRegistry.STICKER_FRAME.get(), StickerFrameRenderer::new);
		event.registerEntityRenderer(FrameRegistry.GLOW_STICKER_FRAME.get(), StickerFrameRenderer::new);
		event.registerEntityRenderer(FrameRegistry.GUI_STICKER_FRAME.get(), StickerFrameRenderer::new);
		event.registerEntityRenderer(FrameRegistry.GLOW_GUI_STICKER_FRAME.get(), StickerFrameRenderer::new);
	}
}

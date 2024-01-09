package com.mrbysco.stickerframes.data;


import com.mrbysco.stickerframes.StickerFrames;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FrameDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new Recipes(packOutput, event.getLookupProvider()));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new Language(packOutput));
			generator.addProvider(true, new ItemModels(packOutput, helper));
		}
	}

	private static class Recipes extends RecipeProvider {
		public Recipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(packOutput, lookupProvider);
		}

		@Override
		protected void buildRecipes(RecipeOutput recipeOutput) {
			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FrameRegistry.STICKER_FRAME_ITEM.get())
					.pattern("XXX")
					.pattern("XFX")
					.pattern("XXX")
					.define('X', Ingredient.of(Tags.Items.RODS_WOODEN))
					.define('F', Items.PAINTING)
					.unlockedBy("has_painting", has(Items.PAINTING))
					.save(recipeOutput);
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, FrameRegistry.GLOW_STICKER_FRAME_ITEM.get())
					.requires(FrameRegistry.STICKER_FRAME_ITEM.get())
					.requires(Items.GLOW_INK_SAC)
					.unlockedBy("has_sticker_frame", has(FrameRegistry.STICKER_FRAME_ITEM.get()))
					.unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
					.save(recipeOutput);

			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FrameRegistry.GUI_STICKER_FRAME_ITEM.get())
					.pattern("XXX")
					.pattern("XFX")
					.pattern("XXX")
					.define('X', Ingredient.of(Tags.Items.RODS_WOODEN))
					.define('F', Items.ITEM_FRAME)
					.unlockedBy("has_item_frame", has(Items.ITEM_FRAME))
					.save(recipeOutput);
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, FrameRegistry.GLOW_GUI_STICKER_FRAME_ITEM.get())
					.requires(FrameRegistry.GUI_STICKER_FRAME_ITEM.get())
					.requires(Items.GLOW_INK_SAC)
					.unlockedBy("has_gui_sticker_frame", has(FrameRegistry.GUI_STICKER_FRAME_ITEM.get()))
					.unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
					.save(recipeOutput);
		}
	}

	private static class Language extends LanguageProvider {
		public Language(PackOutput packOutput) {
			super(packOutput, StickerFrames.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			this.add("itemGroup.stickerframes", "Sticker Frames");

			this.addItem(FrameRegistry.STICKER_FRAME_ITEM, "Sticker Frame");
			this.addEntityType(FrameRegistry.STICKER_FRAME, "Sticker Frame");
			this.addItem(FrameRegistry.GLOW_STICKER_FRAME_ITEM, "Glow Sticker Frame");
			this.addEntityType(FrameRegistry.GLOW_STICKER_FRAME, "Glow Sticker Frame");

			this.addItem(FrameRegistry.GUI_STICKER_FRAME_ITEM, "GUI Sticker Frame");
			this.addEntityType(FrameRegistry.GUI_STICKER_FRAME, "GUI Sticker Frame");
			this.addItem(FrameRegistry.GLOW_GUI_STICKER_FRAME_ITEM, "Glow GUI Sticker Frame");
			this.addEntityType(FrameRegistry.GLOW_GUI_STICKER_FRAME, "Glow GUI Sticker Frame");

			addEnchantment(FrameRegistry.FOILED, "Foiled");
			addEnchantmentDescription(FrameRegistry.FOILED, "Just enables the enchantment glint");
		}

		private void addEnchantmentDescription(Supplier<? extends Enchantment> key, String description) {
			add(key.get().getDescriptionId() + ".desc", description);
		}
	}

	private static class ItemModels extends ItemModelProvider {
		public ItemModels(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, StickerFrames.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			FrameRegistry.ITEMS.getEntries()
					.forEach(item -> {
						String path = item.getId().getPath();
						singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
					});
		}
	}
}

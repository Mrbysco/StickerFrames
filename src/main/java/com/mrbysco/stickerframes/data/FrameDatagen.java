package com.mrbysco.stickerframes.data;


import com.mrbysco.stickerframes.StickerFrames;
import com.mrbysco.stickerframes.registry.FrameEnchantments;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class FrameDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new StickerRecipes.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new StickerEnchantmentTags(packOutput, lookupProvider));
		generator.addProvider(true, new StickerDatapack(
				packOutput,
				event.getLookupProvider(),
				Set.of(StickerFrames.MOD_ID)
		));

		generator.addProvider(true, new StickerLanguage(packOutput));
		generator.addProvider(true, new StickerModels(packOutput));

	}

	private static class StickerDatapack extends DatapackBuiltinEntriesProvider {
		public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
				.add(Registries.ENCHANTMENT, FrameEnchantments::bootstrap);


		public StickerDatapack(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds) {
			super(output, registries, BUILDER, modIds);
		}
	}

	private static class StickerRecipes extends RecipeProvider {
		public StickerRecipes(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			super(provider, recipeOutput);
		}

		@Override
		protected void buildRecipes() {
			shaped(RecipeCategory.DECORATIONS, FrameRegistry.STICKER_FRAME_ITEM.get())
					.pattern("XXX")
					.pattern("XFX")
					.pattern("XXX")
					.define('X', Ingredient.of(tagSet(Tags.Items.RODS_WOODEN)))
					.define('F', Items.PAINTING)
					.unlockedBy("has_painting", has(Items.PAINTING))
					.save(output);
			shapeless(RecipeCategory.DECORATIONS, FrameRegistry.GLOW_STICKER_FRAME_ITEM.get())
					.requires(FrameRegistry.STICKER_FRAME_ITEM.get())
					.requires(Items.GLOW_INK_SAC)
					.unlockedBy("has_sticker_frame", has(FrameRegistry.STICKER_FRAME_ITEM.get()))
					.unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
					.save(output);

			shaped(RecipeCategory.DECORATIONS, FrameRegistry.GUI_STICKER_FRAME_ITEM.get())
					.pattern("XXX")
					.pattern("XFX")
					.pattern("XXX")
					.define('X', Ingredient.of(tagSet(Tags.Items.RODS_WOODEN)))
					.define('F', Items.ITEM_FRAME)
					.unlockedBy("has_item_frame", has(Items.ITEM_FRAME))
					.save(output);
			shapeless(RecipeCategory.DECORATIONS, FrameRegistry.GLOW_GUI_STICKER_FRAME_ITEM.get())
					.requires(FrameRegistry.GUI_STICKER_FRAME_ITEM.get())
					.requires(Items.GLOW_INK_SAC)
					.unlockedBy("has_gui_sticker_frame", has(FrameRegistry.GUI_STICKER_FRAME_ITEM.get()))
					.unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
					.save(output);
		}

		private HolderSet<Item> tagSet(TagKey<Item> tagKey) {
			return this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(tagKey);
		}

		public static class Runner extends RecipeProvider.Runner {
			public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
				super(output, completableFuture);
			}

			@Override
			protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
				return new StickerRecipes(provider, recipeOutput);
			}

			@Override
			public String getName() {
				return "StickerFrames Recipes";
			}
		}
	}

	private static class StickerEnchantmentTags extends EnchantmentTagsProvider {

		public StickerEnchantmentTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider, StickerFrames.MOD_ID);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(EnchantmentTags.NON_TREASURE).addOptional(FrameEnchantments.FOILED);
		}
	}

	private static class StickerLanguage extends LanguageProvider {
		public StickerLanguage(PackOutput packOutput) {
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

			addEnchantment(FrameEnchantments.FOILED, "Foiled");
			addEnchantmentDescription(FrameEnchantments.FOILED, "Just enables the enchantment glint");
		}

		private void addEnchantment(ResourceKey<Enchantment> key, String name) {
			Identifier location = key.identifier();
			add("enchantment." + location.getNamespace() + "." + location.getPath(), name);
		}

		private void addEnchantmentDescription(ResourceKey<Enchantment> key, String description) {
			Identifier location = key.identifier();
			add("enchantment." + location.getNamespace() + "." + location.getPath() + ".desc", description);
		}
	}

	private static class StickerModels extends ModelProvider {
		public StickerModels(PackOutput packOutput) {
			super(packOutput, StickerFrames.MOD_ID);
		}

		@Override
		protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
			FrameRegistry.ITEMS.getEntries()
					.forEach(item -> {
						itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
					});
		}
	}
}

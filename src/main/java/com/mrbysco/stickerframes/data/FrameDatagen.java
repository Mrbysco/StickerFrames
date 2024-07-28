package com.mrbysco.stickerframes.data;


import com.mrbysco.stickerframes.StickerFrames;
import com.mrbysco.stickerframes.registry.FrameEnchantments;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FrameDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		CompletableFuture<HolderLookup.Provider> fullProvider = CompletableFuture.supplyAsync(() -> FrameDatagen.getProvider().full());

		if (event.includeServer()) {
			generator.addProvider(true, new StickerRecipes(packOutput, lookupProvider));
			generator.addProvider(true, new StickerEnchantmentTags(packOutput, fullProvider, helper));
			generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
					packOutput, CompletableFuture.supplyAsync(FrameDatagen::getProvider), Set.of(StickerFrames.MOD_ID)));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new StickerLanguage(packOutput));
			generator.addProvider(true, new StickerItemModels(packOutput, helper));
		}
	}

	private static RegistrySetBuilder.PatchedRegistries getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.ENCHANTMENT, FrameEnchantments::bootstrap);

		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}

	private static class StickerRecipes extends RecipeProvider {
		public StickerRecipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
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

	private static class StickerEnchantmentTags extends EnchantmentTagsProvider {

		public StickerEnchantmentTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
		                              @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, StickerFrames.MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(EnchantmentTags.NON_TREASURE).add(FrameEnchantments.FOILED);
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
			ResourceLocation location = key.location();
			add("enchantment." + location.getNamespace() + "." + location.getPath(), name);
		}

		private void addEnchantmentDescription(ResourceKey<Enchantment> key, String description) {
			ResourceLocation location = key.location();
			add("enchantment." + location.getNamespace() + "." + location.getPath() + ".desc", description);
		}
	}

	private static class StickerItemModels extends ItemModelProvider {
		public StickerItemModels(PackOutput packOutput, ExistingFileHelper helper) {
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

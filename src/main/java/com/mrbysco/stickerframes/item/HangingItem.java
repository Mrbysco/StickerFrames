package com.mrbysco.stickerframes.item;

import com.mrbysco.stickerframes.entity.GlowGuiStickerFrame;
import com.mrbysco.stickerframes.entity.GlowStickerFrame;
import com.mrbysco.stickerframes.entity.GuiStickerFrame;
import com.mrbysco.stickerframes.entity.StickerFrame;
import com.mrbysco.stickerframes.registry.FrameRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class HangingItem extends Item {
	private static final Component TOOLTIP_RANDOM_VARIANT = Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);
	private final Supplier<EntityType<? extends HangingEntity>> typeSupplier;

	public HangingItem(Supplier<EntityType<? extends HangingEntity>> typeSupplier, Item.Properties pProperties) {
		super(pProperties);
		this.typeSupplier = typeSupplier;
	}

	/**
	 * Called when this item is used when targeting a Block
	 */
	public InteractionResult useOn(UseOnContext context) {
		BlockPos blockpos = context.getClickedPos();
		Direction clickedFace = context.getClickedFace();
		BlockPos relativePos = blockpos.relative(clickedFace);
		Player player = context.getPlayer();
		ItemStack itemstack = context.getItemInHand();
		if (player != null && !this.mayPlace(player, clickedFace, itemstack, relativePos)) {
			return InteractionResult.FAIL;
		} else {
			Level level = context.getLevel();
			HangingEntity hangingentity;
			if (getType() == FrameRegistry.STICKER_FRAME.get()) {
				hangingentity = new StickerFrame(level, relativePos, clickedFace);
			} else if (getType() == FrameRegistry.GUI_STICKER_FRAME.get()) {
				hangingentity = new GuiStickerFrame(level, relativePos, clickedFace);
			} else {
				if (getType() == FrameRegistry.GLOW_STICKER_FRAME.get()) {
					hangingentity = new GlowStickerFrame(level, relativePos, clickedFace);
				} else if (getType() == FrameRegistry.GLOW_GUI_STICKER_FRAME.get()) {
					hangingentity = new GlowGuiStickerFrame(level, relativePos, clickedFace);
				} else {
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}

			CompoundTag compoundtag = itemstack.getTag();
			if (compoundtag != null) {
				EntityType.updateCustomEntityTag(level, player, hangingentity, compoundtag);
			}

			if (hangingentity.survives()) {
				if (!level.isClientSide) {
					hangingentity.playPlacementSound();
					level.gameEvent(player, GameEvent.ENTITY_PLACE, hangingentity.position());
					level.addFreshEntity(hangingentity);
				}

				itemstack.shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				return InteractionResult.CONSUME;
			}
		}
	}

	public EntityType<? extends HangingEntity> getType() {
		return typeSupplier.get();
	}

	protected boolean mayPlace(Player player, Direction direction, ItemStack hangingStack, BlockPos pos) {
		return !direction.getAxis().isVertical() && player.mayUseItemAt(pos, direction, hangingStack);
	}

	/**
	 * Allows items to add custom lines of information to the mouseover description.
	 */
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> componentList, TooltipFlag flag) {
		super.appendHoverText(stack, level, componentList, flag);
		if (getType() == EntityType.PAINTING) {
			CompoundTag compoundtag = stack.getTag();
			if (compoundtag != null && compoundtag.contains("EntityTag", 10)) {
				CompoundTag compoundtag1 = compoundtag.getCompound("EntityTag");
				Painting.loadVariant(compoundtag1).ifPresentOrElse((p_270767_) -> {
					p_270767_.unwrapKey().ifPresent((p_270217_) -> {
						componentList.add(Component.translatable(p_270217_.location().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW));
						componentList.add(Component.translatable(p_270217_.location().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY));
					});
					componentList.add(Component.translatable("painting.dimensions", Mth.positiveCeilDiv(p_270767_.value().getWidth(), 16), Mth.positiveCeilDiv(p_270767_.value().getHeight(), 16)));
				}, () -> {
					componentList.add(TOOLTIP_RANDOM_VARIANT);
				});
			} else if (flag.isCreative()) {
				componentList.add(TOOLTIP_RANDOM_VARIANT);
			}
		}

	}
}
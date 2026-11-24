// 免死附魔的复活流程：清空掉落、保留 1 点生命、给再生 / 吸收 / 抗火与黑暗。

package com.cocreateenchantments.support;

import com.cocreateenchantments.config.CceTunables;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public final class DeathSaveSupport {
	private static final byte TOTEM_ANIMATION_EVENT = 35;
	private static final int REGEN_SECONDS = 45;
	private static final int REGEN_AMPLIFIER = 1;
	private static final int ABSORPTION_SECONDS = 5;
	private static final int ABSORPTION_AMPLIFIER = 1;
	private static final int FIRE_RESISTANCE_SECONDS = 40;

	private static final EquipmentSlot[] EQUIPMENT_SLOTS = {
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
	};

	private DeathSaveSupport() {
	}

	public static void performResurrection(ServerPlayer player, int darknessTicks) {
		player.setHealth(1.0F);
		player.removeEffect(MobEffects.REGENERATION);
		player.removeEffect(MobEffects.ABSORPTION);
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
				CceTunables.secondsToTicks(REGEN_SECONDS), REGEN_AMPLIFIER));
		player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,
				CceTunables.secondsToTicks(ABSORPTION_SECONDS), ABSORPTION_AMPLIFIER));
		player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
				CceTunables.secondsToTicks(FIRE_RESISTANCE_SECONDS), 0));
		if (darknessTicks > 0) {
			player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, darknessTicks, 0));
		}
		if (player.level() instanceof ServerLevel serverLevel) {
			serverLevel.broadcastEntityEvent(player, TOTEM_ANIMATION_EVENT);
		}
	}

	public static void dropEverything(ServerPlayer player, EquipmentSlot destroyedSlot) {
		List<ItemStack> drops = new ArrayList<>();
		for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
			if (!stack.isEmpty()) {
				drops.add(stack.copy());
				stack.setCount(0);
			}
		}
		for (EquipmentSlot slot : EQUIPMENT_SLOTS) {
			ItemStack stack = player.getItemBySlot(slot);
			if (stack.isEmpty()) {
				continue;
			}
			if (slot == destroyedSlot) {
				player.setItemSlot(slot, ItemStack.EMPTY);
			} else {
				drops.add(stack.copy());
				player.setItemSlot(slot, ItemStack.EMPTY);
			}
		}
		for (ItemStack drop : drops) {
			ItemEntity itemEntity = player.createItemStackToDrop(drop, true, false);
			if (itemEntity != null) {
				player.level().addFreshEntity(itemEntity);
			}
		}
	}
}

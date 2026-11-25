// 胸甲与副手槽位的附魔等级查询。

package com.cocreateenchantments.support;

import com.cocreateenchantments.CoCreateEnchantments;
import com.huziyang520.merlinlib.api.MerlinApi;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CceSupport {
	private static final EquipmentSlot[] SLOTS = {EquipmentSlot.CHEST, EquipmentSlot.OFFHAND};

	private CceSupport() {
	}

	public static int wornLevel(LivingEntity entity, Holder<Enchantment> enchantment) {
		int best = 0;
		for (EquipmentSlot slot : SLOTS) {
			best = Math.max(best, entity.getItemBySlot(slot).getEnchantments().getLevel(enchantment));
		}
		return best;
	}

	public static EquipmentSlot findSlotWith(LivingEntity entity, String path) {
		if (!(entity instanceof ServerPlayer player)) {
			return null;
		}
		return MerlinApi.playerDeath().findSlotWith(player,
				Identifier.fromNamespaceAndPath(CoCreateEnchantments.MOD_ID, path),
				EquipmentSlot.CHEST, EquipmentSlot.OFFHAND);
	}

	public static int getLevel(ItemStack stack, String path) {
		if (stack == null || stack.isEmpty()) {
			return 0;
		}
		Identifier wanted = Identifier.fromNamespaceAndPath(CoCreateEnchantments.MOD_ID, path);
		for (var entry : stack.getEnchantments().entrySet()) {
			Holder<Enchantment> holder = entry.getKey();
			boolean match = holder.unwrapKey()
					.map(key -> key.identifier().equals(wanted))
					.orElse(false);
			if (match) {
				return entry.getIntValue();
			}
		}
		return 0;
	}
}

// 掉落不死亡：致命伤时掉光物品换一次复活。

package com.cocreateenchantments.enchantment;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.config.CceTunables;
import com.cocreateenchantments.content.CceContent;
import com.cocreateenchantments.support.CceSupport;
import com.huziyang520.merlinlib.api.MerlinApi;
import com.huziyang520.merlinlib.api.PlayerDeathApi;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public final class UndyingDropEnchantment {
	private static Holder<Enchantment> HOLDER;

	private UndyingDropEnchantment() {
	}

	public static void register(HolderLookup.Provider registries) {
		HOLDER = CoCreateEnchantments.resolveEnchantment(registries, CceContent.UNDYING_DROP);
	}

	public static boolean trySave(ServerPlayer player, DamageSource source) {
		Holder<Enchantment> holder = HOLDER;
		if (holder == null) {
			return false;
		}
		int level = CceSupport.wornLevel(player, holder);
		if (level <= 0) {
			return false;
		}
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return false;
		}
		EquipmentSlot destroyedSlot = CceSupport.findSlotWith(player, CceContent.UNDYING_DROP);
		if (destroyedSlot == null) {
			return false;
		}
		MerlinApi.playerDeath().dropInventory(player, destroyedSlot);
		MerlinApi.playerDeath().resurrect(player, new PlayerDeathApi.ResurrectionOptions(
				CceTunables.undyingDropDarknessTicks(level),
				CceTunables.secondsToTicks(45), 1,
				CceTunables.secondsToTicks(5), 1,
				CceTunables.secondsToTicks(40)));
		return true;
	}
}

// 中国人：胸甲或副手解锁创造飞行，空中受击额外磨损胸甲。

package com.cocreateenchantments.enchantment;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.config.CceTunables;
import com.cocreateenchantments.content.CceContent;
import com.cocreateenchantments.support.CceSupport;
import com.huziyang520.merlinlib.api.EntityCounter;
import com.huziyang520.merlinlib.event.BuiltInEvents;
import com.huziyang520.merlinlib.event.EnchantmentContext;
import com.huziyang520.merlinlib.event.EnchantmentEventRegistrar;
import com.huziyang520.merlinlib.event.GlobalEvents;
import com.huziyang520.merlinlib.event.LivingEntityTickEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ChineseEnchantment {
	private static Holder<Enchantment> HOLDER;
	private static final Identifier FLY_GRANTED = Identifier.parse(CceContent.FLY_GRANTED_MARKER);

	private ChineseEnchantment() {
	}

	public static void registerCallbacks(EnchantmentEventRegistrar registrar, HolderLookup.Provider registries) {
		HOLDER = CoCreateEnchantments.resolveEnchantment(registries, CceContent.CHINESE);
		GlobalEvents.enableLivingEntityTick();
		GlobalEvents.LIVING_ENTITY_TICK.register(ChineseEnchantment::onLivingTick);
		registrar.register(HOLDER, BuiltInEvents.POST_HURT, ChineseEnchantment::onPostHurt);
	}

	private static void onLivingTick(LivingEntityTickEvent event) {
		if (!(event.entity() instanceof ServerPlayer player)) {
			return;
		}
		boolean hasChinese = HOLDER != null && CceSupport.wornLevel(player, HOLDER) > 0;
		var abilities = player.getAbilities();
		boolean vanillaFlight = player.isCreative() || player.isSpectator();
		if (hasChinese) {
			if (!abilities.mayfly) {
				abilities.mayfly = true;
				player.onUpdateAbilities();
				EntityCounter.set(player, FLY_GRANTED, 1);
			}
		} else if (abilities.mayfly && EntityCounter.get(player, FLY_GRANTED) > 0) {
			EntityCounter.set(player, FLY_GRANTED, 0);
			if (vanillaFlight) {
				return;
			}
			abilities.mayfly = false;
			abilities.flying = false;
			player.onUpdateAbilities();
		}
	}

	private static void onPostHurt(BuiltInEvents.PostHurtEvent event, EnchantmentContext ctx) {
		if (!(event.target() instanceof ServerPlayer player) || player.onGround()) {
			return;
		}
		ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
		if (chestItem.isEmpty()) {
			return;
		}
		chestItem.hurtAndBreak(CceTunables.chineseAirborneDurability(event.amount()), player, EquipmentSlot.CHEST);
	}
}

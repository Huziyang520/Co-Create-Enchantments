// 中国人：胸甲或副手解锁创造飞行，拿下后立即恢复正常。

package com.cocreateenchantments.enchantment;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.content.CceContent;
import com.cocreateenchantments.support.CceSupport;
import com.huziyang520.merlinlib.api.EntityCounter;
import com.huziyang520.merlinlib.event.EnchantmentEventRegistrar;
import com.huziyang520.merlinlib.event.GlobalEvents;
import com.huziyang520.merlinlib.event.LivingEntityTickEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
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
	}

	private static void onLivingTick(LivingEntityTickEvent event) {
		if (!(event.entity() instanceof ServerPlayer player)) {
			return;
		}
		var abilities = player.getAbilities();
		boolean vanillaFlight = player.isCreative() || player.isSpectator();
		if (hasChinese(player)) {
			if (!abilities.mayfly) {
				abilities.mayfly = true;
				player.onUpdateAbilities();
			}
			// 每次 tick 都补记账：重登与换维度后玩家实体是新的，旧标记已丢失。
			if (!vanillaFlight) {
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

	private static boolean hasChinese(ServerPlayer player) {
		// 同时按捕获的 Holder 与 id 查等级：数据包重载后 Holder 实例可能已经不是物品里存的那一个。
		if (HOLDER != null && CceSupport.wornLevel(player, HOLDER) > 0) {
			return true;
		}
		return CceSupport.wornLevel(player, CceContent.CHINESE) > 0;
	}
}

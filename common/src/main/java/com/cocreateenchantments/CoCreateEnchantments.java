// Co-Create Enchantments 模组入口（前置：MerlinLib）。

package com.cocreateenchantments;

import com.cocreateenchantments.content.CceContent;
import com.cocreateenchantments.enchantment.ChineseEnchantment;
import com.cocreateenchantments.enchantment.GentleDescentEnchantment;
import com.cocreateenchantments.enchantment.UndyingDropEnchantment;
import com.huziyang520.merlinlib.api.MerlinApi;
import com.huziyang520.merlinlib.event.EnchantmentEventRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CoCreateEnchantments {
	public static final String MOD_ID = CceContent.MOD_ID;
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static boolean callbacksRegistered;

	private CoCreateEnchantments() {
	}

	public static void bootstrap() {
		MerlinApi.lifecycle().onServerStarted(server -> registerEventCallbacks(server.registryAccess()));
	}

	private static void registerEventCallbacks(HolderLookup.Provider registries) {
		if (callbacksRegistered) {
			return;
		}
		callbacksRegistered = true;
		EnchantmentEventRegistrar registrar = EnchantmentEventRegistrar.INSTANCE;
		GentleDescentEnchantment.registerCallbacks(registrar, registries);
		UndyingDropEnchantment.register(registries);
		ChineseEnchantment.registerCallbacks(registrar, registries);
	}

	public static Holder<Enchantment> resolveEnchantment(HolderLookup.Provider registries, String path) {
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, path);
		ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, id);
		return registries.lookupOrThrow(Registries.ENCHANTMENT)
				.get(key)
				.orElseThrow(() -> {
					LOGGER.error("Co-Create Enchantments: 附魔未注册: {}", id);
					return new IllegalStateException("Co-Create Enchantments: 附魔未注册: " + id);
				});
	}
}

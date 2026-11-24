// NeoForge 平台入口：启动 common 侧的挂载并注册配置。

package com.cocreateenchantments.neoforge;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.neoforge.config.CceNeoConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;

@Mod(CoCreateEnchantments.MOD_ID)
public final class CceNeoForge {
	public CceNeoForge(IEventBus modEventBus, ModContainer container) {
		CceNeoConfig.preload();
		CoCreateEnchantments.bootstrap();
		container.registerConfig(CceNeoConfig.type(), CceNeoConfig.SPEC);
		modEventBus.addListener(ModConfigEvent.Loading.class, event -> CceNeoConfig.apply());
		modEventBus.addListener(ModConfigEvent.Reloading.class, event -> CceNeoConfig.apply());
		CceNeoConfig.apply();
	}
}

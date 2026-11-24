// Fabric 客户端：把模组列表里的配置按钮接到 Forge Config API Port 的界面。

package com.cocreateenchantments.fabric.client;

import com.cocreateenchantments.CoCreateEnchantments;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public final class CceFcapConfigClient {
	private CceFcapConfigClient() {
	}

	public static void install() {
		ConfigScreenFactoryRegistry.INSTANCE.register(CoCreateEnchantments.MOD_ID, ConfigurationScreen::new);
	}
}

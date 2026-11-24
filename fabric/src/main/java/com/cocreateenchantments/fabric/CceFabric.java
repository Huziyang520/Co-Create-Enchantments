// Fabric 平台入口。

package com.cocreateenchantments.fabric;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.fabric.config.CceFcapConfig;
import net.fabricmc.api.ModInitializer;

public final class CceFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		CceFcapConfig.install();
		CceFcapConfig.apply();
		CoCreateEnchantments.bootstrap();
	}
}

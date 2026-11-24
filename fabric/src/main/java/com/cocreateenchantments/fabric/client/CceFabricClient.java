// Fabric 客户端入口。

package com.cocreateenchantments.fabric.client;

import net.fabricmc.api.ClientModInitializer;

public final class CceFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CceFcapConfigClient.install();
	}
}

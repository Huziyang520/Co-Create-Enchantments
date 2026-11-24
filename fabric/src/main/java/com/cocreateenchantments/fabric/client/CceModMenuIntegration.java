// Mod Menu 集成：在模组列表上提供配置入口。

package com.cocreateenchantments.fabric.client;

import com.cocreateenchantments.CoCreateEnchantments;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

@Environment(EnvType.CLIENT)
public final class CceModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new ConfigurationScreen(CoCreateEnchantments.MOD_ID, parent);
	}
}

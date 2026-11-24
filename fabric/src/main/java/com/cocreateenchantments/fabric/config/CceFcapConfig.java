// Fabric 侧配置：用 Forge Config API Port 生成与 NeoForge 同一份 SPEC。

package com.cocreateenchantments.fabric.config;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.config.CceConfigOption;
import com.cocreateenchantments.config.CceConfigSchema;
import com.cocreateenchantments.config.CceTunables;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class CceFcapConfig {
	private static final Map<String, ModConfigSpec.ConfigValue<?>> VALUES = new LinkedHashMap<>();
	public static final ModConfigSpec SPEC = build();

	private CceFcapConfig() {
	}

	public static void install() {
		ConfigRegistry.INSTANCE.register(CoCreateEnchantments.MOD_ID, type(), SPEC);
	}

	public static void apply() {
		if (!SPEC.isLoaded()) {
			CceTunables.applyAll(key -> null);
			return;
		}
		CceTunables.applyAll(key -> {
			ModConfigSpec.ConfigValue<?> value = VALUES.get(key);
			return value == null ? null : String.valueOf(value.get());
		});
	}

	private static ModConfig.Type type() {
		for (String candidate : new String[] {"SYNCED", "COMMON", "SERVER"}) {
			try {
				return ModConfig.Type.valueOf(candidate);
			} catch (IllegalArgumentException removedOnThisLoader) {
				// 换一个仍然存在的类型
			}
		}
		return ModConfig.Type.COMMON;
	}

	private static ModConfigSpec build() {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		for (String section : CceConfigSchema.sections()) {
			builder.translation(CoCreateEnchantments.MOD_ID + ".configuration." + section).push(section);
			String open = null;
			for (CceConfigOption option : CceConfigSchema.in(section)) {
				String parent = option.parent();
				if (!parent.equals(open)) {
					if (open != null) {
						builder.pop();
					}
					if (!parent.isEmpty()) {
						builder.translation(CoCreateEnchantments.MOD_ID + ".configuration." + section + "." + parent).push(parent);
					}
					open = parent;
				}
				VALUES.put(option.key(), define(builder, option));
			}
			if (open != null && !open.isEmpty()) {
				builder.pop();
			}
			builder.pop();
		}
		return builder.build();
	}

	private static ModConfigSpec.ConfigValue<?> define(ModConfigSpec.Builder builder, CceConfigOption option) {
		var entry = builder.translation(CoCreateEnchantments.MOD_ID + ".configuration." + option.key());
		return switch (option.kind()) {
			case BOOLEAN -> entry.define(option.name(), option.defaultBool());
			case INTEGER -> entry.comment("范围 " + option.intMin() + " – " + option.intMax())
					.defineInRange(option.name(), option.defaultInt(), option.intMin(), option.intMax());
			case DECIMAL -> entry.comment("范围 " + (float) option.min() + " – " + (float) option.max())
					.defineInRange(option.name(), option.defaultDouble(), option.min(), option.max());
		};
	}
}

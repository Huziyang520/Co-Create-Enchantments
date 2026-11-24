// 三条附魔的配置表；键名与旧版本保持一致。

package com.cocreateenchantments.config;

import java.util.List;

public final class CceConfigSchema {
	private static final List<String> SECTIONS = List.of("armor");
	private static final List<CceConfigOption> OPTIONS = List.of(
			CceConfigOption.integer("armor", "gentle_descent.seconds", 20, 1, 120),
			CceConfigOption.integer("armor", "gentle_descent.refresh_seconds", 1, 1, 10),
			CceConfigOption.bool("armor", "gentle_descent.cancel_on_sneak", true),
			CceConfigOption.integer("armor", "undying_drop.darkness_base_seconds", 20, 0, 120),
			CceConfigOption.integer("armor", "undying_drop.darkness_step", 4, 0, 20),
			CceConfigOption.integer("armor", "chinese.airborne_durability_base", 2, 0, 20),
			CceConfigOption.integer("armor", "chinese.damage_step", 4, 1, 20)
	);

	private CceConfigSchema() {
	}

	public static List<String> sections() {
		return SECTIONS;
	}

	public static List<CceConfigOption> in(String section) {
		return OPTIONS.stream().filter(option -> option.section().equals(section)).toList();
	}
}

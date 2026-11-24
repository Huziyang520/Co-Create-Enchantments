// 配置值缓存：加载器把 SPEC 的读数交进来，业务只读这里的字段。

package com.cocreateenchantments.config;

import java.util.function.Function;

public final class CceTunables {
	private static volatile int gentleDescentSeconds = 20;
	private static volatile int gentleDescentRefreshSeconds = 1;
	private static volatile boolean gentleDescentCancelOnSneak = true;
	private static volatile int undyingDropDarknessBaseSeconds = 20;
	private static volatile int undyingDropDarknessStep = 4;
	private static volatile int chineseAirborneDurabilityBase = 2;
	private static volatile int chineseDamageStep = 4;

	private CceTunables() {
	}

	public static void applyAll(Function<String, String> reader) {
		gentleDescentSeconds = integer(reader, "armor.gentle_descent.seconds", 20);
		gentleDescentRefreshSeconds = integer(reader, "armor.gentle_descent.refresh_seconds", 1);
		gentleDescentCancelOnSneak = bool(reader, "armor.gentle_descent.cancel_on_sneak", true);
		undyingDropDarknessBaseSeconds = integer(reader, "armor.undying_drop.darkness_base_seconds", 20);
		undyingDropDarknessStep = integer(reader, "armor.undying_drop.darkness_step", 4);
		chineseAirborneDurabilityBase = integer(reader, "armor.chinese.airborne_durability_base", 2);
		chineseDamageStep = integer(reader, "armor.chinese.damage_step", 4);
	}

	public static int secondsToTicks(int seconds) {
		return Math.max(0, seconds) * 20;
	}

	public static int gentleDescentSeconds() {
		return gentleDescentSeconds;
	}

	public static int gentleDescentRefreshSeconds() {
		return gentleDescentRefreshSeconds;
	}

	public static boolean gentleDescentCancelOnSneak() {
		return gentleDescentCancelOnSneak;
	}

	public static int undyingDropDarknessTicks(int level) {
		return secondsToTicks(Math.max(0, undyingDropDarknessBaseSeconds - undyingDropDarknessStep * (level - 1)));
	}

	public static int chineseAirborneDurability(float damage) {
		int steps = Math.max(1, (int) (damage / Math.max(1, chineseDamageStep)));
		return chineseAirborneDurabilityBase * steps;
	}

	private static int integer(Function<String, String> reader, String key, int fallback) {
		String raw = reader.apply(key);
		if (raw == null || raw.isBlank()) {
			return fallback;
		}
		try {
			return Integer.parseInt(raw.trim());
		} catch (NumberFormatException invalid) {
			return fallback;
		}
	}

	private static boolean bool(Function<String, String> reader, String key, boolean fallback) {
		String raw = reader.apply(key);
		return raw == null || raw.isBlank() ? fallback : Boolean.parseBoolean(raw.trim());
	}
}

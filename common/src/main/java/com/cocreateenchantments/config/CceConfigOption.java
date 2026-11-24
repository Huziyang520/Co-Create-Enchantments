// 三条附魔的可调参数定义。

package com.cocreateenchantments.config;

public record CceConfigOption(String section, String path, Kind kind, String defaultValue,
		int intMin, int intMax, double min, double max) {
	public enum Kind {
		BOOLEAN, INTEGER, DECIMAL
	}

	public static CceConfigOption bool(String section, String path, boolean defaultValue) {
		return new CceConfigOption(section, path, Kind.BOOLEAN, Boolean.toString(defaultValue), 0, 0, 0.0D, 0.0D);
	}

	public static CceConfigOption integer(String section, String path, int defaultValue, int min, int max) {
		return new CceConfigOption(section, path, Kind.INTEGER, Integer.toString(defaultValue), min, max, min, max);
	}

	public String key() {
		return section + "." + path;
	}

	public String name() {
		return path.substring(path.lastIndexOf('.') + 1);
	}

	public String parent() {
		int cut = path.lastIndexOf('.');
		return cut < 0 ? "" : path.substring(0, cut);
	}

	public boolean defaultBool() {
		return Boolean.parseBoolean(defaultValue);
	}

	public int defaultInt() {
		return (int) Math.round(Double.parseDouble(defaultValue));
	}

	public double defaultDouble() {
		return Double.parseDouble(defaultValue);
	}
}

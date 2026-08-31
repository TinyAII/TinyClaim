package nl.tinyaii.tinyclaim.util;

import org.bukkit.ChatColor;

/**
 * 消息工具：统一颜色转义（全家桶字体约定：中文+ASCII，不用 emoji）。
 */
public final class Messages {

    private Messages() {}

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s == null ? "" : s);
    }

    public static String prefix() { return color("&7[&a领地&7] &r"); }
}

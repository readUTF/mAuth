package com.readutf.mauth.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class SpigotUtils {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> color(String... s) {
        List<String> colored = new ArrayList<>();
        for(String s1 : s) {
            colored.add(color(s1));
        }
        return colored;
    }
    public static List<String> color(List<String> s) {
        List<String> colored = new ArrayList<>();
        for(String s1 : s) {
            colored.add(color(s1));
        }
        return colored;
    }
}

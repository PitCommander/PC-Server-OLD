package net.came20.pitcommander.server.util;

import sun.reflect.Reflection;

/**
 * Created by cameronearle on 5/11/17. lol rekt m89
 */
public class Log {
    private static String getCaller() {
        return Reflection.getCallerClass().getName();
    }

    public static void d(String l) {
        System.out.println("[" + getCaller() + "]");
    }
    public static void i(String l) {}
    public static void w(String l) {}
    public static void e(String l) {}
    public static void x(String l) {}
}

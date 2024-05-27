package com.cjx.utils;

/**
 * 提供线程本地变量的工具类。线程本地变量（ThreadLocal）是一种变量副本，每个线程都拥有自己独立的副本，互不干扰。
 */
public class ThreadLocalUtil {
    // 静态的ThreadLocal实例，用于存储线程本地变量
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    /**
     * 获取当前线程的线程本地变量的值。
     *
     * @return 当前线程本地变量的值，其类型根据调用时的上下文决定。
     */
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    /**
     * 为当前线程的线程本地变量设置值。
     *
     * @param value 要设置的值。
     */
    public static void set(Object value) {
        THREAD_LOCAL.set(value);
    }

    /**
     * 移除当前线程的线程本地变量的值。
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}

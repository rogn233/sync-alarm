package com.boco.test;

/**
 * Created by Administrator on 2018/6/1 0001.
 */
public class TestResources {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("proxool.properties").getPath());
    }
}

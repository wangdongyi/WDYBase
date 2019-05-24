package com.wdy.base;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2019-05-23.
 */
public class Test implements Serializable {
    private int a;
    private int b;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}

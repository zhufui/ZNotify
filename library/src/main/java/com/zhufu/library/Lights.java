package com.zhufu.library;

/**
 * author  : zhufu
 * email   : zhufui@sina.com
 * time    : 2018/04/02
 * desc    : 灯光相关参数
 * version : 1.0
 */
public class Lights {
    //灯光颜色
    private int argb;
    //亮持续时间
    private int onMs;
    //暗的时间
    private int offMs;

    public int getArgb() {
        return argb;
    }

    public void setArgb(int argb) {
        this.argb = argb;
    }

    public int getOnMs() {
        return onMs;
    }

    public void setOnMs(int onMs) {
        this.onMs = onMs;
    }

    public int getOffMs() {
        return offMs;
    }

    public void setOffMs(int offMs) {
        this.offMs = offMs;
    }
}

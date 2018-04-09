package com.zhufu.library;

/**
 * author  : zhufu
 * email   : zhufui@sina.com
 * time    : 2018/04/02
 * desc    :
 * version : 1.0
 */
public class Progress {
    //进度条最大数值
    private int max;
    //当前进度
    private int progress;
    //表示进度是否不确定，true为不确定，false为确定
    private boolean indeterminate;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }
}

package com.wdy.base.module.photopicker.model;

import com.wdy.base.module.photopicker.utils.PhotoUtils;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2019-06-03.
 */
public class PhotoInput implements Serializable {
    private boolean isShowCamera=false;
    private int mode;
    private int max;
    private PhotoUtils.OnPhotoBack onPhotoBack;

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public PhotoUtils.OnPhotoBack getOnPhotoBack() {
        return onPhotoBack;
    }

    public void setOnPhotoBack(PhotoUtils.OnPhotoBack onPhotoBack) {
        this.onPhotoBack = onPhotoBack;
    }
}

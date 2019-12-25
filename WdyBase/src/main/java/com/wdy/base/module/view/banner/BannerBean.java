package com.wdy.base.module.view.banner;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2019-12-25.
 * 广告位-bean
 */
public class BannerBean implements Serializable {
    private int pictureSelected;
    private int pictureUnSelected;
    private boolean isSelected;
    private int position;

    public int getPictureSelected() {
        return pictureSelected;
    }

    public void setPictureSelected(int pictureSelected) {
        this.pictureSelected = pictureSelected;
    }

    public int getPictureUnSelected() {
        return pictureUnSelected;
    }

    public void setPictureUnSelected(int pictureUnSelected) {
        this.pictureUnSelected = pictureUnSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

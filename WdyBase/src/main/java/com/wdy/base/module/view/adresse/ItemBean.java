package com.wdy.base.module.view.adresse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2018/9/6.
 */
public class ItemBean implements Serializable {
    private String id;
    private String name;
    private boolean selected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

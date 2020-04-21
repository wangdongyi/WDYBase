package com.wdy.base.bean;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2020/4/21.
 */
public class RefreshTestItemBean implements Serializable {
    private String textName;
    private String url;

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

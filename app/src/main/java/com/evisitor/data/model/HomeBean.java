package com.evisitor.data.model;

public class HomeBean {
    private int icon;
    private String title;
    private String count;

    public HomeBean(int icon, String title, String count) {
        this.icon = icon;
        this.title = title;
        this.count = count;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

package com.evisitor.data.model;

public class HomeBean {
    private int pos;
    private int icon;
    private String title;
    private String count = "0";

    public HomeBean(int pos, int icon, String title) {
        this.pos = pos;
        this.icon = icon;
        this.title = title;
    }

    public HomeBean(int pos, int icon, String title, String count) {
        this.pos = pos;
        this.icon = icon;
        this.title = title;
        this.count = count;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
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
        return count == null || count.isEmpty() ? "0" : count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

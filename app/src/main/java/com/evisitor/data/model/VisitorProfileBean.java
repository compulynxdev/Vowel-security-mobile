package com.evisitor.data.model;

import java.util.List;

public class VisitorProfileBean {
    public final static int VIEW_TYPE_ITEM = 0;
    public final static int VIEW_TYPE_EDITABLE = 1;
    public final static int VIEW_TYPE_DAYS = 2;

    private String title;
    private String value;
    private int view_type = VIEW_TYPE_ITEM;
    private List<String> dataList;


    private int pos = -1;

    public VisitorProfileBean(String title) {
        this.title = title;
    }

    public VisitorProfileBean(int pos, String title) {
        this.title = title;
        this.pos = pos;
    }

    public VisitorProfileBean(String title, String value, int view_type) {
        this.title = title;
        this.value = value;
        this.view_type = view_type;
    }

    public VisitorProfileBean(String title, int view_type, List<String> dataList) {
        this.title = title;
        this.view_type = view_type;
        this.dataList = dataList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public int getPos() {
        return pos;
    }

}

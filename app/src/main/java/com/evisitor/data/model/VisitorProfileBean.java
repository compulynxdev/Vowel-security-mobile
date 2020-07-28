package com.evisitor.data.model;

public class VisitorProfileBean {
    private String title;
    private String value;
    private boolean isEditable;

    public VisitorProfileBean(String title) {
        this.title = title;
    }

    public VisitorProfileBean(String title, String value, boolean isEditable) {
        this.title = title;
        this.value = value;
        this.isEditable = isEditable;
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

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}

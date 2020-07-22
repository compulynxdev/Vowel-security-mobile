package com.evisitor.data.model;

public class LanguageBean {
    private String localisationTitle = "";
    private String langName;
    private boolean isSelected = false;

    public LanguageBean(String langName) {
        this.langName = langName;
    }

    public LanguageBean(String localisationTitle, String langName, boolean isSelected) {
        this.localisationTitle = localisationTitle;
        this.langName = langName;
        this.isSelected = isSelected;
    }

    public String getLocalisationTitle() {
        return localisationTitle;
    }

    public void setLocalisationTitle(String localisationTitle) {
        this.localisationTitle = localisationTitle;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}

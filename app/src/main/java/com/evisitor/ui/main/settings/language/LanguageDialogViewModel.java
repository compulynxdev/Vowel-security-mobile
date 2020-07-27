package com.evisitor.ui.main.settings.language;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.LanguageBean;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class LanguageDialogViewModel extends BaseViewModel<BaseNavigator> {

    public LanguageDialogViewModel(DataManager dataManager) {
        super(dataManager);
    }

    LanguageBean getSelectedLang() {
        return new LanguageBean(getDataManager().getLanguage());
    }

    List<LanguageBean> getLangList() {
        List<LanguageBean> languageList = new ArrayList<>();
        languageList.add(new LanguageBean(getNavigator().getContext().getString(R.string.english), "English", false));
        languageList.add(new LanguageBean(getNavigator().getContext().getString(R.string.swahili), "Swahili", false));
        return languageList;
    }

}

package com.evisitor.eVisitor.ui.base;

import androidx.lifecycle.ViewModel;

import com.evisitor.eVisitor.data.DataManager;

import java.lang.ref.WeakReference;

public class BaseViewModel <N extends  BaseNavigator> extends ViewModel {
    private DataManager dataManager;
    private WeakReference<N> mNavigator;


    public BaseViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

}

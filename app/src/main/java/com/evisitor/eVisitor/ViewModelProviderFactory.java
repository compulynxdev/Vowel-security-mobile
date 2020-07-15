package com.evisitor.eVisitor;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.eVisitor.data.DataManager;
import com.evisitor.eVisitor.ui.main.MainViewModel;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 * Divergent software labs pvt. ltd
 */
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private static ViewModelProviderFactory instance;
    private DataManager dataManager;

    public ViewModelProviderFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public static synchronized ViewModelProviderFactory getInstance(){
        if (instance == null) {
            instance = new ViewModelProviderFactory(EVisitor.getInstance().getDataManager());
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)){
            //noinspection unchecked
            return (T) new MainViewModel(dataManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

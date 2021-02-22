package com.evisitor.ui.main.home.recurrentvisitor;

import com.evisitor.data.model.RecurrentVisitor;
import com.evisitor.ui.base.BaseNavigator;

interface FilterRecurrentVisitorNavigator extends BaseNavigator {
    void onResponseSuccess(RecurrentVisitor recurrentVisitor);
}

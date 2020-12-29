package com.evisitor.ui.main.home.idverification;

public interface IdVerificationCallback {
    void onScanClick(IdVerificationDialog dialog);

    void onSubmitClick(IdVerificationDialog dialog, String id);
}

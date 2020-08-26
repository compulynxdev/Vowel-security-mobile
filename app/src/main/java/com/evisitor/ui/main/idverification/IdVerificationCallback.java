package com.evisitor.ui.main.idverification;

public interface IdVerificationCallback {
    void onScanClick(IdVerificationDialog dialog);

    void onSubmitClick(IdVerificationDialog dialog, String id);
}

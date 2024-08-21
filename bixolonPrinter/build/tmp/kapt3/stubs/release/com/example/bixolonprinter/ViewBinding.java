package com.example.bixolonprinter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\n"}, d2 = {"Lcom/example/bixolonprinter/ViewBinding;", "", "()V", "bindGone", "", "v", "Landroid/view/View;", "printingStatus", "Lcom/example/bixolonprinter/PrintingStatus;", "bindShowProgress", "bixolonPrinter_release"})
public final class ViewBinding {
    @org.jetbrains.annotations.NotNull
    public static final com.example.bixolonprinter.ViewBinding INSTANCE = null;
    
    private ViewBinding() {
        super();
    }
    
    @androidx.databinding.BindingAdapter(value = {"app:gone"})
    @kotlin.jvm.JvmStatic
    public static final void bindGone(@org.jetbrains.annotations.NotNull
    android.view.View v, @org.jetbrains.annotations.NotNull
    com.example.bixolonprinter.PrintingStatus printingStatus) {
    }
    
    @androidx.databinding.BindingAdapter(value = {"app:showProgress"})
    @kotlin.jvm.JvmStatic
    public static final void bindShowProgress(@org.jetbrains.annotations.NotNull
    android.view.View v, @org.jetbrains.annotations.NotNull
    com.example.bixolonprinter.PrintingStatus printingStatus) {
    }
}
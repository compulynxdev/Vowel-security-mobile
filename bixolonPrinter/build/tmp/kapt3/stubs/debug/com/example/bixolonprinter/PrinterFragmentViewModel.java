package com.example.bixolonprinter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0016J\u0010\u0010\f\u001a\u00020\u00182\b\u0010\f\u001a\u0004\u0018\u00010\u000bJ\u000e\u0010\u001a\u001a\u00020\u00182\u0006\u0010\f\u001a\u00020\u000bJ\u0011\u0010\u001b\u001a\u00020\u0018H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\f\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0012R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001e"}, d2 = {"Lcom/example/bixolonprinter/PrinterFragmentViewModel;", "Landroidx/lifecycle/ViewModel;", "bixolonPrinter", "Lcom/example/bixolonprinter/BixolonPrinter;", "(Lcom/example/bixolonprinter/BixolonPrinter;)V", "_message", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_printerStatus", "Lcom/example/bixolonprinter/PrintingStatus;", "<set-?>", "", "address", "getAddress", "()Ljava/lang/String;", "message", "Lkotlinx/coroutines/flow/StateFlow;", "getMessage", "()Lkotlinx/coroutines/flow/StateFlow;", "printerStatus", "getPrinterStatus", "receipt", "Lcom/example/bixolonprinter/Receipt;", "addReceipt", "", "_receipt", "openPrinter", "print", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "MyViewModelFactory", "bixolonPrinter_debug"})
public final class PrinterFragmentViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.bixolonprinter.BixolonPrinter bixolonPrinter = null;
    private com.example.bixolonprinter.Receipt receipt;
    @org.jetbrains.annotations.Nullable
    private java.lang.String address;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _message = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.bixolonprinter.PrintingStatus> _printerStatus = null;
    
    public PrinterFragmentViewModel(@org.jetbrains.annotations.NotNull
    com.example.bixolonprinter.BixolonPrinter bixolonPrinter) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getAddress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.bixolonprinter.PrintingStatus> getPrinterStatus() {
        return null;
    }
    
    public final void addReceipt(@org.jetbrains.annotations.Nullable
    com.example.bixolonprinter.Receipt _receipt) {
    }
    
    public final void openPrinter(@org.jetbrains.annotations.NotNull
    java.lang.String address) {
    }
    
    private final java.lang.Object print(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    public final void address(@org.jetbrains.annotations.Nullable
    java.lang.String address) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J%\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00060\tH\u0016\u00a2\u0006\u0002\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/bixolonprinter/PrinterFragmentViewModel$MyViewModelFactory;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "bixolonPrinter", "Lcom/example/bixolonprinter/BixolonPrinter;", "(Lcom/example/bixolonprinter/BixolonPrinter;)V", "create", "T", "Landroidx/lifecycle/ViewModel;", "modelClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Landroidx/lifecycle/ViewModel;", "bixolonPrinter_debug"})
    public static final class MyViewModelFactory implements androidx.lifecycle.ViewModelProvider.Factory {
        private final com.example.bixolonprinter.BixolonPrinter bixolonPrinter = null;
        
        public MyViewModelFactory(@org.jetbrains.annotations.NotNull
        com.example.bixolonprinter.BixolonPrinter bixolonPrinter) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public <T extends androidx.lifecycle.ViewModel>T create(@org.jetbrains.annotations.NotNull
        java.lang.Class<T> modelClass) {
            return null;
        }
    }
}
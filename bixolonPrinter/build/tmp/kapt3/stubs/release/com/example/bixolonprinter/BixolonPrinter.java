package com.example.bixolonprinter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u009c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u001f\u001a\u00020 J\u0012\u0010!\u001a\u00020\u001e2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\r\u0010$\u001a\u0004\u0018\u00010 \u00a2\u0006\u0002\u0010%J\u0012\u0010&\u001a\u00020\u001e2\b\u0010\"\u001a\u0004\u0018\u00010\'H\u0016J\u0006\u0010(\u001a\u00020 J\u0012\u0010)\u001a\u00020\u001e2\b\u0010\"\u001a\u0004\u0018\u00010*H\u0016J\b\u0010+\u001a\u00020\u001dH\u0002J\u0010\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020\bH\u0002J\u0010\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u000200H\u0002J\u000e\u00101\u001a\u00020\u00002\u0006\u0010/\u001a\u000200J\u0012\u00102\u001a\u00020\u001e2\b\u0010\"\u001a\u0004\u0018\u000103H\u0016J\u0018\u00104\u001a\u00020 2\b\u00105\u001a\u0004\u0018\u00010\b2\u0006\u00106\u001a\u00020\u001dJ&\u00107\u001a\u00020 2\u0006\u00105\u001a\u00020\b2\u0006\u00106\u001a\u00020\u001d2\u0006\u00108\u001a\u00020\u001d2\u0006\u00109\u001a\u00020\u001dJ\b\u0010:\u001a\u00020 H\u0002JQ\u0010;\u001a\u00020 2\b\b\u0002\u0010<\u001a\u00020\u001d2\b\b\u0002\u0010=\u001a\u00020\b2\b\b\u0002\u0010>\u001a\u00020 2\u0006\u0010?\u001a\u00020\b2\u0018\u0010@\u001a\u0014\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0\u001bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010AJ(\u0010B\u001a\u00020 2\u0006\u0010<\u001a\u00020\u001d2\u0006\u0010=\u001a\u00020\b2\u0006\u0010C\u001a\u00020\u001d2\u0006\u0010D\u001a\u00020\bH\u0002J\u0012\u0010E\u001a\u00020\u001e2\b\u0010\"\u001a\u0004\u0018\u00010FH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u001a\u001a\u0016\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006G"}, d2 = {"Lcom/example/bixolonprinter/BixolonPrinter;", "Ljpos/events/ErrorListener;", "Ljpos/events/StatusUpdateListener;", "Ljpos/events/DataListener;", "Ljpos/events/OutputCompleteListener;", "Ljpos/events/DirectIOListener;", "()V", "TAG", "", "bixolonPrinter", "bxlConfigLoader", "Lcom/bxl/config/editor/BXLConfigLoader;", "cashDrawer", "Ljpos/CashDrawer;", "localSmartCardRW", "Ljpos/LocalSmartCardRW;", "lock", "", "msr", "Ljpos/MSR;", "posPrinter", "Ljpos/POSPrinter;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "smartCardRW", "Ljpos/SmartCardRW;", "statusMessage", "Lkotlin/Function2;", "Lcom/example/bixolonprinter/PrintingStatus;", "", "", "beginTransactionPrint", "", "dataOccurred", "p0", "Ljpos/events/DataEvent;", "deviceEnabled", "()Ljava/lang/Boolean;", "directIOOccurred", "Ljpos/events/DirectIOEvent;", "endTransactionPrint", "errorOccurred", "Ljpos/events/ErrorEvent;", "getPortBluetooth", "getProductName", "name", "initBxPrinter", "context", "Landroid/content/Context;", "instance", "outputCompleteOccurred", "Ljpos/events/OutputCompleteEvent;", "printBarcode", "data", "alignment", "printText", "attribute", "textSize", "printerClose", "printerOpen", "portType", "logicalName", "isAsyncMode", "btAddress", "callBack", "(ILjava/lang/String;ZLjava/lang/String;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTargetDevice", "deviceCategory", "address", "statusUpdateOccurred", "Ljpos/events/StatusUpdateEvent;", "bixolonPrinter_release"})
public final class BixolonPrinter implements jpos.events.ErrorListener, jpos.events.StatusUpdateListener, jpos.events.DataListener, jpos.events.OutputCompleteListener, jpos.events.DirectIOListener {
    @org.jetbrains.annotations.NotNull
    public static final com.example.bixolonprinter.BixolonPrinter INSTANCE = null;
    private static jpos.LocalSmartCardRW localSmartCardRW;
    private static com.bxl.config.editor.BXLConfigLoader bxlConfigLoader;
    private static jpos.POSPrinter posPrinter;
    private static jpos.MSR msr;
    private static jpos.SmartCardRW smartCardRW;
    private static jpos.CashDrawer cashDrawer;
    private static final kotlinx.coroutines.CoroutineScope scope = null;
    private static kotlin.jvm.functions.Function2<? super com.example.bixolonprinter.PrintingStatus, ? super java.lang.Integer, kotlin.Unit> statusMessage;
    private static com.example.bixolonprinter.BixolonPrinter bixolonPrinter;
    private static java.lang.String TAG;
    private static final java.lang.Object lock = null;
    
    private BixolonPrinter() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.bixolonprinter.BixolonPrinter instance(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    private final void initBxPrinter(android.content.Context context) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object printerOpen(int portType, @org.jetbrains.annotations.NotNull
    java.lang.String logicalName, boolean isAsyncMode, @org.jetbrains.annotations.NotNull
    java.lang.String btAddress, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.example.bixolonprinter.PrintingStatus, ? super java.lang.Integer, kotlin.Unit> callBack, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
    }
    
    private final boolean setTargetDevice(int portType, java.lang.String logicalName, int deviceCategory, java.lang.String address) {
        return false;
    }
    
    private final int getPortBluetooth() {
        return 0;
    }
    
    private final java.lang.String getProductName(java.lang.String name) {
        return null;
    }
    
    public final boolean printText(@org.jetbrains.annotations.NotNull
    java.lang.String data, int alignment, int attribute, int textSize) {
        return false;
    }
    
    public final boolean beginTransactionPrint() {
        return false;
    }
    
    public final boolean endTransactionPrint() {
        return false;
    }
    
    private final boolean printerClose() {
        return false;
    }
    
    @java.lang.Override
    public void errorOccurred(@org.jetbrains.annotations.Nullable
    jpos.events.ErrorEvent p0) {
    }
    
    @java.lang.Override
    public void statusUpdateOccurred(@org.jetbrains.annotations.Nullable
    jpos.events.StatusUpdateEvent p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Boolean deviceEnabled() {
        return null;
    }
    
    public final boolean printBarcode(@org.jetbrains.annotations.Nullable
    java.lang.String data, int alignment) {
        return false;
    }
    
    @java.lang.Override
    public void dataOccurred(@org.jetbrains.annotations.Nullable
    jpos.events.DataEvent p0) {
    }
    
    @java.lang.Override
    public void outputCompleteOccurred(@org.jetbrains.annotations.Nullable
    jpos.events.OutputCompleteEvent p0) {
    }
    
    @java.lang.Override
    public void directIOOccurred(@org.jetbrains.annotations.Nullable
    jpos.events.DirectIOEvent p0) {
    }
}
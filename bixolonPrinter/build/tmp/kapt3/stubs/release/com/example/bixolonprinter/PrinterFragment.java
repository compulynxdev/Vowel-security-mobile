package com.example.bixolonprinter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 <2\u00020\u0001:\u0001<B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001a\u001a\u00020\u0017H\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0016H\u0002J\b\u0010\u001d\u001a\u00020\u0017H\u0002J\u0010\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020 \u0018\u00010\u001fH\u0002J\b\u0010!\u001a\u00020\u001bH\u0002J\u0012\u0010\"\u001a\u00020\u00172\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J&\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010\'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010+\u001a\u00020\u0017H\u0016J-\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00132\u000e\u0010.\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00160/2\u0006\u00100\u001a\u000201H\u0016\u00a2\u0006\u0002\u00102J\u001a\u00103\u001a\u00020\u00172\u0006\u00104\u001a\u00020&2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u00105\u001a\u00020\u0017H\u0002J\b\u00106\u001a\u00020\u0017H\u0002J\u0010\u00107\u001a\u00020\u00172\u0006\u00108\u001a\u00020\u001bH\u0002J\u0014\u00109\u001a\u00020\u001b*\u00020:2\u0006\u0010;\u001a\u00020\u0016H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\f\u0012\n \r*\u0004\u0018\u00010\f0\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082D\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0014\u001a\u0010\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006="}, d2 = {"Lcom/example/bixolonprinter/PrinterFragment;", "Lcom/google/android/material/bottomsheet/BottomSheetDialogFragment;", "()V", "_binding", "Lcom/example/bixolonprinter/databinding/FragmentPrinterListDialogBinding;", "binding", "getBinding", "()Lcom/example/bixolonprinter/databinding/FragmentPrinterListDialogBinding;", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "enableBtLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "mContext", "Landroid/content/Context;", "printingSts", "Lcom/example/bixolonprinter/PrintingStatusCallBack;", "requestBtScanPermission", "", "selectedDevice", "Lkotlin/Function1;", "", "", "viewModel", "Lcom/example/bixolonprinter/PrinterFragmentViewModel;", "checkBtPermission", "", "permission", "enableBluetooth", "getPairedDevices", "", "Landroid/bluetooth/BluetoothDevice;", "isBluetoothSupported", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onViewCreated", "view", "openPrinter", "showPairedDevices", "showRecyclerView", "boolean", "missingSystemFeature", "Landroid/content/pm/PackageManager;", "name", "Companion", "bixolonPrinter_release"})
public final class PrinterFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    private com.example.bixolonprinter.PrintingStatusCallBack printingSts;
    private final int requestBtScanPermission = 2;
    private android.content.Context mContext;
    private com.example.bixolonprinter.databinding.FragmentPrinterListDialogBinding _binding;
    private android.bluetooth.BluetoothAdapter bluetoothAdapter;
    private com.example.bixolonprinter.PrinterFragmentViewModel viewModel;
    private kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> selectedDevice;
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> enableBtLauncher;
    @org.jetbrains.annotations.NotNull
    public static final com.example.bixolonprinter.PrinterFragment.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String TAG = "PrinterFragment";
    public static final int requestBtConnectPermission = 1;
    
    public PrinterFragment() {
        super();
    }
    
    private final com.example.bixolonprinter.databinding.FragmentPrinterListDialogBinding getBinding() {
        return null;
    }
    
    private final boolean missingSystemFeature(android.content.pm.PackageManager $this$missingSystemFeature, java.lang.String name) {
        return false;
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final boolean isBluetoothSupported() {
        return false;
    }
    
    private final boolean checkBtPermission(java.lang.String permission) {
        return false;
    }
    
    private final void checkBtPermission() {
    }
    
    @java.lang.Override
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull
    int[] grantResults) {
    }
    
    private final void enableBluetooth() {
    }
    
    private final java.util.Set<android.bluetooth.BluetoothDevice> getPairedDevices() {
        return null;
    }
    
    private final void openPrinter() {
    }
    
    private final void showPairedDevices() {
    }
    
    private final void showRecyclerView(boolean p0_32355860) {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J<\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00120\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/example/bixolonprinter/PrinterFragment$Companion;", "", "()V", "TAG", "", "requestBtConnectPermission", "", "newInstance", "Lcom/example/bixolonprinter/PrinterFragment;", "printingStatus", "Lcom/example/bixolonprinter/PrintingStatusCallBack;", "context", "Landroid/content/Context;", "deviceAddress", "receipt", "Lcom/example/bixolonprinter/Receipt;", "selectedDevice", "Lkotlin/Function1;", "", "bixolonPrinter_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.bixolonprinter.PrinterFragment newInstance(@org.jetbrains.annotations.Nullable
        com.example.bixolonprinter.PrintingStatusCallBack printingStatus, @org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        java.lang.String deviceAddress, @org.jetbrains.annotations.NotNull
        com.example.bixolonprinter.Receipt receipt, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> selectedDevice) {
            return null;
        }
    }
}
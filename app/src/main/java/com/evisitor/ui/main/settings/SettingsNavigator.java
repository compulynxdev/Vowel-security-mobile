package com.evisitor.ui.main.settings;

import android.bluetooth.BluetoothDevice;

import com.evisitor.ui.base.BaseNavigator;

import java.util.Set;

public interface SettingsNavigator extends BaseNavigator {
    void showBluetoothDialog(Set<BluetoothDevice> pairedDevices);
}

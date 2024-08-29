package com.evisitor.ui.main.settings;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bixolon.commonlib.BXLCommonConst;
import com.bixolon.commonlib.log.LogService;
import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.base.DialogManager;
import com.evisitor.ui.main.MainActivity;
import com.evisitor.ui.main.activity.checkin.CheckInFragment;
import com.evisitor.util.AppUtils;

import java.util.Set;

public class SettingsViewModel extends BaseViewModel<SettingsNavigator> {
    private final MutableLiveData<String> version = new MutableLiveData<>();
    private final MutableLiveData<String> printerStatus = new MutableLiveData<>();
    private BixolonLabelPrinter mBixolonLabelPrinter = null;


    public SettingsViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getVersion() {
        version.setValue(AppUtils.getAppVersionName(EVisitor.getInstance().getApplicationContext()));
        return version;
    }

    public void setSelectedDevice(String address) {
        getDataManager().setPrinterAddress(address);

    }

    public void pairBixolonPrinter(Context context) {
        if (mBixolonLabelPrinter == null) {
            mBixolonLabelPrinter = new BixolonLabelPrinter(context, mHandler, Looper.getMainLooper());
            mBixolonLabelPrinter.findBluetoothPrinters();
        }
    }


    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BixolonLabelPrinter.STATE_CONNECTED:

                            break;

                        case BixolonLabelPrinter.STATE_CONNECTING:

                            break;

                        case BixolonLabelPrinter.STATE_NONE:

                            break;
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_READ:

                    break;

                case BixolonLabelPrinter.MESSAGE_DEVICE_NAME:

                    break;

                case BixolonLabelPrinter.MESSAGE_TOAST:

                    break;

                case BixolonLabelPrinter.MESSAGE_LOG:

                    break;

                case BixolonLabelPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if (msg.obj ==null){
                        getNavigator().showAlert(R.string.alert,R.string.no_paired_devices);
                    }else {
                        getNavigator().showBluetoothDialog((Set<BluetoothDevice>)  msg.obj);
                    }
                    break;

            }
        }
    };
}

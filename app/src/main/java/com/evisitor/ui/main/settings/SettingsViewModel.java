package com.evisitor.ui.main.settings;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.base.DialogManager;
import com.evisitor.ui.main.activity.checkin.CheckInFragment;
import com.evisitor.util.AppUtils;

import java.util.Set;

public class SettingsViewModel extends BaseViewModel<SettingsNavigator> {
    private final MutableLiveData<String> version = new MutableLiveData<>();
    private final MutableLiveData<String> printerStatus = new MutableLiveData<>();
    private static BixolonLabelPrinter bixolonLabelPrinter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BixolonLabelPrinter.STATE_CONNECTED:
                            printerStatus.postValue(getNavigator().getContext().getString(R.string.title_connected_to));
                            break;

                        case BixolonLabelPrinter.STATE_CONNECTING:
                            //setStatus(getResources().getString(R.string.title_connecting));
                            printerStatus.postValue(getNavigator().getContext().getString(R.string.title_connecting));
                            break;

                        case BixolonLabelPrinter.STATE_NONE:
                            //Log.e("NONE", msg.toString());
                            // setStatus(getResources().getString(R.string.title_not_connected));
                            printerStatus.postValue(getNavigator().getContext().getString(R.string.title_not_connected));
                            //invalidateOptionsMenu();
                            break;
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_READ:
                    _dispatchMessage(msg);
                    break;

                case BixolonLabelPrinter.MESSAGE_DEVICE_NAME:
                    //connectedDeviceName = msg.getData().getString(BixolonLabelPrinter.DEVICE_NAME);
                    printerStatus.postValue(msg.getData().getString(BixolonLabelPrinter.DEVICE_NAME));
                    break;

                case BixolonLabelPrinter.MESSAGE_TOAST:
                    printerStatus.postValue(msg.getData().getString(BixolonLabelPrinter.TOAST));
                    break;

                case BixolonLabelPrinter.MESSAGE_LOG:
                    printerStatus.postValue(msg.getData().getString(BixolonLabelPrinter.LOG));
                    break;

                case BixolonLabelPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if (msg.obj == null) {
                        printerStatus.postValue("No paired device");
                    } else {
                        getNavigator().onPairedDevices((Set<BluetoothDevice>) msg.obj);
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_USB_DEVICE_SET:
                    if (msg.obj == null) {
                        printerStatus.postValue("No connected device");
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_OUTPUT_COMPLETE:
                    printerStatus.postValue("Transaction Print complete");
                    break;

            }
        }
    };

    public SettingsViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getVersion() {
        version.setValue(AppUtils.getAppVersionName(EVisitor.getInstance().getApplicationContext()));
        return version;
    }

    public void initBixolonPrinter(Context context) {

        if (bixolonLabelPrinter == null) {
            bixolonLabelPrinter = new BixolonLabelPrinter(context, mHandler, Looper.getMainLooper());
            bixolonLabelPrinter.findBluetoothPrinters();
        }
    }

    private void _dispatchMessage(Message msg) {
        switch (msg.arg1) {
            case BixolonLabelPrinter.PROCESS_GET_STATUS:
                byte[] report = (byte[]) msg.obj;
                StringBuffer buffer = new StringBuffer();
                if ((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_PAPER_EMPTY) == BixolonLabelPrinter.STATUS_1ST_BYTE_PAPER_EMPTY) {
                    buffer.append("Paper Empty.\n");
                }
                if ((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_COVER_OPEN) == BixolonLabelPrinter.STATUS_1ST_BYTE_COVER_OPEN) {
                    buffer.append("Cover open.\n");
                }
                if ((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_CUTTER_JAMMED) == BixolonLabelPrinter.STATUS_1ST_BYTE_CUTTER_JAMMED) {
                    buffer.append("Cutter jammed.\n");
                }
                if ((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_TPH_OVERHEAT) == BixolonLabelPrinter.STATUS_1ST_BYTE_TPH_OVERHEAT) {
                    buffer.append("TPH(thermal head) overheat.\n");
                }
                if ((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_AUTO_SENSING_FAILURE) == BixolonLabelPrinter.STATUS_1ST_BYTE_AUTO_SENSING_FAILURE) {
                    buffer.append("Gap detection error. (Auto-sensing failure)\n");
                }
                if ((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_RIBBON_END_ERROR) == BixolonLabelPrinter.STATUS_1ST_BYTE_RIBBON_END_ERROR) {
                    buffer.append("Ribbon end error.\n");
                }

                if (report.length == 2) {
                    if ((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_BUILDING_IN_IMAGE_BUFFER) == BixolonLabelPrinter.STATUS_2ND_BYTE_BUILDING_IN_IMAGE_BUFFER) {
                        buffer.append("On building label to be printed in image buffer.\n");
                    }
                    if ((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_PRINTING_IN_IMAGE_BUFFER) == BixolonLabelPrinter.STATUS_2ND_BYTE_PRINTING_IN_IMAGE_BUFFER) {
                        buffer.append("On printing label in image buffer.\n");
                    }
                    if ((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_PAUSED_IN_PEELER_UNIT) == BixolonLabelPrinter.STATUS_2ND_BYTE_PAUSED_IN_PEELER_UNIT) {
                        buffer.append("Issued label is paused in peeler unit.\n");
                    }
                }
                if (buffer.length() == 0) {
                    buffer.append("No error");
                }
                printerStatus.postValue(buffer.toString());
                break;
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_MODEL_NAME:
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_FIRMWARE_VERSION:
            case BixolonLabelPrinter.PROCESS_EXECUTE_DIRECT_IO:
                try {
                    printerStatus.postValue((String) msg.obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    public void setSelectedDevice(String address){
        getDataManager().setPrinterAddress(address);

    }
}

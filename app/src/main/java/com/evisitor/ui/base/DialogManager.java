package com.evisitor.ui.base;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.evisitor.ui.main.activity.checkin.CheckInFragment;
import com.evisitor.ui.main.commercial.visitor.expected.ExpectedCommercialGuestFragment;
import com.evisitor.ui.main.residential.sp.ExpectedSPFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

public class DialogManager {
	
	public static void showBluetoothDialog(Context context, final Set<BluetoothDevice> pairedDevices, boolean isSp) {
		final String[] items = new String[pairedDevices.size()];
		int index = 0;
		for (BluetoothDevice device : pairedDevices) {
			items[index++] = device.getName() + "\n" + device.getAddress();
		}

		new AlertDialog.Builder(context).setTitle("Paired Bluetooth printers")
				.setItems(items, new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
						String strSelectList = items[which];
						String temp;
						int indexSpace = 0;
						for(int i = 5; i<strSelectList.length(); i++){
							temp = strSelectList.substring(i-5, i);
							if((temp.equals("00:10"))||(temp.equals("74:F0"))||(temp.equals("00:15")) || (temp.equals("DD:C5"))){
								indexSpace = i;
								i = 100;
							}
						}
						String strDeviceInfo = null;
						strDeviceInfo = strSelectList.substring(indexSpace-5, strSelectList.length());
						CheckInFragment.bixolonLabelPrinter.connect(strDeviceInfo);
					}
				}).show();
	}

	


}

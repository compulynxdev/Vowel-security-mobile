package com.evisitor.ui.main.settings;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.FragmentSettingsBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.settings.content.ContentActivity;
import com.evisitor.ui.main.settings.info.DeviceInfoDialog;
import com.evisitor.ui.main.settings.language.LanguageDialog;
import com.evisitor.ui.main.settings.propertyinfo.PropertyInfoActivity;
import com.evisitor.util.AppConstants;

import java.util.Set;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding, SettingsViewModel> implements SettingsNavigator, View.OnClickListener {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    public SettingsViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(SettingsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_settings);
        getViewDataBinding().tvLang.setText(mViewModel.getDataManager().getLanguage());

        setOnClickListener(getViewDataBinding().printerConstraint, getViewDataBinding().infoConstraint, getViewDataBinding().premiseInfoConstraint, getViewDataBinding().languageConstraint, getViewDataBinding().aboutusConstraint
                , getViewDataBinding().privacyConstraint, getViewDataBinding().logoutConstraint);
    }

    private void setOnClickListener(View... views) {
        for (View view : views)
            view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.info_constraint) {
            DeviceInfoDialog.newInstance().show(getChildFragmentManager());
        } else if (id == R.id.premise_info_constraint) {
            startActivity(PropertyInfoActivity.newIntent(getBaseActivity()));
        } else if (id == R.id.language_constraint) {
            LanguageDialog.newInstance(language -> {
                mViewModel.getDataManager().setLanguage(language.getLangName());
                getViewDataBinding().tvLang.setText(language.getLocalisationTitle());
            }).show(getChildFragmentManager());
        } else if (id == R.id.aboutus_constraint) {
            Intent intent = ContentActivity.newIntent(getBaseActivity());
            intent.putExtra("From", AppConstants.ACTIVITY_ABOUT_US);
            startActivity(intent);
        } else if (id == R.id.privacy_constraint) {
            Intent intent = ContentActivity.newIntent(getBaseActivity());
            intent.putExtra("From", AppConstants.ACTIVITY_PRIVACY);
            startActivity(intent);
        } else if (id == R.id.printer_constraint) {
            checkBluetoothPermission();
        } else if (id == R.id.logout_constraint) {
            showAlert(R.string.logout, R.string.logout_msg)
                    .setNegativeBtnShow(true)
                    .setPositiveBtnLabel(getString(R.string.yes))
                    .setNegativeBtnLabel(getString(R.string.no))
                    .setOnPositiveClickListener(dialog -> {
                        dialog.dismiss();
                        openActivityOnTokenExpire();
                    }).setOnNegativeClickListener(DialogFragment::dismiss);
        }

    }

    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.BLUETOOTH_CONNECT) ==
                PackageManager.PERMISSION_GRANTED) {

            BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
            if (bluetoothAdapter == null) {
                com.evisitor.ui.dialog.AlertDialog.newInstance()
                        .setNegativeBtnShow(true)
                        .setCloseBtnShow(false)
                        .setTitle(getString(R.string.bluetooth_not_supported))
                        .setMsg(getString(R.string.bluetooth_not_supported_msg))
                        .setPositiveBtnLabel(getString(R.string.ok))
                        .setOnPositiveClickListener(DialogFragment::dismiss).show(getChildFragmentManager());
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBluetoothLauncher.launch(enableBtIntent);

                } else {
                    getViewModel().initBixolonPrinter(getContext());
                }
            }

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.BLUETOOTH_CONNECT)) {
            com.evisitor.ui.dialog.AlertDialog.newInstance()
                    .setNegativeBtnShow(true)
                    .setCloseBtnShow(false)
                    .setTitle(getString(R.string.bluetooth))
                    .setMsg(getString(R.string.bluetooth_desc))
                    .setPositiveBtnLabel(getString(R.string.enable))
                    .setNegativeBtnLabel(getString(R.string.no_thanks))
                    .setOnNegativeClickListener(DialogFragment::dismiss)
                    .setOnPositiveClickListener(dialog12 -> {
                        dialog12.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            requestPermissionLauncher.launch(
                                    Manifest.permission.BLUETOOTH_CONNECT);
                        }
                    }).show(getChildFragmentManager());


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLauncher.launch(
                        Manifest.permission.BLUETOOTH_CONNECT);
            }
        }
    }

    private ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    getViewModel().initBixolonPrinter(getContext());
                } else {
                    // User declined to enable Bluetooth
                    com.evisitor.ui.dialog.AlertDialog.newInstance()
                            .setNegativeBtnShow(false)
                            .setCloseBtnShow(false)
                            .setPositiveBtnLabel(getString(R.string.ok))
                            .setTitle(getString(R.string.bluetooth))
                            .setMsg(getString(R.string.bluetooth_denied_permission)).setOnPositiveClickListener(dialog -> {
                                dialog.dismiss();
                            }).show(getChildFragmentManager());
                }
            });


    private BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter == null) {
                        com.evisitor.ui.dialog.AlertDialog.newInstance()
                                .setNegativeBtnShow(false)
                                .setCloseBtnShow(false)
                                .setTitle(getString(R.string.bluetooth_not_supported))
                                .setMsg(getString(R.string.bluetooth_not_supported_msg))
                                .setPositiveBtnLabel(getString(R.string.ok))
                                .setOnPositiveClickListener(DialogFragment::dismiss).show(getChildFragmentManager());
                    } else {
                        if (!bluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            enableBluetoothLauncher.launch(enableBtIntent);

                        } else {
                            getViewModel().initBixolonPrinter(getContext());
                        }
                    }

                } else {
                    com.evisitor.ui.dialog.AlertDialog.newInstance()
                            .setNegativeBtnShow(false)
                            .setCloseBtnShow(false)
                            .setTitle(getString(R.string.bluetooth))
                            .setMsg(getString(R.string.printing_denied_permission))
                            .setPositiveBtnLabel(getString(R.string.ok))
                            .setOnPositiveClickListener(DialogFragment::dismiss).show(getChildFragmentManager());
                }
            });


    @Override
    public void onPairedDevices(Set<BluetoothDevice> devices) {
        showBluetoothDialog(devices);
    }

    void showBluetoothDialog(Set<BluetoothDevice> pairedDevices) {
        final String[] items = new String[pairedDevices.size()];
        int index = 0;
        for (BluetoothDevice device : pairedDevices) {
            items[index++] = device.getName() + "\n" + device.getAddress();
        }


        new AlertDialog.Builder(getContext()).setTitle("Paired Bluetooth printers")
                .setItems(items, (dialog, which) -> {

                    String strSelectList = items[which];
                    String temp;
                    int indexSpace = 0;
                    for (int i = 5; i < strSelectList.length(); i++) {
                        temp = strSelectList.substring(i - 5, i);
                        if ((temp.equals("00:10")) || (temp.equals("74:F0")) || (temp.equals("00:15")) || (temp.equals("DD:C5")) || (temp.equals("40:19"))) {
                            indexSpace = i;
                            i = 100;
                        }
                    }

                    final String strDeviceInfo = strSelectList.substring(indexSpace - 5, strSelectList.length());

                    getViewModel().setSelectedDevice(strDeviceInfo);
                }).show();
    }
}

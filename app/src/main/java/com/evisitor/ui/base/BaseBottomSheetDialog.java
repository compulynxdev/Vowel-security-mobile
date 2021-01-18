package com.evisitor.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evisitor.R;
import com.evisitor.ui.dialog.AlertDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import okhttp3.ResponseBody;


/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public abstract class BaseBottomSheetDialog<T extends ViewDataBinding, V extends BaseViewModel> extends BottomSheetDialogFragment implements BaseNavigator, View.OnFocusChangeListener {

    protected V mViewModel;
    private BaseActivity mActivity;
    private T mViewDataBinding;
    private int defaultStyle = R.style.CustomBottomSheetDialogTheme;
    private boolean draggable = true;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    public void setStyle(int style) {
        this.defaultStyle = style;
    }

    protected void setDraggableFalse() {
        this.draggable = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity mActivity = (BaseActivity) context;
            this.mActivity = mActivity;
            mActivity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, defaultStyle);
        mViewModel = getViewModel();
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewDataBinding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.setOnShowListener(dialog -> {
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            assert bottomSheet != null;
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setDraggable(draggable);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        show(transaction, tag);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard();
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        return mActivity != null && mActivity.hasPermission(permission);
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    @Override
    public boolean isNetworkConnected(boolean isShowMsg) {
        return mActivity != null && mActivity.isNetworkConnected(isShowMsg);
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }

    @Override
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (mActivity != null) {
            mActivity.requestPermissionsSafely(permissions, requestCode);
        }
    }

    @Override
    public void hideLoading() {
        if (mActivity != null) {
            mActivity.hideLoading();
        }
    }

    @Override
    public void showLoading() {
        if (mActivity != null) {
            mActivity.showLoading();
        }
    }


    private String getResString(@StringRes int value) {
        if (mActivity != null)
            return getString(value);
        else return "";
    }

    @Override
    public AlertDialog showAlert(@StringRes int title, @StringRes int msg) {
        return showAlert(getResString(title), getResString(msg));
    }

    @Override
    public AlertDialog showAlert(@StringRes int title, String msg) {
        return showAlert(getResString(title), msg);
    }

    @Override
    public AlertDialog showAlert(String title, @StringRes int msg) {
        return showAlert(title, getResString(msg));
    }

    @Override
    public AlertDialog showAlert(String title, String msg) {
        if (isAdded()) {
            AlertDialog alertDialog = AlertDialog.newInstance().setMsg(msg)
                    .setTitle(title)
                    .setNegativeBtnShow(false)
                    .setOnPositiveClickListener(DialogFragment::dismiss);
            alertDialog.show(getChildFragmentManager());
            return alertDialog;
        } else return null;
    }

    @Override
    public void handleApiFailure(@NonNull Throwable t) {
        showAlert(getResString(R.string.alert), t.getMessage());
    }

    @Override
    public void handleApiError(ResponseBody response) {
        try {
            String data = response.string();
            if (data.isEmpty()) {
                showAlert(R.string.alert, R.string.alert_error);
            } else {
                JSONObject object = new JSONObject(data);
                if (object.has("detail")) {
                    showAlert(R.string.alert, object.getString("detail"));
                } else if (object.has("message")) {
                    showAlert(R.string.alert, object.getString("message"));
                } else if (object.has("respMessage"))
                    showAlert(R.string.alert, object.getString("respMessage"));
                else {
                    showAlert(R.string.alert, R.string.alert_error);
                }
            }
        } catch (Exception e) {
            showAlert(getResString(R.string.alert), R.string.alert_error);
        }
    }

    @Override
    public void showToast(String msg) {
        if (mActivity != null) mActivity.showToast(msg);
    }

    @Override
    public void showToast(@StringRes int msg) {
        if (mActivity != null) mActivity.showToast(msg);
    }
}

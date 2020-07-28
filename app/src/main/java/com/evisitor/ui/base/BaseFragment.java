package com.evisitor.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.evisitor.ui.dialog.AlertDialog;

import okhttp3.ResponseBody;

/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public abstract class BaseFragment <T extends ViewDataBinding, V extends BaseViewModel> extends Fragment implements BaseNavigator {

    private BaseActivity mActivity;
    private View mRootView;
    private T mViewDataBinding;
    private V mViewModel;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
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

    protected BaseActivity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @Nullable
    @Override
    public Context getContext() {
        return mActivity.getContext();
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

    @Override
    public AlertDialog showAlert(@StringRes int title, @StringRes int msg) {
        return showAlert(getString(title), getString(msg));
    }

    @Override
    public AlertDialog showAlert(@StringRes int title, String msg) {
        return showAlert(getString(title), msg);
    }

    @Override
    public AlertDialog showAlert(String title, @StringRes int msg) {
        return showAlert(title, getString(msg));
    }

    @Override
    public AlertDialog showAlert(String title, String msg) {
        AlertDialog alertDialog = AlertDialog.newInstance().setMsg(msg)
                .setTitle(title)
                .setNegativeBtnShow(false)
                .setOnPositiveClickListener(DialogFragment::dismiss);
        alertDialog.show(getChildFragmentManager());
        return alertDialog;
    }


    @Override
    public void showToast(String msg) {
        if (mActivity != null) mActivity.showToast(msg);
    }

    @Override
    public void showToast(@StringRes int msg) {
        if (mActivity != null) mActivity.showToast(msg);
    }

    @Override
    public void handleApiFailure(@NonNull Throwable t) {
        if (mActivity != null)
            mActivity.handleApiFailure(t);
    }

    @Override
    public void handleApiError(ResponseBody response) {
        if (mActivity != null)
            mActivity.handleApiError(response);
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

}

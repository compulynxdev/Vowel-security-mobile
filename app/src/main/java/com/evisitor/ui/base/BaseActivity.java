package com.evisitor.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.util.CommonUtils;
import com.evisitor.util.NetworkUtils;

/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public abstract class BaseActivity <T extends ViewDataBinding,V extends BaseViewModel> extends AppCompatActivity
implements BaseFragment.Callback, BaseNavigator{

    private static final String TAG = "BaseActivity";
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private ProgressDialog mProgressDialog;
    private T mViewDataBinding;
    private V mViewModel;
    private Toast toast;

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
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void openActivityOnTokenExpire() {
        getViewModel().getDataManager().logout(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
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
        AlertDialog alertDialog = AlertDialog.newInstance()
                .setMsg(msg)
                .setTitle(title)
                .setCancelGone()
                .setOnOKClickListener(DialogFragment::dismiss);
        alertDialog.show(getSupportFragmentManager());
        return alertDialog;
    }

    public void replaceFragment(@NonNull Fragment fragmentHolder, int layoutId) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            String fragmentName = fragmentHolder.getClass().getName();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(layoutId, fragmentHolder, fragmentName);
            //fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
            hideKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragment(@NonNull Fragment fragment, int layoutId, boolean addToBackStack) {
        try {
            String fragmentName = fragment.getClass().getName();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            //todo
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setEnterTransition(null);
            }
            fragmentTransaction.add(layoutId, fragment, fragmentName);
            if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();

            hideKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String msg) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void showToast(@StringRes int msg) {
        showToast(getString(msg));
    }


}

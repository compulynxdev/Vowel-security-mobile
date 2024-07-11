package com.evisitor.ui.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evisitor.R;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.image.BitmapImageViewActivity;
import com.evisitor.ui.image.ImageViewActivity;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CommonUtils;
import com.evisitor.util.NetworkUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import okhttp3.ResponseBody;

/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity
        implements BaseFragment.Callback, BaseNavigator {

    protected V mViewModel;
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private ProgressDialog mProgressDialog;
    private T mViewDataBinding;
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
    public boolean isNetworkConnected(boolean isShowMsg) {
        boolean status = NetworkUtils.isNetworkConnected(getApplicationContext());

        if (isShowMsg && !status) showAlert(R.string.app_name, R.string.alert_internet);
        return status;
    }

    @Override
    public void openActivityOnTokenExpire() {
        mViewModel.getDataManager().logout(this);
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
                .setNegativeBtnShow(false)
                .setOnPositiveClickListener(DialogFragment::dismiss);
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
            AppLogger.w("replaceFragment", e.toString());
        }
    }

    public void addFragment(@NonNull Fragment fragment, int layoutId, boolean addToBackStack) {
        try {
            String fragmentName = fragment.getClass().getName();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setEnterTransition(null);
            }
            fragmentTransaction.add(layoutId, fragment, fragmentName);
            if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();

            hideKeyboard();
        } catch (Exception e) {
            AppLogger.w("addFragment", e.toString());
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

    @Override
    public void handleApiFailure(@NonNull Throwable t) {
        showAlert(getString(R.string.alert), t.getMessage());
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
            showAlert(getString(R.string.alert), R.string.alert_error);
        }
    }

    public void showFullImage(String imageUrl) {
        Intent intent = ImageViewActivity.newIntent(this);
        intent.putExtra(AppConstants.IMAGE_VIEW_URL, imageUrl);
        startActivity(intent);
    }

    public void showFullBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = BitmapImageViewActivity.newIntent(this);
        intent.putExtra(AppConstants.IMAGE_VIEW_URL, byteArray);
        startActivity(intent);
    }

    public void setupSearchSetting(SearchView searchView) {
        searchView.setActivated(true);
        if (getViewModel().getDataManager().isCommercial())
            searchView.setQueryHint(getString(R.string.search_commercial_visitor_data, getViewModel().getDataManager().getLevelName()));
        else searchView.setQueryHint(getString(R.string.search_data));
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        //TextView searchText = searchView.findViewById(R.id.search_src_text);
        //searchText.setTextSize(16);
        //searchText.setTypeface(ResourcesCompat.getFont(this, R.font.futura_round_medium));
        searchView.setOnSearchClickListener(v -> hideKeyboard());
    }

    public void setStatusBarColor(Activity activity, int color) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity,color));

        }
}

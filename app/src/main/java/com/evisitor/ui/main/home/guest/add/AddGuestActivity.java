package com.evisitor.ui.main.home.guest.add;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityAddGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.main.home.guest.add.dialogs.GenderPickerBottomSheetDialog;

public class AddGuestActivity extends BaseActivity<ActivityAddGuestBinding,AddGuestViewModel> implements AddGuestNavigator, View.OnClickListener {


    public static Intent getStartIntent(Context context){
        return new Intent(context,AddGuestActivity.class);
    }
    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_guest;
    }

    @Override
    public AddGuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(AddGuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_add_guest);

        setUpClick();

        getViewDataBinding().etHouseNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpClick() {
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        getViewDataBinding().tvGender.setOnClickListener(this);
        getViewDataBinding().tvOwner.setOnClickListener(this);
        getViewDataBinding().tvHost.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.tv_gender:
               GenderPickerBottomSheetDialog.newInstance().setItemSelectedListener(data -> getViewDataBinding().tvGender.setText(data)).show(getSupportFragmentManager());
                break;


            case R.id.tv_owner:
                onBackPressed();
                break;

            case R.id.tv_host:
                onBackPressed();
                break;
        }

    }
}

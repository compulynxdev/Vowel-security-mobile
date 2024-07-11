package com.evisitor.ui.main.commercial.secondryguest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.SecondaryGuest;
import com.evisitor.databinding.SecondryGuestInputDialogBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.AppLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

public class SecondaryGuestInputActivity extends BaseActivity<SecondryGuestInputDialogBinding, SecoundryGuestInputViewModel> implements View.OnClickListener {

    private List<SecondaryGuest> beans;
    private SecondaryGuestAdapter adapter;
    private boolean isAdd;
    private boolean isCheckIn;
    private boolean checkInApproved;
    List<CheckInTemperature> guestIds = new ArrayList<>();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SecondaryGuestInputActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.secondry_guest_input_dialog;
    }

    @Override
    public SecoundryGuestInputViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(SecoundryGuestInputViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        setUpIntent(getIntent());
        setUp();
        setUpAdapter();
    }

    private void setUpIntent(Intent intent) {
        beans = new ArrayList<>();
        if (intent.hasExtra("list")) {
            Type listType = new TypeToken<List<SecondaryGuest>>() {
            }.getType();
            beans.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(intent.getStringExtra("list"), listType)));
        }
        isAdd = intent.getBooleanExtra("add", false);
        isCheckIn = intent.getBooleanExtra("checkIn", false);
        checkInApproved = intent.getBooleanExtra("checkInApproved",false);
        if(isCheckIn){
            getViewDataBinding().btnOk.setText(R.string.check_in);
        }
    }

    private void setUpAdapter() {
        if (beans.isEmpty()) {
            String dialing_code = EVisitor.getInstance().getDataManager().getUserDetail().getDialingCode();
            beans.add(new SecondaryGuest("", getString(R.string.member).concat(" ").concat("1"), "", dialing_code, "", "", "", false, 0));
        }
        adapter = new SecondaryGuestAdapter(beans, new SecondaryGuestAdapter.AdapterCallback() {
            @Override
            public void onChangeList(List<SecondaryGuest> deviceList) {
                beans = deviceList;
            }

            @Override
            public void onRemove(int position) {
                beans.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onCheckInSelected(int id) {
                if(guestIds!=null){
                    CheckInTemperature tmp = new CheckInTemperature();
                    tmp.setBodyTemperature("");
                    tmp.setId(id);
                    guestIds.add(tmp);
                }
            }

            @Override
            public void onCheckInDeselected(int id) {
                if(guestIds!=null){
                    for(int i=0;i < guestIds.size();i++){
                        if(guestIds.get(i).getId() == id)
                            guestIds.remove(i);
                    }
                }
            }

            @Override
            public void onCheckInTemperature(int id, String temperature) {
                if(guestIds!=null){
                    for(int i=0;i < guestIds.size();i++){
                        if(guestIds.get(i).getId() == id)
                            guestIds.get(i).setBodyTemperature(temperature);
                    }
                }
            }
        }, getSupportFragmentManager());
        adapter.setIsAdd(isAdd);
        adapter.setIsCheckIn(isCheckIn);
        adapter.checkInApproved(checkInApproved);
        if(isCheckIn)
            getViewDataBinding().toolbar.tvTitle.setText(R.string.title_secoundary_guest);
        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    private void setUp() {
        if(isAdd)
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_add_secoundry_guest);
        else getViewDataBinding().toolbar.tvTitle.setText(R.string.title_secoundary_guest);

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
        if (isAdd) {
            imgSearch.setVisibility(View.VISIBLE);
            getViewDataBinding().btnOk.setVisibility(View.VISIBLE);
        } else if(isCheckIn){
            imgSearch.setVisibility(View.GONE);
            getViewDataBinding().btnOk.setVisibility(View.VISIBLE);
        }else{
            imgSearch.setVisibility(View.GONE);
            getViewDataBinding().btnOk.setVisibility(View.GONE);
        }
        imgSearch.setOnClickListener(this);
        //getViewDataBinding().setOnClickListener(this);
        getViewDataBinding().btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back) {
            onBackPressed();
        }else if(id == R.id.btn_ok){
            if(isCheckIn){
                    /*if(guestIds.isEmpty()){
                        showAlert(R.string.alert, R.string.please_select_guest).show(getSupportFragmentManager());
                    }else{*/
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                String yourListAsString = new Gson().toJson(guestIds);
                bundle.putString("data", yourListAsString);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                //}
            }else{
                if (!beans.isEmpty()) {
                    if (mViewModel.verifyGuestDetails(beans)) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        String yourListAsString = new Gson().toJson(beans);
                        AppLogger.i("Device List", yourListAsString);
                        bundle.putString("data", yourListAsString);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showAlert(R.string.alert, R.string.please_fill_guest_details).show(getSupportFragmentManager());
                    }
                } else {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    String yourListAsString = new Gson().toJson(beans);
                    AppLogger.i("Device List", yourListAsString);
                    bundle.putString("data", yourListAsString);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }else if (id == R.id.img_search){
            if (beans.size() < 10) {
                if (mViewModel.verifyGuestDetails(beans)) {
                    String dialing_code = EVisitor.getInstance().getDataManager().getUserDetail().getDialingCode();
                    beans.add(new SecondaryGuest( "", getString(R.string.member).concat(" ").concat("1"), "", dialing_code, "", "","",false,0));
                    adapter.notifyDataSetChanged();
                    getViewDataBinding().recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                } else
                    showAlert(R.string.alert, R.string.please_add_group_member_detail_first).show(getSupportFragmentManager());
            } else
                showAlert(R.string.alert, R.string.can_add_more_member).show(getSupportFragmentManager());
        }

    }

}

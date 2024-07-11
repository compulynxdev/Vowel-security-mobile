package com.evisitor.ui.main.commercial.add.whomtomeet;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.SelectCommercialStaffResponse;
import com.evisitor.databinding.DialogWhomToMeetBinding;
import com.evisitor.ui.base.BaseBottomSheetDialog;
import com.evisitor.ui.main.commercial.add.whomtomeet.level.SelectLastLevelFragment;
import com.evisitor.ui.main.commercial.add.whomtomeet.staff.SelectStaffFragment;
import com.evisitor.util.ViewPagerAdapter;

public class WhomToMeetBottomSheet extends BaseBottomSheetDialog<DialogWhomToMeetBinding, WhomToMeetViewModel> implements View.OnClickListener, WhomToMeetCallback {

    private static final String TAG = "WhomToMeetBottomSheet";
    private SelectLastLevelFragment selectLastLevelFragment;
    private SelectStaffFragment selectStaffFragment;
    private WhomToMeetCallback callback;

    public static WhomToMeetBottomSheet newInstance(WhomToMeetCallback callback) {

        Bundle args = new Bundle();

        WhomToMeetBottomSheet fragment = new WhomToMeetBottomSheet();
        fragment.setArguments(args);
        fragment.setCallback(callback);
        return fragment;
    }

    public void setCallback(WhomToMeetCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(R.style.BottomSheetDialogThemeBG);
        setDraggableFalse();
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_whom_to_meet;
    }

    @Override
    public WhomToMeetViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(WhomToMeetViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().header.tvTitle.setText(R.string.title_who_to_meet);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(this);

        getViewDataBinding().tvTab1.setText(mViewModel.getDataManager().getLevelName());
        getViewDataBinding().tvTab1.setOnClickListener(this);
        getViewDataBinding().tvTab2.setOnClickListener(this);

        setUpPagerAdapter();
        setUpSearch();
    }

    private void setUpSearch() {

        getViewDataBinding().header.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgSearch.setOnClickListener(this);

        getBaseActivity().setupSearchSetting(getViewDataBinding().customSearchView.searchView);
        if (getViewModel().getDataManager().isCommercial())
            getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_data_trespasser, ""));
        else
            getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_data_trespasser));
        getViewDataBinding().customSearchView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty() || newText.trim().length() >= 2) {
                    if (getViewDataBinding().viewPager.getCurrentItem() == 0)
                        selectLastLevelFragment.setSearch(newText);
                    else
                        selectStaffFragment.setSearch(newText);
                }
                return false;
            }
        });

    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        selectLastLevelFragment = SelectLastLevelFragment.newInstance(this);
        adapter.addFragment(selectLastLevelFragment);
        selectStaffFragment = SelectStaffFragment.newInstance(this);
        adapter.addFragment(selectStaffFragment);
        getViewDataBinding().viewPager.setOffscreenPageLimit(2);

        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getViewDataBinding().customSearchView.searchView.setQuery("", false);

                if (position == 0) {
                    getViewDataBinding().tvTab1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvTab2.setTextColor(getResources().getColor(R.color.black));
                } else {
                    getViewDataBinding().tvTab1.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvTab2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_back) {
            dismiss();
        } else if (id == R.id.tv_tab_1) {
            getViewDataBinding().viewPager.setCurrentItem(0, true);
        } else if (id == R.id.tv_tab_2) {
            getViewDataBinding().viewPager.setCurrentItem(1, true);
        } else if (id == R.id.img_search) {
            hideKeyboard();
            if (getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE) {
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(View.VISIBLE);
            } else {
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(View.GONE);
            }
            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        }

    }

    @Override
    public void onLastLevelClick(HouseDetailBean houseDetailBean) {
        if (callback != null) callback.onLastLevelClick(houseDetailBean);
        dismiss();
    }

    @Override
    public void onStaffClick(SelectCommercialStaffResponse staffDetail) {
        if (callback != null) callback.onStaffClick(staffDetail);
        dismiss();
    }
}

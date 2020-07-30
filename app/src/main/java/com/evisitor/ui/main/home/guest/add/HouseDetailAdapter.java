package com.evisitor.ui.main.home.guest.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evisitor.R;
import com.evisitor.data.model.HouseDetailBean;

import java.util.List;

public class HouseDetailAdapter extends ArrayAdapter<HouseDetailBean> {

    private Context mContext;
    private List<HouseDetailBean> list;

    HouseDetailAdapter(@NonNull Context context, List<HouseDetailBean> list) {
        super(context, 0, list);
        mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_name, parent, false);

        HouseDetailBean houseDetailBean = list.get(position);

        TextView name = listItem.findViewById(R.id.tv_name);
        name.setText(houseDetailBean.getName());

        return listItem;
    }
}

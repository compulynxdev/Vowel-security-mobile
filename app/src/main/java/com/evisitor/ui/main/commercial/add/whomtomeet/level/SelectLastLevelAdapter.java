package com.evisitor.ui.main.commercial.add.whomtomeet.level;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;
import com.evisitor.util.AppUtils;

import java.util.List;

public class SelectLastLevelAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<HouseDetailBean> list;
    private final ItemClickCallback callback;

    SelectLastLevelAdapter(List<HouseDetailBean> list, ItemClickCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_last_level, parent, false);
        return new SelectLastLevelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends BaseViewHolder {
        final TextView name, profileName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            profileName = itemView.findViewById(R.id.tv_circle);

            itemView.setOnClickListener(v -> {
                if (callback != null && getAdapterPosition() != -1)
                    callback.onItemClick(getAdapterPosition());
            });
        }

        @Override
        public void onBind(int position) {
            HouseDetailBean bean = list.get(position);
            name.setText(name.getContext().getString(R.string.data_name, bean.getName()));
            profileName.setText(AppUtils.getFirstLetter(bean.getName()));
        }
    }

}

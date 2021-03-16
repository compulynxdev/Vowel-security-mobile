package com.evisitor.ui.dialog.country;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.CountryResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;

import java.util.List;

public class CountrySelectionAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<CountryResponse> list;
    private final ItemClickCallback clickListener;
    private final boolean isShowDialCode;

    CountrySelectionAdapter(boolean isShowDialCode, List<CountryResponse> list, ItemClickCallback clickListener) {
        this.isShowDialCode = isShowDialCode;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder rvHolder, int position) {
        rvHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        private final TextView tv_name;
        private final TextView tv_code;

        ViewHolder(@NonNull View v) {
            super(v);
            tv_name = v.findViewById(R.id.tv_name);
            tv_code = v.findViewById(R.id.tv_code);

            tv_code.setVisibility(isShowDialCode ? View.VISIBLE : View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onBind(int position) {
            CountryResponse bean = list.get(position);
            tv_name.setText(bean.getName());
            tv_code.setText("+".concat(bean.getDial_code()));
        }

        @Override
        public void onClick(@NonNull final View view) {
            int tempPos = getAdapterPosition();
            if (tempPos != -1 && clickListener != null) {
                clickListener.onItemClick(tempPos);
            }

        }
    }
}



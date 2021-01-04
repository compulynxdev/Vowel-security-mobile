package com.evisitor.ui.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.HomeBean;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<HomeBean> list;
    private final ItemClickCallback itemClickCallback;

    HomeAdapter(List<HomeBean> list, ItemClickCallback itemClickCallback) {
        this.list = list;
        this.itemClickCallback = itemClickCallback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeAdapter.ViewHolder(view);
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

        final ImageView img;
        final TextView tv_title, tv_count;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_title = itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(v -> {
                if (itemClickCallback != null && getAdapterPosition() != -1) {
                    itemClickCallback.onItemClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void onBind(int position) {
            HomeBean mainBean = list.get(position);
            tv_count.setVisibility(mainBean.getCount().equals("0") ? View.GONE : View.VISIBLE);
            img.setImageDrawable(ContextCompat.getDrawable(img.getContext(), mainBean.getIcon()));
            tv_title.setText(mainBean.getTitle());
            tv_count.setText(mainBean.getCount());
        }
    }
}

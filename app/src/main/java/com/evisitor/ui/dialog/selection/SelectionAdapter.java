package com.evisitor.ui.dialog.selection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;

import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List list;
    private final ItemClickCallback clickListener;

    SelectionAdapter(List list, ItemClickCallback clickListener) {
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection, parent, false);
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

        ViewHolder(@NonNull View v) {
            super(v);
            tv_name = v.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onBind(int position) {
            String data = list.get(position).toString();
            tv_name.setText(data);
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



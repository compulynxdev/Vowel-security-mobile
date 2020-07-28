package com.evisitor.ui.main.visitorprofile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewHolder;

import java.util.List;

public class VisitorProfileAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<VisitorProfileBean> list;

    VisitorProfileAdapter(List<VisitorProfileBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        return new VisitorProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ViewHolder extends BaseViewHolder {

        ConstraintLayout constraint_editable;
        TextView tv_name, tv_title;
        EditText et_data;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            constraint_editable = itemView.findViewById(R.id.constraint_editable);
            tv_title = itemView.findViewById(R.id.tv_title);
            et_data = itemView.findViewById(R.id.et_data);
        }

        @Override
        public void onBind(int position) {
            VisitorProfileBean bean = list.get(position);

            if (bean.isEditable()) {
                constraint_editable.setVisibility(View.VISIBLE);
                tv_title.setText(bean.getTitle());
                et_data.setText(bean.getValue());
            } else {
                constraint_editable.setVisibility(View.GONE);
                tv_name.setText(bean.getTitle());
            }
        }
    }
}

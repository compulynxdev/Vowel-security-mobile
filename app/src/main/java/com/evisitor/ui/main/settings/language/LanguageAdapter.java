package com.evisitor.ui.main.settings.language;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.LanguageBean;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<LanguageBean> list;
    private final ItemClickCallback clickListener;
    int lastPos = -1;

    LanguageAdapter(List<LanguageBean> list, ItemClickCallback clickListener) {
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang, parent, false);
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
        private final TextView tvName;
        private final FrameLayout frameCheck;

        ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            frameCheck = v.findViewById(R.id.frameCheck);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onBind(int position) {
            LanguageBean bean = list.get(position);
            tvName.setText(bean.getLocalisationTitle());
            tvName.setTextColor(bean.getIsSelected() ? tvName.getContext().getResources().getColor(R.color.colorPrimary) : tvName.getContext().getResources().getColor(R.color.black));
            frameCheck.setVisibility(bean.getIsSelected() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(@NonNull final View view) {
            int tempPos = getAdapterPosition();
            if (tempPos != -1 && clickListener != null) {
                if (lastPos != -1) {
                    list.get(lastPos).setIsSelected(false);
                    notifyItemChanged(lastPos);
                }

                clickListener.onItemClick(getAdapterPosition());
                list.get(tempPos).setIsSelected(true);
                notifyItemChanged(tempPos);
                lastPos = tempPos;
            }

        }
    }
}



package com.evisitor.ui.main.home.blacklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.R;
import com.evisitor.data.model.BlackListVisitorResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class BlackListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BlackListVisitorResponse.ContentBean> list;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private ItemClickCallback callback;
    private boolean showLoader;

    BlackListAdapter(List<BlackListVisitorResponse.ContentBean> list, ItemClickCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black_list, parent, false);
                return new BlackListAdapter.ViewHolder(view);

            default:
            case VIEWTYPE_LOADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loader, parent, false);
                return new FooterLoader(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()-1) {
            return showLoader ? VIEWTYPE_LOADER : VIEWTYPE_ITEM;
        }
        return VIEWTYPE_ITEM;
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof FooterLoader) {
            FooterLoader loaderViewHolder = (FooterLoader) holder;
            if (showLoader) {
                loaderViewHolder.mProgressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.mProgressBar.setVisibility(View.GONE);
            }
            return;
        }
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends BaseViewHolder {
        ImageView imgVisitor;
        TextView name, docId, profile, contact, type;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            docId = itemView.findViewById(R.id.tv_doc_id);
            profile = itemView.findViewById(R.id.tv_profile);
            contact = itemView.findViewById(R.id.tv_contact);
            type = itemView.findViewById(R.id.tv_type);
            imgVisitor = itemView.findViewById(R.id.img_visitor);

            itemView.setOnClickListener(v -> {
                if (callback != null && getAdapterPosition() != -1)
                    callback.onItemClick(getAdapterPosition());
            });

            imgVisitor.setOnClickListener(v -> {
                if (getAdapterPosition() != -1)
                    showFullImage(list.get(getAdapterPosition()).getImageUrl());
            });
        }

        @Override
        public void onBind(int position) {
            BlackListVisitorResponse.ContentBean bean = list.get(position);
            name.setText(name.getContext().getString(R.string.data_name,bean.getFullName()));
            if (bean.getDocumentId()!=null && !bean.getDocumentId().isEmpty()){
                docId.setVisibility(View.VISIBLE);
                docId.setText(docId.getContext().getString(R.string.data_identity,bean.getDocumentId()));
            }else docId.setVisibility(View.GONE);

            if (bean.getProfile()!=null && !bean.getProfile().isEmpty()){
                profile.setVisibility(View.VISIBLE);
                profile.setText(profile.getContext().getString(R.string.data_profile,bean.getProfile()));
            }else profile.setVisibility(View.GONE);

            if (bean.getContactNo()!=null && !bean.getContactNo().isEmpty()){
                contact.setVisibility(View.VISIBLE);
                contact.setText(contact.getContext().getString(R.string.data_mobile, bean.getDialingCode().concat(bean.getContactNo())));
            }else contact.setVisibility(View.GONE);

            if (bean.getType()!=null && !bean.getType().isEmpty()){
                type.setVisibility(View.VISIBLE);
                type.setText(type.getContext().getString(R.string.data_type,bean.getType()));
            }else type.setVisibility(View.GONE);

            if (bean.getImageUrl().isEmpty()) {
                Glide.with(imgVisitor.getContext())
                        .load(R.drawable.ic_person)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVisitor);
            } else {
                Glide.with(imgVisitor.getContext())
                        .load(getImageUrl(bean.getImageUrl()))
                        .centerCrop()
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVisitor);
            }
        }
    }

}

package com.evisitor.ui.main.home.flag;

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
import com.evisitor.data.model.FlaggedVisitorResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;
import com.evisitor.util.CommonUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class FlagVisitorAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    private final List<FlaggedVisitorResponse.ContentBean> list;
    private final ItemClickCallback callback;
    private boolean showLoader;

    FlagVisitorAdapter(List<FlaggedVisitorResponse.ContentBean> list, ItemClickCallback callback) {
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
                return new FlagVisitorAdapter.ViewHolder(view);

            default:
            case VIEWTYPE_LOADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loader, parent, false);
                return new FooterLoader(view);
        }
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() - 1) {
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
        final ImageView imgVisitor;
        final TextView name, docId, profile, contact, type;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            docId = itemView.findViewById(R.id.tv_doc_id);
            docId.setVisibility(View.GONE);
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
            FlaggedVisitorResponse.ContentBean bean = list.get(position);
            name.setText(name.getContext().getString(R.string.data_name, bean.getFullName()));
            if (bean.getProfile() != null && !bean.getProfile().isEmpty()) {
                profile.setVisibility(View.VISIBLE);
                profile.setText(profile.getContext().getString(R.string.data_profile, bean.getProfile()));
            } else profile.setVisibility(View.GONE);

            if (bean.getContactNo() != null && !bean.getContactNo().isEmpty()) {
                contact.setVisibility(View.VISIBLE);
                contact.setText(contact.getContext().getString(R.string.data_mobile, CommonUtils.paritalEncodeData("".concat(bean.getDialingCode()).concat(" ").concat(bean.getContactNo()))));
            } else contact.setVisibility(View.GONE);

            if (bean.getType() != null && !bean.getType().isEmpty()) {
                type.setVisibility(View.VISIBLE);
                type.setText(type.getContext().getString(R.string.data_type, CommonUtils.getVisitorType(bean.getType())));
            } else type.setVisibility(View.GONE);

            if (bean.getImageUrl().isEmpty()) {
                Glide.with(imgVisitor.getContext())
                        .load(R.drawable.ic_person)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVisitor);
            } else {
                Glide.with(imgVisitor.getContext())
                        .load(getImageBaseUrl(bean.getImageUrl()))
                        .centerCrop()
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVisitor);
            }
        }
    }

}

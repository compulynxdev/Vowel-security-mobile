package com.evisitor.util.pagination;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.evisitor.eVisitor.R;
import com.evisitor.eVisitor.ui.base.BaseViewHolder;


/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public final class FooterLoader extends BaseViewHolder {

    public ProgressBar mProgressBar;

    public FooterLoader(@NonNull View itemView) {
        super(itemView);
        mProgressBar = itemView.findViewById(R.id.progressbar);
    }

    @Override
    public void onBind(int position) {

    }
}

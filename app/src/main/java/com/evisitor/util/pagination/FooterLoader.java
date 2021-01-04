package com.evisitor.util.pagination;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.ui.base.BaseViewHolder;


/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public final class FooterLoader extends BaseViewHolder {

    public final ProgressBar mProgressBar;

    public FooterLoader(@NonNull View itemView) {
        super(itemView);
        mProgressBar = itemView.findViewById(R.id.progressbar);
    }

    @Override
    public void onBind(int position) {

    }
}

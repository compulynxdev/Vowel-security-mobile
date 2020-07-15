

package com.evisitor.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.util.Date;

/**
 * Created by Hemant Sharma on 23-02-20.
 */

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }

    @BindingAdapter("bindServerDate")
    public static void bindServerDate(@NonNull TextView textView, Date date) {
        /*Parse string data and set it in another format for your textView*/
        textView.setText(CalenderUtils.formatDate(date,CalenderUtils.CUSTOM_TIMESTAMP_FORMAT_SLASH));
    }
}

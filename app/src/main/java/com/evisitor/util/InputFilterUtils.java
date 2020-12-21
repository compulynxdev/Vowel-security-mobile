package com.evisitor.util;

import android.content.Context;
import android.text.InputFilter;
import android.widget.Toast;

public class InputFilterUtils {

    public static InputFilter getInputLetterAndDigits(Context context) {
        return (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i))) { // Accept only letter & digits ; otherwise just return
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        };
    }
}

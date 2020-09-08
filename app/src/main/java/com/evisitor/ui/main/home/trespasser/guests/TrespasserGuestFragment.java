package com.evisitor.ui.main.home.trespasser.guests;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evisitor.R;

public class TrespasserGuestFragment extends Fragment {

    public static TrespasserGuestFragment newInstance(String param1, String param2) {
        TrespasserGuestFragment fragment = new TrespasserGuestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}

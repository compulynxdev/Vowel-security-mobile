package com.evisitor.ui.main.home.trespasser.services;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evisitor.R;

public class TrespasserSPFragment extends Fragment {
   public static TrespasserSPFragment newInstance(String param1, String param2) {
        TrespasserSPFragment fragment = new TrespasserSPFragment();
        Bundle args = new Bundle();
       fragment.setArguments(args);
        return fragment;
    }


}

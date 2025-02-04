// Generated by view binder compiler. Do not edit!
package com.cll.miniposprinter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cll.miniposprinter.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentItemListDialogListDialogBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final Button actionBtn;

  @NonNull
  public final Button iconButton;

  @NonNull
  public final RecyclerView list;

  @NonNull
  public final LinearProgressIndicator progress;

  @NonNull
  public final FrameLayout standardBottomSheet;

  @NonNull
  public final TextView status;

  private FragmentItemListDialogListDialogBinding(@NonNull CoordinatorLayout rootView,
      @NonNull Button actionBtn, @NonNull Button iconButton, @NonNull RecyclerView list,
      @NonNull LinearProgressIndicator progress, @NonNull FrameLayout standardBottomSheet,
      @NonNull TextView status) {
    this.rootView = rootView;
    this.actionBtn = actionBtn;
    this.iconButton = iconButton;
    this.list = list;
    this.progress = progress;
    this.standardBottomSheet = standardBottomSheet;
    this.status = status;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentItemListDialogListDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentItemListDialogListDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentItemListDialogListDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.action_btn;
      Button actionBtn = ViewBindings.findChildViewById(rootView, id);
      if (actionBtn == null) {
        break missingId;
      }

      id = R.id.iconButton;
      Button iconButton = ViewBindings.findChildViewById(rootView, id);
      if (iconButton == null) {
        break missingId;
      }

      id = R.id.list;
      RecyclerView list = ViewBindings.findChildViewById(rootView, id);
      if (list == null) {
        break missingId;
      }

      id = R.id.progress;
      LinearProgressIndicator progress = ViewBindings.findChildViewById(rootView, id);
      if (progress == null) {
        break missingId;
      }

      id = R.id.standard_bottom_sheet;
      FrameLayout standardBottomSheet = ViewBindings.findChildViewById(rootView, id);
      if (standardBottomSheet == null) {
        break missingId;
      }

      id = R.id.status;
      TextView status = ViewBindings.findChildViewById(rootView, id);
      if (status == null) {
        break missingId;
      }

      return new FragmentItemListDialogListDialogBinding((CoordinatorLayout) rootView, actionBtn,
          iconButton, list, progress, standardBottomSheet, status);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

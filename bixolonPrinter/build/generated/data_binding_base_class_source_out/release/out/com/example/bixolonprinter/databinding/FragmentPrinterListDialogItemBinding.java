// Generated by data binding compiler. Do not edit!
package com.example.bixolonprinter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.example.bixolonprinter.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentPrinterListDialogItemBinding extends ViewDataBinding {
  @NonNull
  public final TextView text;

  protected FragmentPrinterListDialogItemBinding(Object _bindingComponent, View _root,
      int _localFieldCount, TextView text) {
    super(_bindingComponent, _root, _localFieldCount);
    this.text = text;
  }

  @NonNull
  public static FragmentPrinterListDialogItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_printer_list_dialog_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentPrinterListDialogItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentPrinterListDialogItemBinding>inflateInternal(inflater, R.layout.fragment_printer_list_dialog_item, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentPrinterListDialogItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_printer_list_dialog_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentPrinterListDialogItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentPrinterListDialogItemBinding>inflateInternal(inflater, R.layout.fragment_printer_list_dialog_item, null, false, component);
  }

  public static FragmentPrinterListDialogItemBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentPrinterListDialogItemBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (FragmentPrinterListDialogItemBinding)bind(component, view, R.layout.fragment_printer_list_dialog_item);
  }
}

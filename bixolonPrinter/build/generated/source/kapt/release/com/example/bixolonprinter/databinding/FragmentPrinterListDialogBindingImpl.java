package com.example.bixolonprinter.databinding;
import com.example.bixolonprinter.R;
import com.example.bixolonprinter.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentPrinterListDialogBindingImpl extends FragmentPrinterListDialogBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.imageButton, 5);
    }
    // views
    @NonNull
    private final androidx.coordinatorlayout.widget.CoordinatorLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentPrinterListDialogBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private FragmentPrinterListDialogBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[4]
            , (android.widget.ImageView) bindings[5]
            , (com.google.android.material.progressindicator.CircularProgressIndicator) bindings[2]
            , (androidx.recyclerview.widget.RecyclerView) bindings[3]
            , (android.widget.TextView) bindings[1]
            );
        this.floatingActionButton.setTag(null);
        this.linearProgressIndicator.setTag(null);
        this.list.setTag(null);
        this.mboundView0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.txtMessage.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.vm == variableId) {
            setVm((com.example.bixolonprinter.PrinterFragmentViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVm(@Nullable com.example.bixolonprinter.PrinterFragmentViewModel Vm) {
        this.mVm = Vm;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.vm);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmMessage((kotlinx.coroutines.flow.StateFlow<java.lang.Integer>) object, fieldId);
            case 1 :
                return onChangeVmPrinterStatus((kotlinx.coroutines.flow.StateFlow<com.example.bixolonprinter.PrintingStatus>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmMessage(kotlinx.coroutines.flow.StateFlow<java.lang.Integer> VmMessage, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmPrinterStatus(kotlinx.coroutines.flow.StateFlow<com.example.bixolonprinter.PrintingStatus> VmPrinterStatus, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        kotlinx.coroutines.flow.StateFlow<java.lang.Integer> vmMessage = null;
        com.example.bixolonprinter.PrintingStatus vmPrinterStatusGetValue = null;
        kotlinx.coroutines.flow.StateFlow<com.example.bixolonprinter.PrintingStatus> vmPrinterStatus = null;
        java.lang.Integer vmMessageGetValue = null;
        com.example.bixolonprinter.PrinterFragmentViewModel vm = mVm;
        int androidxDatabindingViewDataBindingSafeUnboxVmMessageGetValue = 0;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (vm != null) {
                        // read vm.message
                        vmMessage = vm.getMessage();
                    }
                    androidx.databinding.ViewDataBindingKtx.updateStateFlowRegistration(this, 0, vmMessage);


                    if (vmMessage != null) {
                        // read vm.message.getValue()
                        vmMessageGetValue = vmMessage.getValue();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vm.message.getValue())
                    androidxDatabindingViewDataBindingSafeUnboxVmMessageGetValue = androidx.databinding.ViewDataBinding.safeUnbox(vmMessageGetValue);
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (vm != null) {
                        // read vm.printerStatus
                        vmPrinterStatus = vm.getPrinterStatus();
                    }
                    androidx.databinding.ViewDataBindingKtx.updateStateFlowRegistration(this, 1, vmPrinterStatus);


                    if (vmPrinterStatus != null) {
                        // read vm.printerStatus.getValue()
                        vmPrinterStatusGetValue = vmPrinterStatus.getValue();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            com.example.bixolonprinter.ViewBinding.bindGone(this.floatingActionButton, vmPrinterStatusGetValue);
            com.example.bixolonprinter.ViewBinding.bindShowProgress(this.linearProgressIndicator, vmPrinterStatusGetValue);
            com.example.bixolonprinter.ViewBinding.bindGone(this.list, vmPrinterStatusGetValue);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            this.txtMessage.setText(androidxDatabindingViewDataBindingSafeUnboxVmMessageGetValue);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vm.message
        flag 1 (0x2L): vm.printerStatus
        flag 2 (0x3L): vm
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}
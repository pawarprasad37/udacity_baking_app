package com.theandroiddeveloper.bakersworld.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.activity.BaseActivity;

public class ProgressDialog extends Dialog {
    private BaseActivity mActivity;

    public ProgressDialog(@NonNull BaseActivity mActivity) {
        super(mActivity, R.style.AppTheme);
        this.mActivity = mActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
    }
}

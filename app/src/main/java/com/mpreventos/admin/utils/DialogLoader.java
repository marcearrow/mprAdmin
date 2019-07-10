package com.mpreventos.admin.utils;


import android.app.Dialog;
import android.content.Context;

import com.mpreventos.admin.R;

public class DialogLoader {
    Dialog dialog;
    Context context;

    public DialogLoader(Context context) {
        this.context = context;
    }

    public void CreateDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);

    }

    public void ShowDialog() {
        if (dialog != null) {
            dialog.show();
        }

    }

    public void DismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}

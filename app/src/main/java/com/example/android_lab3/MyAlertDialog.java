package com.example.android_lab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyAlertDialog extends DialogFragment {

    public MyAlertDialog() {}

    public static MyAlertDialog newInstance(String title, String desc) {
        MyAlertDialog frag = new MyAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("desc", desc);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println(getClass().getName() + " onCreate");
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        System.out.println(getClass().getName() + " onCreateDialog");
        String title = getArguments().getString("title", "default title");
        String msg = getArguments().getString("desc", "default description");
        AlertDialog.Builder devInfoDlg  = new AlertDialog.Builder(getActivity());
        devInfoDlg.setMessage(msg);
        devInfoDlg.setTitle(title);
        // add only OK button to dialog
        devInfoDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        devInfoDlg.setCancelable(true);
        Dialog dlg = devInfoDlg.create();
        dlg.show();
        return dlg;
    }
}


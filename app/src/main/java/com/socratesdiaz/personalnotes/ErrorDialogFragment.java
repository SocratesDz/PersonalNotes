package com.socratesdiaz.personalnotes;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by socratesdiaz on 11/25/16.
 */
public class ErrorDialogFragment extends DialogFragment {

    public ErrorDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int errorCode = getArguments().getInt(AppConstant.DIALOG_ERROR);
        return GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), errorCode, errorCode);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getActivity().finish();
    }
}

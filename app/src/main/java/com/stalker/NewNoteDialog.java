package com.stalker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Diana on 24.05.2016 at 18:51.
 */
public class NewNoteDialog extends DialogFragment {

    private DialogInterface.OnClickListener saveToDB;
    private DialogInterface.OnClickListener cancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

        builder.setView(inflater.inflate(R.layout.dialog_new_note, null))
                .setPositiveButton(context.getString(R.string.add), saveToDB)
                .setNegativeButton(context.getString(R.string.cancel), cancel);
        return builder.create();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_note, container, false);
    }

    public interface NewNoteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}

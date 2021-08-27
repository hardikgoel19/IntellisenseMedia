package com.video.player.intellisensemedia.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.interfaces.OnInput;

import java.util.ArrayList;

public class DialogHelper {

    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
    }

    public void showInputDialog(String btnText, final OnInput onLibraryNameInput) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_library, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onLibraryNameInput.onInput(editText.getText().toString());
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    public void showListDialog(ArrayList<String> names, DialogInterface.OnClickListener onClickListener) {

        String[] namesString = new String[names.size()];
        for (int i = 0; i < names.size(); i++) namesString[i] = names.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(namesString, onClickListener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

    public void confirmationDialog(String message, DialogInterface.OnClickListener onYesClicked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage(message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", onYesClicked);
        builder.create().show();
    }

}

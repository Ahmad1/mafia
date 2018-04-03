package com.example.ahmadnemati.mfa.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmadnemati.mfa.PreferenceHelper;
import com.example.ahmadnemati.mfa.R;
import com.example.ahmadnemati.mfa.domain.Player;

import butterknife.Unbinder;

/**
 * Created by ahmad.nemati on 2/27/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected static final String CONFIRMED = "Confirm";
    protected static final String EXIT = "Exit?";
    protected static final String SETUP = "First do the setup";
    protected PreferenceHelper prefs;

    protected String s;
    protected Class clazz;

    protected abstract int resourceId();
    protected abstract Unbinder getUnbinder();

    interface DialogListener {
        void onSuccess(String result);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getUnbinder() != null) getUnbinder().unbind();
    }

    protected void confirmAccess(String s, Class clazz) {
        /*this.s = s;
        this.clazz = clazz;
        showInputDialog(this, "Confirm Access Code", null);
        */
        openActivity(clazz);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * Shows a dialog asking for an input from user, option to cancel or enter a text and click ok.
     * @param listener will receive the text that user has entered.
     * @param title
     * @param message
     */
    protected void showInputDialog(final DialogListener listener, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        if (message != null)  ((TextView)view.findViewById(R.id.dialog_message)).setText(message);
        final EditText input = view.findViewById(R.id.dialog_input);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (input!= null) {
                            listener.onSuccess(input.getText().toString());
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
    }

    /**
     * Shows a dialog affirming intention of the user.
     * @param listener will receive the title back to listener.
     * @param title
     * @param message
     * @param cancelable
     */
    protected void showAffirmingDialog(final DialogListener listener, final String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        if (message != null)  ((TextView)view.findViewById(R.id.dialog_message)).setText(message);
        view.findViewById(R.id.input_container).setVisibility(View.GONE);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                            listener.onSuccess(title);
                    }
                });

        if (cancelable)  {
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        builder.setCancelable(cancelable);
        builder.create().show();
    }

}

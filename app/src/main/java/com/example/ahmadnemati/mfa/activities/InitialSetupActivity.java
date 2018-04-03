package com.example.ahmadnemati.mfa.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahmadnemati.mfa.PreferenceHelper;
import com.example.ahmadnemati.mfa.R;
import com.example.ahmadnemati.mfa.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InitialSetupActivity extends BaseActivity implements BaseActivity.DialogListener {
    @BindView(R.id.access_code_btn)
    Button setAccessCode;
    @BindView(R.id.current_code)
    TextView currentCode;
    @BindView(R.id.number_of_ppl_btn)
    Button numberOfPpl;
    @BindView(R.id.current_Number)
    TextView currentNumber;
    @BindView(R.id.ppl_container)
    LinearLayout PplContainer;

    Unbinder unbinder;

    private int currentItemId = 0;

    @Override
    protected int resourceId() {
        return R.layout.activity_setup;
    }

    @Override
    protected Unbinder getUnbinder() {
        return unbinder;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourceId());
        setTitle("Initial Setup");
        unbinder = ButterKnife.bind(this);
        if (prefs == null) prefs = PreferenceHelper.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentCode == null) return;
        setCurrentValues();
    }

    private void setCurrentValues() {
        currentCode.setText("Current Access Code is : " + prefs.getAccessCode());
        int count = Integer.valueOf(prefs.getCurrentNumberOfPpl());
        currentNumber.setText("Current Number of People is : " + count);
        if (count > 0) {
            populatePeopleNames(count);
        }
    }

    @OnClick({R.id.number_of_ppl_btn, R.id.access_code_btn, R.id.done})
    public void onClick(View v) {
        currentItemId = v.getId();
        switch (currentItemId) {
            case R.id.access_code_btn:
                showInputDialog(this, "New Access Code", null);
                break;
            case R.id.number_of_ppl_btn:
                showInputDialog(this, "Enter number of People", null);
                break;
            case R.id.done:
                if (PplContainer.getChildCount() > 0) {
                    ArrayList<String> names = new ArrayList<>();
                    for (int i= 0; i < PplContainer.getChildCount(); i++) {
                        if (PplContainer.getChildAt(i).findViewById(R.id.name) != null) {
                            names.add(((EditText) PplContainer.getChildAt(i).findViewById(R.id.name)).getText().toString().trim());
                        }
                    }
                    prefs.setPeopleNames(Util.namesToString(names));
                }
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(String input) {
        switch (currentItemId) {
            case R.id.access_code_btn:
                prefs.setAccessCode(input);
                break;
            case R.id.number_of_ppl_btn:
                prefs.setCurrentNumberOfPpl(input);
                adjustNamesList();
                break;
        }
        setCurrentValues();
    }

    private void populatePeopleNames(int count) {
        if (prefs.getPeopleNames().length() > 0) {
            ArrayList<String> names = Util.namesToArrayList(prefs.getPeopleNames());
            int namesCount = names.size();
            adjustNamesList();
            count = count > namesCount ? namesCount : count;
            for (int i = 0; i < count; i++) {
                Log.w("zzzzz", "count: " + count + " " + names.toString());
                ((EditText) PplContainer.getChildAt(i).findViewById(R.id.name)).setText(names.get(i));
            }

        }

    }

    private void adjustNamesList() {
        int pplCount = Integer.valueOf(prefs.getCurrentNumberOfPpl());
        if (pplCount > 0 /*&& PplContainer.getChildCount() < pplCount*/) {
            PplContainer.removeAllViews();
            // check for string array of names in prefs
            for (int i = 0; i< pplCount ;i++) {
                View view = getLayoutInflater().inflate(R.layout.person_input, null);
                TextInputLayout e = view.findViewById(R.id.name_input);
                e.setHint((i + 1) + ":");
                PplContainer.addView(view);
            }
        }
    }
}








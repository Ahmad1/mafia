package com.example.ahmadnemati.mfa.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ahmadnemati.mfa.PreferenceHelper;
import com.example.ahmadnemati.mfa.R;
import com.example.ahmadnemati.mfa.Util;
import com.example.ahmadnemati.mfa.domain.Player;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmad.nemati on 2/28/18.
 */

public class CastingActivity extends BaseActivity implements View.OnClickListener, BaseActivity.DialogListener {

    private Unbinder unbinder;
    @BindView(R.id.generic_container)
    LinearLayout castContainer;
    @BindView(R.id.generic_done)
    Button castingDone;

    private ArrayList<Player.Role> roles;
    private ArrayList<Player> players = new ArrayList<>();

    @Override
    protected int resourceId() {
        return R.layout.generic_view;
    }

    @Override
    protected Unbinder getUnbinder() {
        return unbinder;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourceId());
        setTitle("Pick Roles");
        unbinder = ButterKnife.bind(this);
        castingDone.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs == null) prefs = PreferenceHelper.getInstance();
        populatePeopleNames();
    }

    private void populatePeopleNames() {
        ArrayList<String> names = Util.namesToArrayList(prefs.getPeopleNames());
        if (names.size() > 1) {
            castContainer.removeAllViews();
            Log.d("zzzzz", "casting names " + names.toString());
            for (String name : names) {
                View view = getLayoutInflater().inflate(R.layout.person_btn, null);
                Button nameBtn = view.findViewById(R.id.person_btn);
                nameBtn.setText(name);
                nameBtn.setTag(name);
                nameBtn.setOnClickListener(this);
                castContainer.addView(view);
            }
        } else {
            showAffirmingDialog(this, SETUP, getString(R.string.setup_first_txt), false);
        }
    }

    private Button selectedBtn;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.generic_done) {
            checkSaveAndExit();
        } else if (v instanceof Button) {
            Log.i("zzzzz", "name click: " + v.getTag());
            selectedBtn = (Button) v;
            showAffirmingDialog(this, v.getTag().toString(), "Ready to grab a role?", true);
        }
    }

    private void checkSaveAndExit() {
        if (checkForPoopingInGame()) return;
        if (players.size() > 1) {
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                p.index = i;
                prefs.savePerson(p, i);
            }
        }
        finish();
    }

    private boolean checkForPoopingInGame() {
        if (roles != null && roles.size() > 0) {
            showAffirmingDialog(this, EXIT, getString(R.string.exit_casting), true);
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(String result) {
        if (result.equals(CONFIRMED)) {
            // grey out the button and set it's onClick to null
            selectedBtn.setBackgroundColor(Color.parseColor("#dddddd"));
            selectedBtn.setOnClickListener(null);
        } else if (result.equals(SETUP)) {
            openActivity(InitialSetupActivity.class);
            finish();
        } else if (result.equals(EXIT)) {
            finish();
        } else {
            if (roles == null) {
                int peopleCount = Util.namesToArrayList(prefs.getPeopleNames()).size();
                roles = Util.getRoles(peopleCount);
            }
            // randomly grab a role for name and create a person object
            // remove taken role from stack
            Collections.shuffle(roles);
            Player.Role role = roles.remove(0);
            showAffirmingDialog(this, CONFIRMED, result + " You Are:    " + role.name(), false);

            players.add(new Player(result, role, -1));
        }
    }

    @Override
    public void onBackPressed() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else {
            checkSaveAndExit();
        }
    }

}

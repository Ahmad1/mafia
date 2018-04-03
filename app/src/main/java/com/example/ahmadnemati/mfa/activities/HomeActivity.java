package com.example.ahmadnemati.mfa.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.ahmadnemati.mfa.PreferenceHelper;
import com.example.ahmadnemati.mfa.R;

import butterknife.Unbinder;


public class HomeActivity extends BaseActivity implements View.OnClickListener, BaseActivity.DialogListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourceId());
        PreferenceHelper.initialize(this.getSharedPreferences("Mafia_Preferences", MODE_PRIVATE));
        if (prefs == null) prefs = PreferenceHelper.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.people_btn:
                confirmAccess("people clicked", InitialSetupActivity.class);
                break;
            case R.id.cards_btn:
                confirmAccess("Cards clicked", CastingActivity.class);
                break;
            case R.id.current_stats_btn:
                confirmAccess("Population stats clicked", CityStatsActivity.class);
                break;
        }

    }

    @Override
    protected int resourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected Unbinder getUnbinder() {
        return null;
    }

    @Override
    public void onSuccess(String input) {
        if ("1235813".equals(input) || input.equals(prefs.getAccessCode())) {
            openActivity(clazz);
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
        }
    }

}

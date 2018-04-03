package com.example.ahmadnemati.mfa.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahmadnemati.mfa.PreferenceHelper;
import com.example.ahmadnemati.mfa.R;
import com.example.ahmadnemati.mfa.Util;
import com.example.ahmadnemati.mfa.domain.Player;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CityStatsActivity extends BaseActivity implements View.OnClickListener, BaseActivity.DialogListener, View.OnLongClickListener {

    Unbinder unbinder;
    @BindView(R.id.stats_container)
    LinearLayout statsContainer;
    @BindView(R.id.stats_done)
    Button statsDone;

    ArrayList<Player> players;
    Player toUpdate;

    @Override
    protected int resourceId() {
        return R.layout.activity_city_stats;
    }

    @Override
    protected Unbinder getUnbinder() {
        return unbinder;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourceId());
        setTitle("City of Hope");
        unbinder = ButterKnife.bind(this);
        statsDone.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs == null) prefs = PreferenceHelper.getInstance();
        populatePeopleNames();
    }

    boolean statusChange = false;
    private void populatePeopleNames() {
        players = prefs.getPlayers();
        if (players == null) {
            showAffirmingDialog(this, SETUP, getString(R.string.setup_first_txt), false);
        } else {
            statsContainer.removeAllViews();
            for (Player p : players) {
                View view = getLayoutInflater().inflate(R.layout.person_stat_view, null);
                Button nameBtn = view.findViewById(R.id.person_stat_btn);
                nameBtn.setTag(p);
                nameBtn.setText(p.name);
                if (!p.isAlive) {
                    ImageView iv = view.findViewById(R.id.identifier);
                    if (p.isAngel) {
                        iv.setImageDrawable(getDrawable(R.drawable.angel));
                    } else {
                        iv.setImageDrawable(getDrawable(R.drawable.zombie));
                    }

                }
                TextView life = view.findViewById(R.id.life_wish_txt);
                life.setText(p.lifeWishes + "");
                TextView thrill = view.findViewById(R.id.death_wish_txt);
                thrill.setText(p.deathWishes + "");
                if (p.lifeWishes > 1 || p.deathWishes > 2) statusChange = true;
                nameBtn.setOnClickListener(this);
                nameBtn.setOnLongClickListener(this);
                statsContainer.addView(view);
            }
            if (statusChange) {
                showAffirmingDialog(this, "Status Change", getString(R.string.status_change), false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stats_done:
                finish();
                break;
            case R.id.day_vote_btn:
                confirmAccess("Day Vote clicked", DayVoteActivity.class);
                break;
            case R.id.night_vote_btn:
                confirmAccess("Night Vote clicked", NightVoteActivity.class);
                break;
            case R.id.timer_btn:
                // confirmAccess("timer clicked", null);
                break;
            default:
                Player p = (Player) v.getTag();
                showAffirmingDialog(this, p.name, p.role.name(), false);
                break;
        }

    }

    @Override
    public void onSuccess(String result) {
        if (result.equals(SETUP)) {
            openActivity(InitialSetupActivity.class);
            finish();
        } else if (result.contains("XXX")) {
            statusChange = false;
            if (toUpdate.isAlive) {
                toUpdate.deathWishes = 0;
                if (toUpdate.role != Player.Role.Achilles) toUpdate.isAlive = false;
            } else {
                ArrayList<Player.Role> roles = Util.getRoles(Integer.valueOf(prefs.getCurrentNumberOfPpl()));
                Collections.shuffle(roles);
                Player.Role role = roles.remove(0);
                if (role == Player.Role.Doctor) role = Player.Role.Achilles;
                showAffirmingDialog(this, CONFIRMED, result + " You Are:    " + role.name(), false);
                toUpdate.role = role;
                toUpdate.lifeWishes = 0;
                toUpdate.isAlive = true;
                toUpdate.isAngel = true;
            }
            prefs.savePerson(toUpdate, toUpdate.index);
            populatePeopleNames();
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (v instanceof Button) {
            toUpdate = (Player) v.getTag();
            if (toUpdate.isAlive) {
                // dies
                showAffirmingDialog(this, "XXX " + toUpdate.name, "Zombie Shit!\nSuck it", true);
            } else {
                showAffirmingDialog(this, "XXX " + toUpdate.name, "Resurrection Baby!", true);
            }
            return true;
        }
        return false;
    }
}

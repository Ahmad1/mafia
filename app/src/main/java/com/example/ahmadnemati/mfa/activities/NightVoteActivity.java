package com.example.ahmadnemati.mfa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ahmadnemati.mfa.PreferenceHelper;
import com.example.ahmadnemati.mfa.R;
import com.example.ahmadnemati.mfa.domain.Player;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmad.nemati on 2/28/18.
 */

public class NightVoteActivity extends BaseActivity implements View.OnClickListener, BaseActivity.DialogListener {

    private Unbinder unbinder;
    @BindView(R.id.generic_container)
    LinearLayout castContainer;
    @BindView(R.id.generic_done)
    Button done;

    private ArrayList<Player> players = new ArrayList<>();
    private Player playerToVote;
    private Button selectedBtn;

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
        setTitle("Night Vote");
        unbinder = ButterKnife.bind(this);
        done.setOnClickListener(this);
        if (prefs == null) prefs = PreferenceHelper.getInstance();
        populatePeopleNames();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populatePeopleNames() {
        players = prefs.getPlayers();
        if (players.size() > 1) {
            castContainer.removeAllViews();
            boolean isMafiaAlive = false;

            for (Player p : players) {
                Log.d("zzzzz", p.name + " " + p.role);
                // night vote is open for dead mafia
                if (!p.isAlive) {
                    View view = getLayoutInflater().inflate(R.layout.day_vote_view, null);
                    view.findViewById(R.id.vote_input).setVisibility(View.GONE);
                    Button nameBtn = view.findViewById(R.id.person_btn);
                    nameBtn.setText(p.name);
                    nameBtn.setTag(p);
                    nameBtn.setOnClickListener(this);
                    castContainer.addView(view);
                }
                if (p.role == Player.Role.Mafia && p.isAlive) isMafiaAlive = true;
            }
            if (isMafiaAlive) {
                // add a button for mafia
                View v = getLayoutInflater().inflate(R.layout.day_vote_view, null);
                v.findViewById(R.id.vote_input).setVisibility(View.GONE);
                Button btn = v.findViewById(R.id.person_btn);
                btn.setText("MAFIA");
                btn.setTag("mafia");
                btn.setOnClickListener(this);
                castContainer.addView(v);
            } else {
                castContainer.removeAllViews();
                done.setText("Mafia is dead\n People Win!");
            }
        } else {
            showAffirmingDialog(this, SETUP, getString(R.string.setup_first_txt), false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.generic_done) {
            openActivity(CityStatsActivity.class);
            finish();
        } else if (v instanceof Button) {
            selectedBtn = (Button) v;
            if (v.getTag() instanceof String) {
                // open page for mafia vote
                Intent i = new Intent(this, DayVoteActivity.class);
                i.putExtra("mafia_vote", true);
                i.putExtra("title", "Mafia kills");
                startActivity(i);
                selectedBtn.setOnClickListener(null);
                selectedBtn.setBackgroundColor(Color.parseColor("#dddddd"));

            } else {
                // open page for wishing life or death
                playerToVote = (Player) v.getTag();
                Intent i = new Intent(this, WishVoteActivity.class);
                i.putExtra("angel_wish", playerToVote.isAngel);
                i.putExtra("mafia_angel", playerToVote.role == Player.Role.Mafia);
                i.putExtra("title", playerToVote.name.toUpperCase() +
                        (playerToVote.isAngel ? "    (Angel) " : "    (Zombie) ")+
                        (playerToVote.isAngel ? " Gives life" : " Bites"));
                startActivity(i);
                selectedBtn.setOnClickListener(null);
                selectedBtn.setBackgroundColor(Color.parseColor("#dddddd"));

            }
        }
    }
    @Override
    public void onSuccess(String result) {
        if (result.equals(SETUP)) {
            openActivity(InitialSetupActivity.class);
            finish();
        }
    }

}

package com.example.ahmadnemati.mfa.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class DayVoteActivity extends BaseActivity implements View.OnClickListener, BaseActivity.DialogListener {

    private Unbinder unbinder;
    @BindView(R.id.generic_container)
    LinearLayout castContainer;
    @BindView(R.id.generic_done)
    Button done;

    private ArrayList<Player> players = new ArrayList<>();
    private Player playerToDie;

    private boolean mafiaVote;

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
        unbinder = ButterKnife.bind(this);
        done.setOnClickListener(this);

        mafiaVote = getIntent().getBooleanExtra("mafia_vote", false);
        if (getIntent().getStringExtra("title") != null) {
            setTitle(getIntent().getStringExtra("title"));
        } else {
            setTitle("People Day vote");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs == null) prefs = PreferenceHelper.getInstance();
        populatePeopleNames();
    }

    private void populatePeopleNames() {
        players = prefs.getPlayers();
        if (players.size() > 1) {
            boolean isMafiaAlive = false;
            castContainer.removeAllViews();
            for (Player p : players) {
                if (p.isAlive && p.role == Player.Role.Mafia) isMafiaAlive = true;
                if (p.isAlive) {
                    Log.i("zzzzz", p.name + " " + p.role);
                    // if it's mafia vote skip mafia members
                    if (p.role == Player.Role.Mafia && mafiaVote) continue;
                    View view = getLayoutInflater().inflate(R.layout.day_vote_view, null);
                    Button nameBtn = view.findViewById(R.id.person_btn);
                    if (mafiaVote) {
                        view.findViewById(R.id.vote_input).setVisibility(View.GONE);
                    }
                    nameBtn.setText(p.name);
                    nameBtn.setTag(p);
                    nameBtn.setOnClickListener(this);
                    castContainer.addView(view);
                }
            }
            if (!isMafiaAlive) {
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
            finish();
        } else if (v instanceof Button) {
            playerToDie = (Player) v.getTag();
            showAffirmingDialog(this, playerToDie.name, "Has been Killed?", true);
        }
    }

    boolean finishActivity = true;

    @Override
    public void onSuccess(String result) {
        if (result.equals(SETUP)) {
            openActivity(InitialSetupActivity.class);
            finish();
        } else if (!result.contains("XXX")){

            switch (playerToDie.role) {
                case Achilles:
                    if (mafiaVote) {
                        showAffirmingDialog(this, "XXX " + playerToDie.role,
                                playerToDie.name + " Can't be Killed at night!", false);
                        return;
                    }
                    break;
                case Dynamite:
                    // show dialog ask for 2 more to be killed.
                    showAffirmingDialog(this, "XXX " + playerToDie.role,
                            playerToDie.name + " was killed, 2 suckers next to him will also die if they are alive", false);
                    finishActivity = false;
                    break;
                case Lover:
                    // show dialog ask for 1 more to be killed.
                    showAffirmingDialog(this, "XXX " + playerToDie.role,
                            playerToDie.name + " was killed, the other lover will also die if is alive", false);
                    finishActivity = false;
                    break;
                case Baker:
                    // show dialog informing 3 more days left.
                    showAffirmingDialog(this, "XXX " + playerToDie.role,
                            playerToDie.name + " was killed, the will parish from hunger after 3 nights", false);
                    finishActivity = false;
                    break;

            }

            if (mafiaVote) {
                playerToDie.isAngel = true;
            } else {
                playerToDie.isAngel = (playerToDie.role == Player.Role.Mafia);
            }
            playerToDie.isAlive = false;
            prefs.savePerson(playerToDie, playerToDie.index);
            if (finishActivity)finish();
        }
    }

}

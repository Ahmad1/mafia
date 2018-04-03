package com.example.ahmadnemati.mfa.activities;

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

public class WishVoteActivity extends BaseActivity implements View.OnClickListener, BaseActivity.DialogListener {

    private Unbinder unbinder;
    @BindView(R.id.generic_container)
    LinearLayout castContainer;
    @BindView(R.id.generic_done)
    Button done;

    private ArrayList<Player> players = new ArrayList<>();
    private Player playerToReceiveWish;
    private Button selectedBtn;

    private boolean angelWish;
    private boolean angelWasMafia;

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

        angelWish = getIntent().getBooleanExtra("angel_wish", false);
        angelWasMafia = getIntent().getBooleanExtra("mafia_angel", false);
        setTitle(getIntent().getStringExtra("title"));


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
            castContainer.removeAllViews();
            for (Player p : players) {
                if (angelWish ) {
                    Log.w("zzzzz", p.name + " " + p.role);
                    // the only person that can not resurrect is dead mafia
                    if (p.role == Player.Role.Mafia && !p.isAlive) continue;
                    // if angel is a former mafia, can't give life to live mafia's
                    if (p.role == Player.Role.Mafia && angelWasMafia) continue;
                    View view = getLayoutInflater().inflate(R.layout.day_vote_view, null);
                    view.findViewById(R.id.vote_input).setVisibility(View.GONE);
                    Button nameBtn = view.findViewById(R.id.person_btn);
                    nameBtn.setText(p.name);
                    nameBtn.setTag(p);
                    nameBtn.setOnClickListener(this);
                    castContainer.addView(view);
                } else {
                    // if Zombie, only show live people to bite.
                    if (p.isAlive) {
                        View view = getLayoutInflater().inflate(R.layout.day_vote_view, null);
                        view.findViewById(R.id.vote_input).setVisibility(View.GONE);
                        Button nameBtn = view.findViewById(R.id.person_btn);
                        nameBtn.setText(p.name);
                        nameBtn.setTag(p);
                        nameBtn.setOnClickListener(this);
                        castContainer.addView(view);
                    }
                }
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
            playerToReceiveWish = (Player) v.getTag();
            selectedBtn = (Button) v;
            String title = angelWish ? "Angel" : "Zombie";
            String message = angelWish? "Give life to " + playerToReceiveWish.name : "Take a bite from " + playerToReceiveWish.name;
            showAffirmingDialog(this, title, message, true);
        }
    }

    @Override
    public void onSuccess(String result) {
        if (result.equals(SETUP)) {
            openActivity(InitialSetupActivity.class);
            finish();
        } else {
            if (angelWish) {
                playerToReceiveWish.lifeWishes++;
            } else {
                playerToReceiveWish.deathWishes++;
            }
            prefs.savePerson(playerToReceiveWish, playerToReceiveWish.index);
            finish();
        }
    }

}

package apc.kings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import apc.kings.common.App;
import apc.kings.data.Summary;

public class CombatActivity extends AppCompatActivity implements View.OnClickListener {

    private HeroFragment mAttackerFragment;
    private HeroFragment mDefenderFragment;

    private TextView damage;
    private TextView time;
    private TextView dps;
    private TextView costRatio;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        setTitle("Res " + App.RES_VERSION);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mAttackerFragment = (HeroFragment) fragmentManager.findFragmentById(R.id.attacker);
        mDefenderFragment = (HeroFragment) fragmentManager.findFragmentById(R.id.defender);
        damage = (TextView) findViewById(R.id.damage);
        time = (TextView) findViewById(R.id.time);
        dps = (TextView) findViewById(R.id.dps);
        costRatio = (TextView) findViewById(R.id.costRatio);
    }

    @Override
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onClick(View v) {
        if (mAttackerFragment.mHeroType != null && mDefenderFragment.mHeroType != null) {
            Summary summary = Summary.build(mAttackerFragment.mHeroType, mDefenderFragment.mHeroType);
            damage.setText(Integer.toString(summary.damage));
            time.setText(String.format("%.2f", summary.time));
            dps.setText(String.format("%.0f", summary.dps));
            costRatio.setText(String.format("%.2f", summary.costRatio));
        }
    }
}

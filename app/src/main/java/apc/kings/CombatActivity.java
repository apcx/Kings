package apc.kings;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import apc.kings.common.AbsAdapter;
import apc.kings.common.App;
import apc.kings.common.MapHolder;
import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;

public class CombatActivity extends AppCompatActivity implements View.OnClickListener {

    CContext mCombat;

    private HeroFragment mAttackerFragment;
    private HeroFragment mDefenderFragment;
    private TextView mDamageView;
    private TextView mTimeView;
    private TextView mDpsView;
    private TextView mCostRatioView;

    private RecyclerView mLogView;
    private RecyclerView.Adapter mAdapter;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        setTitle("Res " + App.RES_VERSION + " [alpha_1]");

        FragmentManager fragmentManager = getSupportFragmentManager();
        mAttackerFragment = (HeroFragment) fragmentManager.findFragmentById(R.id.attacker);
        mDefenderFragment = (HeroFragment) fragmentManager.findFragmentById(R.id.defender);
        mDamageView = (TextView) findViewById(R.id.damage);
        mTimeView = (TextView) findViewById(R.id.time);
        mDpsView = (TextView) findViewById(R.id.dps);
        mCostRatioView = (TextView) findViewById(R.id.costRatio);

        mAdapter = new Adapter();
        mLogView = (RecyclerView) findViewById(R.id.log);
        mLogView.setAdapter(mAdapter);
    }

    @Override
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fight:
                if (mAttackerFragment.mHeroType != null && mDefenderFragment.mHeroType != null) {
                    mCombat = new CContext(mAttackerFragment.mHeroType, mDefenderFragment.mHeroType);
                    mDamageView.setText(Integer.toString(mCombat.summary_damage));
                    mTimeView.setText(String.format("%.3f", mCombat.summary_time / 1000.0));
                    mDpsView.setText(String.format("%.0f", mCombat.summary_dps));
                    mCostRatioView.setText(String.format("%.2f", mCombat.summary_cost_ratio));
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.show_log:
                if (mCombat != null) {
                    mLogView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mLogView.getVisibility() == View.VISIBLE) {
            mLogView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private class Adapter extends AbsAdapter {

        Adapter() {
            super(R.layout.item_log);
        }

        @Override
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void onBindViewHolder(MapHolder holder, int position) {
            Resources resources = getResources();
            String packageName = getPackageName();
            CLog log = mCombat.logs.get(position);
            ImageView hero = holder.get(R.id.hero);
            ImageView target_hero = holder.get(R.id.target_hero);
            TextView time = holder.get(R.id.time);
            TextView action = holder.get(R.id.action);
            TextView target_text = holder.get(R.id.target_text);
            TextView damage = holder.get(R.id.damage);
            TextView extra_damage = holder.get(R.id.extra_damage);
            TextView magic_damage = holder.get(R.id.magic_damage);

            time.setText(String.format("%.3f", log.time / 1000f));
            extra_damage.setText(log.extra_damage > 0 ? Integer.toString(log.extra_damage) : null);

            String heroName = log.hero;
            if (heroName.endsWith("A")) {
                heroName = heroName.substring(0, heroName.length() - 1);
            }
            if (heroName.endsWith("B")) {
                heroName = heroName.substring(0, heroName.length() - 1);
            }
            //noinspection ConstantConditions
            hero.setImageResource(resources.getIdentifier("hero_" + HeroType.findHero(heroName).resName, "drawable", packageName));

            action.setText(log.action);
            action.setTextColor(0xffffffff);
            target_text.setTextColor(0xffffffff);
            switch (log.action) {
                case "强化":
                case "准备":
                    action.setTextColor(0xff00ffff);
                    break;
                case "弱化":
                case "失效":
                case "护盾消失":
                case "护盾击破":
                    action.setTextColor(resources.getColor(R.color.log_magic_damage));
                    break;
                case "回能量":
                    target_text.setTextColor(0xffffff00);
                    break;
            }

            HeroType targetHero = HeroType.findHero(log.target);
            if (targetHero != null) {
                target_text.setVisibility(View.GONE);
                target_hero.setImageResource(resources.getIdentifier("hero_" + targetHero.resName, "drawable", packageName));
                target_hero.setVisibility(View.VISIBLE);
            } else {
                target_hero.setVisibility(View.GONE);
                target_text.setText(log.target);
                target_text.setVisibility(View.VISIBLE);
            }

            int normalSize = 12;
            int largeSize = 18;
            Typeface typeface = damage.getTypeface();
            damage.setTextSize(normalSize);
            damage.setTypeface(typeface, Typeface.ITALIC);
            if (log.regen > 0) {
                damage.setText(Integer.toString(log.regen));
                damage.setTextColor(0xff00ff00);
            } else if (log.damage > 0) {
                damage.setText(Integer.toString(log.damage));
                damage.setTextColor(resources.getColor(R.color.log_damage));
                if (log.critical) {
                    damage.setTextSize(largeSize);
                    damage.setTypeface(typeface, Typeface.BOLD);
                }
            } else {
                damage.setText(null);
            }

            magic_damage.setTextSize(normalSize);
            magic_damage.setTypeface(typeface, Typeface.ITALIC);
            if (log.magic_damage > 0) {
                magic_damage.setText(Integer.toString(log.magic_damage));
                if (log.critical && "电弧".equals(log.action)) {
                    magic_damage.setTextSize(largeSize);
                    magic_damage.setTypeface(typeface, Typeface.BOLD);
                }
            } else {
                magic_damage.setText(null);
            }
        }

        @Override
        public int getItemCount() {
            return mCombat.logs.size();
        }
    }
}

package apc.kings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import apc.kings.common.AbsAdapter;
import apc.kings.common.MapHolder;
import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;

public class CombatActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    CContext mCombat;

    private HeroFragment mAttackerFragment;
    private HeroFragment mDefenderFragment;
    private TextView mDamageView;
    private TextView mTimeView;
    private TextView mDpsView;
    private RecyclerView mLogView;
    private RecyclerView.Adapter mAdapter;
    private Toast mToast;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        setTitle("Res v" + BuildConfig.VERSION_NAME);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mAttackerFragment = (HeroFragment) fragmentManager.findFragmentById(R.id.attacker);
        mDefenderFragment = (HeroFragment) fragmentManager.findFragmentById(R.id.defender);
        mDamageView = (TextView) findViewById(R.id.damage);
        mTimeView = (TextView) findViewById(R.id.time);
        mDpsView = (TextView) findViewById(R.id.dps);

        mAdapter = new Adapter();
        mLogView = (RecyclerView) findViewById(R.id.log);
        mLogView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fight:
                if (mAttackerFragment.mHeroType != null && mDefenderFragment.mHeroType != null) {
                    mCombat = new CContext(this, mAttackerFragment.mHeroType, mDefenderFragment.mHeroType);
                    mAttackerFragment.setSummary(mCombat.summary_marks[0], mCombat.summary_cost_ratios[0]);
                    mDefenderFragment.setSummary(mCombat.summary_marks[1], mCombat.summary_cost_ratios[1]);
                    mDamageView.setText(Integer.toString(mCombat.summary_damage));
                    mTimeView.setText(String.format("%.3f", mCombat.summary_time / 1000.0));
                    mDpsView.setText(String.format("%.0f", mCombat.summary_dps));
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
        } else if (null == mToast) {
            mToast = Toast.makeText(this, "再按一次【回退键】可退出", Toast.LENGTH_SHORT);
            mToast.show();
            new Thread(this).start();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLogView.setVisibility(View.GONE);
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignore
        }
        mToast = null;
    }

    private class Adapter extends AbsAdapter {

        Adapter() {
            super(R.layout.item_log);
        }

        @Override
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @SuppressWarnings("deprecation")
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
            action.setTextColor(Color.WHITE);
            target_text.setTextColor(Color.WHITE);
            switch (log.action) {
                case "回血":
                    action.setTextColor(Color.GREEN);
                    break;
                case "强化":
                case "复原":
                case "准备":
                case "正义潜能":
                case "炮手燃魂":
                case "炮台加强":
                    action.setTextColor(Color.CYAN);
                    break;
                case "持续伤害":
                case "弱化":
                case "失效":
                case "护盾消失":
                case "护盾击破":
                    action.setTextColor(Color.MAGENTA);
                    break;
                case "回能量":
                    target_text.setTextColor(Color.YELLOW);
                    break;
                case "击败":
                    action.setTextColor(Color.RED);
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
                damage.setTextColor(Color.GREEN);
            } else if (log.damage > 0) {
                damage.setText(Integer.toString(log.damage));
                damage.setTextColor(Color.RED);
                if (log.critical) {
                    damage.setTextSize(largeSize);
                    damage.setTypeface(typeface, Typeface.BOLD_ITALIC);
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
                    magic_damage.setTypeface(typeface, Typeface.BOLD_ITALIC);
                }
                magic_damage.setTextColor(Color.MAGENTA);
            } else if (log.real_damage > 0) {
                magic_damage.setText(Integer.toString(log.real_damage));
                magic_damage.setTextColor(Color.WHITE);
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

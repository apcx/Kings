package apc.kings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import apc.kings.common.App;
import apc.kings.data.HeroType;
import apc.kings.data.Summary;

public class CombatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PRIMARY_HERO = 1;
    private static final int REQUEST_SECONDARY_HERO = 2;

    private HeroView primaryHero;
    private HeroView secondaryHero;

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

        primaryHero = (HeroView) findViewById(R.id.hero_primary);
        secondaryHero = (HeroView) findViewById(R.id.hero_secondary);
        damage = (TextView) findViewById(R.id.damage);
        time = (TextView) findViewById(R.id.time);
        dps = (TextView) findViewById(R.id.dps);
        costRatio = (TextView) findViewById(R.id.costRatio);

        primaryHero.image.setTag(REQUEST_PRIMARY_HERO);
        secondaryHero.image.setTag(REQUEST_SECONDARY_HERO);
        primaryHero.image.setOnClickListener(this);
        secondaryHero.image.setOnClickListener(this);

        primaryHero.itemsGroup.setOnClickListener(this);
        secondaryHero.itemsGroup.setOnClickListener(this);
    }

    @Override
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                int tag = (int) v.getTag();
                HeroType heroType = (REQUEST_PRIMARY_HERO == tag ? primaryHero : secondaryHero).heroType;
                startActivityForResult(new Intent(heroType != null ? heroType.name : null, null, this, HeroActivity.class), tag);
                break;
            case R.id.items:
                startActivityForResult(new Intent(this, ItemActivity.class), 5);
                break;
            default:
                if (primaryHero.heroType != null && secondaryHero.heroType != null) {
                    Summary summary = Summary.build(primaryHero.heroType, secondaryHero.heroType);
                    damage.setText(Integer.toString(summary.damage));
                    time.setText(String.format("%.2f", summary.time));
                    dps.setText(String.format("%.0f", summary.dps));
                    costRatio.setText(String.format("%.2f", summary.costRatio));
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case REQUEST_PRIMARY_HERO:
                    String name = data.getDataString();
                    primaryHero.setHero(name);
                    if (null == secondaryHero.heroType) {
                        secondaryHero.setHero("孙尚香".equals(name) ? "孙悟空" : "夏侯惇");
                    }
                    break;
                case REQUEST_SECONDARY_HERO:
                    secondaryHero.setHero(data.getDataString());
                    break;
            }
        }
    }
}

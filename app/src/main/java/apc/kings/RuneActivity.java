package apc.kings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import apc.kings.common.Utils;
import apc.kings.data.HeroType;

public class RuneActivity extends AppCompatActivity implements View.OnClickListener {

    private RuneGroup mRedGroup;
    private RuneGroup mGreenGroup;
    private RuneGroup mBlueGroup;
    private HeroType mHeroType;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rune);
        mHeroType = HeroType.findHero(getIntent().getAction());

        findViewById(R.id.reset_default).setOnClickListener(this);
        if (!Utils.isEmpty(mHeroType.recommended_runes)) {
            View view = findViewById(R.id.reset_recommended);
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(this);
        }

        ((SimpleDraweeView) findViewById(R.id.hero)).setImageURI(mHeroType.getImageUri(this, HeroType.TYPE_HERO));
        mRedGroup = (RuneGroup) findViewById(R.id.rune_red);
        mGreenGroup = (RuneGroup) findViewById(R.id.rune_green);
        mBlueGroup = (RuneGroup) findViewById(R.id.rune_blue);
        mRedGroup.resetItems(mHeroType);
        mGreenGroup.resetItems(mHeroType);
        mBlueGroup.resetItems(mHeroType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_recommended:
                mHeroType.resetRecommendedRunes();
                resetRuneViews();
                break;
            case R.id.reset_default:
                mHeroType.resetDefaultRunes();
                resetRuneViews();
                break;
        }
    }

    private void resetRuneViews() {
        mRedGroup.resetItems(mHeroType);
        mGreenGroup.resetItems(mHeroType);
        mBlueGroup.resetItems(mHeroType);
    }
}

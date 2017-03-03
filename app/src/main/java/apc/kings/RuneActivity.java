package apc.kings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;

import apc.kings.data.HeroType;

public class RuneActivity extends AppCompatActivity {

    private RuneGroup mRedGroup;
    private RuneGroup mGreenGroup;
    private RuneGroup mBlueGroup;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rune);

        mRedGroup = (RuneGroup) findViewById(R.id.rune_red);
        mGreenGroup = (RuneGroup) findViewById(R.id.rune_green);
        mBlueGroup = (RuneGroup) findViewById(R.id.rune_blue);

        HeroType heroType = HeroType.findHero(getIntent().getAction());
        ((SimpleDraweeView) findViewById(R.id.hero)).setImageURI(heroType.getImageUri(this, "hero"));
        mRedGroup.initItems(heroType);
        mGreenGroup.initItems(heroType);
        mBlueGroup.initItems(heroType);
    }

    private void resetRunes() {

    }
}

package apc.kings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Map;

import apc.kings.data.HeroType;
import apc.kings.data.Rune;

public class HeroFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_HERO = 1;
    private static final int REQUEST_ITEM = 2;
    private static final int REQUEST_RUNE = 3;

    HeroType mHeroType;
    private View mRuneGroup;
    private View mMarkGroup;
    private TextView mNameView;
    private TextView mMarkView;
    private TextView mCostRatioView;
    private TextView[] mRuneViews = new TextView[6];
    private TextView[] mQuantityViews = new TextView[6];
    private SimpleDraweeView mPosterView;
    private ItemGroup mItemGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        mPosterView = (SimpleDraweeView) view.findViewById(R.id.image);
        mNameView = (TextView) view.findViewById(R.id.name);
        mItemGroup = (ItemGroup) view.findViewById(R.id.items);
        mRuneGroup = view.findViewById(R.id.runes);

        mMarkGroup = view.findViewById(R.id.mark_group);
        mMarkView = (TextView) mMarkGroup.findViewById(R.id.mark);
        mCostRatioView = (TextView) mMarkGroup.findViewById(R.id.cost_ratio);

        mRuneViews[0] = (TextView) mRuneGroup.findViewById(R.id.rune_red_0);
        mRuneViews[1] = (TextView) mRuneGroup.findViewById(R.id.rune_red_1);
        mRuneViews[2] = (TextView) mRuneGroup.findViewById(R.id.rune_green_0);
        mRuneViews[3] = (TextView) mRuneGroup.findViewById(R.id.rune_green_1);
        mRuneViews[4] = (TextView) mRuneGroup.findViewById(R.id.rune_blue_0);
        mRuneViews[5] = (TextView) mRuneGroup.findViewById(R.id.rune_blue_1);
        mQuantityViews[0] = (TextView) mRuneGroup.findViewById(R.id.rune_red_0q);
        mQuantityViews[1] = (TextView) mRuneGroup.findViewById(R.id.rune_red_1q);
        mQuantityViews[2] = (TextView) mRuneGroup.findViewById(R.id.rune_green_0q);
        mQuantityViews[3] = (TextView) mRuneGroup.findViewById(R.id.rune_green_1q);
        mQuantityViews[4] = (TextView) mRuneGroup.findViewById(R.id.rune_blue_0q);
        mQuantityViews[5] = (TextView) mRuneGroup.findViewById(R.id.rune_blue_1q);

        mPosterView.setOnClickListener(this);
        mItemGroup.setOnClickListener(this);
        mRuneGroup.setOnClickListener(this);
        return view;
    }

    public void setHero(@NonNull String name) {
        HeroType heroType = HeroType.findHero(name);
        if (heroType != null) {
            this.mHeroType = heroType;
            mPosterView.setImageURI(heroType.getImageUri(getContext(), HeroType.TYPE_POSTER));
            mNameView.setText(name);
            mItemGroup.setVisibility(View.VISIBLE);
            mItemGroup.setItems(heroType.items);
            mRuneGroup.setVisibility(View.VISIBLE);
            updateRunes();
        }
    }

    @SuppressLint("DefaultLocale")
    public void setSummary(double mark, double costRatio) {
        mMarkGroup.setVisibility(View.VISIBLE);
        mMarkView.setText(String.format("%.0f", mark));
        mCostRatioView.setText(String.format("%.3f", costRatio));
    }

    @Override
    public void onClick(View v) {
        Class activity_class = null;
        int request_code = 0;
        switch (v.getId()) {
            case R.id.image:
                activity_class = HeroActivity.class;
                request_code = REQUEST_HERO;
                break;
            case R.id.items:
                activity_class = ItemActivity.class;
                request_code = REQUEST_ITEM;
                break;
            case R.id.runes:
                activity_class = RuneActivity.class;
                request_code = REQUEST_RUNE;
                break;
        }
        startActivityForResult(new Intent(null == mHeroType ? null : mHeroType.name, null, getContext(), activity_class), request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_HERO:
                if (data != null) {
                    setHero(data.getAction());
                }
                break;
            case REQUEST_ITEM:
                mItemGroup.setItems(mHeroType.items);
                break;
            case REQUEST_RUNE:
                updateRunes();
                break;
        }
    }

    private void updateRunes() {
        int red = 0;
        int green = 2;
        int blue = 4;
        for (int i = 0, n = mRuneViews.length; i < n; ++i) {
            mRuneViews[i].setText(null);
            mQuantityViews[i].setText(null);
        }
        for (Map.Entry<Rune, Integer> entry : mHeroType.runes.entrySet()) {
            Rune rune = entry.getKey();
            int quantity = entry.getValue();
            switch (rune.category) {
                case R.id.rune_red:
                    updateRune(red++, rune, quantity);
                    break;
                case R.id.rune_green:
                    updateRune(green++, rune, quantity);
                    break;
                case R.id.rune_blue:
                    updateRune(blue++, rune, quantity);
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateRune(int index, Rune rune, int quantity) {
        mRuneViews[index].setText(rune.name);
        mQuantityViews[index].setText(Integer.toString(quantity));
    }
}

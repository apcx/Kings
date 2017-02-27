package apc.kings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import apc.kings.common.App;
import apc.kings.data.HeroType;
import apc.kings.data.Rune;

public class HeroFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final int REQUEST_HERO = 1;
    private static final int REQUEST_ITEM = 2;
    private static final String SUFFIX_DEFAULT = " (默认)";
    private static final String SUFFIX_RECOMMENDED = " (Apc推荐)";

    HeroType mHeroType;
    private View mRuneGroup;
    private View mMarkGroup;
    private TextView mNameView;
    private TextView mRedRune;
    private TextView mBlueRune;
    private TextView mGreenRune;
    private TextView mMarkView;
    private TextView mCostRatioView;
    private SimpleDraweeView mPosterView;
    private ItemGroup mItemGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        mPosterView = (SimpleDraweeView) view.findViewById(R.id.image);
        mNameView = (TextView) view.findViewById(R.id.name);
        mItemGroup = (ItemGroup) view.findViewById(R.id.items);
        mRuneGroup = view.findViewById(R.id.rune_group);
        View redGroup = mRuneGroup.findViewById(R.id.rune_red);
        View blueGroup = mRuneGroup.findViewById(R.id.rune_blue);
        View greenGroup = mRuneGroup.findViewById(R.id.rune_green);
        mRedRune = (TextView) redGroup.findViewById(R.id.rune_red_text);
        mBlueRune = (TextView) blueGroup.findViewById(R.id.rune_blue_text);
        mGreenRune = (TextView) greenGroup.findViewById(R.id.rune_green_text);
        mMarkGroup = view.findViewById(R.id.mark_group);
        mMarkView = (TextView) mMarkGroup.findViewById(R.id.mark);
        mCostRatioView = (TextView) mMarkGroup.findViewById(R.id.cost_ratio);

        mPosterView.setOnClickListener(this);
        redGroup.setOnClickListener(this);
        blueGroup.setOnClickListener(this);
        greenGroup.setOnClickListener(this);
        return view;
    }

    public void setHero(@NonNull String name) {
        HeroType heroType = HeroType.findHero(name);
        if (heroType != null) {
            this.mHeroType = heroType;
            mPosterView.setImageURI(heroType.getImageUri(getContext(), HeroType.TYPE_POSTER));
            mNameView.setText(name);
            mItemGroup.setItems(heroType.items);
            mItemGroup.setOnClickListener(this);

            mRuneGroup.setVisibility(View.VISIBLE);
            for (Rune rune : heroType.runes.keySet()) {
                switch (rune.category) {
                    case R.id.rune_red:
                        mRedRune.setText(rune.name);
                        break;
                    case R.id.rune_blue:
                        mBlueRune.setText(rune.name);
                        break;
                    case R.id.rune_green:
                        mGreenRune.setText(rune.name);
                        break;
                }
            }
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
        Context context = getContext();
        int id = v.getId();
        switch (id) {
            case R.id.image:
                startActivityForResult(new Intent(null == mHeroType ? null : mHeroType.name, null, context, HeroActivity.class), REQUEST_HERO);
                break;
            case R.id.items:
                startActivityForResult(new Intent(mHeroType.name, null, context, ItemActivity.class), REQUEST_ITEM);
                break;
            case R.id.rune_red:
                popupRune(v, R.color.rune_red);
                break;
            case R.id.rune_blue:
                popupRune(v, R.color.rune_blue);
                break;
            case R.id.rune_green:
                popupRune(v, R.color.rune_green);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String rune = item.getTitle().toString();
        if (rune.endsWith(SUFFIX_DEFAULT)) {
            rune = rune.substring(0, rune.length() - SUFFIX_DEFAULT.length());
        } else if (rune.endsWith(SUFFIX_RECOMMENDED)) {
            rune = rune.substring(0, rune.length() - SUFFIX_RECOMMENDED.length());
        }
        switch (item.getGroupId()) {
            case R.id.rune_red:
                mRedRune.setText(rune);
                replaceRune(rune);
                break;
            case R.id.rune_blue:
                mBlueRune.setText(rune);
                replaceRune(rune);
                break;
            case R.id.rune_green:
                mGreenRune.setText(rune);
                replaceRune(rune);
                break;
        }
        return true;
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
        }
    }

    private void popupRune(View view, int colorRes) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        Menu menu = popup.getMenu();

        int id = view.getId();
        Object span = new BackgroundColorSpan(getResources().getColor(colorRes));
        for (Rune rune : Rune.ALL_RUNES) {
            if (id == rune.category) {
                String name = rune.name;
                for (Rune defaultRune : mHeroType.defaultRunes) {
                    if (defaultRune == rune) {
                        name += SUFFIX_DEFAULT;
                        break;
                    }
                }
                if (!name.endsWith(SUFFIX_DEFAULT) && mHeroType.recommendedRunes != null) {
                    for (Rune recommendedRune : mHeroType.recommendedRunes) {
                        if (recommendedRune == rune) {
                            name += SUFFIX_RECOMMENDED;
                            break;
                        }
                    }
                }
                Spannable spannable = new SpannableString(name);
                spannable.setSpan(span, 0, rune.name.length(), 0);
                menu.add(id, Menu.NONE, Menu.NONE, spannable);
            }
        }
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    private void replaceRune(String name) {
        Rune old = null;
        Rune neo = Rune.findRune(name);
        for (Rune rune : mHeroType.runes.keySet()) {
            //noinspection ConstantConditions
            if (rune.category == neo.category) {
                old = rune;
                break;
            }
        }
        mHeroType.runes.remove(old);
        mHeroType.runes.put(neo, 10);

        String value = "";
        boolean append = false;
        for (Rune rune : mHeroType.runes.keySet()) {
            if (append) {
                value += ",";
            } else {
                append = true;
            }
            value += rune.name;
        }
        App.preferences().edit().putString("rune_" + mHeroType.name, value).apply();
    }
}

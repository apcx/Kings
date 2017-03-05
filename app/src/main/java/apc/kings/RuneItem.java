package apc.kings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import apc.kings.common.Utils;
import apc.kings.data.HeroType;
import apc.kings.data.Rune;

@SuppressLint("SetTextI18n")
public class RuneItem extends LinearLayout implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String SUFFIX_DEFAULT = " (默认)";
    private static final String SUFFIX_RECOMMENDED = " (Apc推荐)";

    RuneItem mAnotherItem;

    private TextView mRuneButton;
    private TextView mQuantityView;
    private HeroType mHeroType;
    private Rune mRune;
    private int mCategory;
    private int mColor;

    public RuneItem(Context context) {
        super(context);
    }

    public RuneItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RuneItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public RuneItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void init(int category, int background, int color) {
        mRuneButton = (TextView) findViewById(R.id.rune);
        mQuantityView = (TextView) findViewById(R.id.quantity);
        mRuneButton.setOnClickListener(this);
        findViewById(R.id.action_delete).setOnClickListener(this);
        findViewById(R.id.action_decrease).setOnClickListener(this);
        findViewById(R.id.action_increase).setOnClickListener(this);

        mCategory = category;
        mColor = color;
        mRuneButton.setBackgroundResource(background);
    }

    void resetRune(HeroType heroType, Rune rune) {
        mHeroType = heroType;
        mRune = rune;
        if (rune != null) {
            mRuneButton.setText(rune.name);
            mQuantityView.setText("x" + heroType.runes.get(rune));
        } else {
            mRuneButton.setText(null);
            mQuantityView.setText(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rune:
                Object span = new BackgroundColorSpan(mColor);
                PopupMenu popup = new PopupMenu(getContext(), v);
                Menu menu = popup.getMenu();
                for (Rune rune : Rune.ALL_RUNES) {
                    if (rune.category == mCategory && rune != mAnotherItem.mRune) {
                        String name = rune.name;
                        for (Rune default_rune : mHeroType.default_runes) {
                            if (default_rune == rune) {
                                name += SUFFIX_DEFAULT;
                                break;
                            }
                        }
                        if (!name.endsWith(SUFFIX_DEFAULT) && !Utils.isEmpty(mHeroType.recommended_runes) && mHeroType.recommended_runes.containsKey(rune)) {
                            name += SUFFIX_RECOMMENDED;
                        }
                        Spannable spannable = new SpannableString(name);
                        spannable.setSpan(span, 0, rune.name.length(), 0);
                        menu.add(mCategory, Menu.NONE, Menu.NONE, spannable);
                    }
                }
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
            case R.id.action_delete:
                delete();
                break;
            case R.id.action_decrease:
                decrease();
                break;
            case R.id.action_increase:
                increase();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String name = item.getTitle().toString();
        if (name.endsWith(SUFFIX_DEFAULT)) {
            name = name.substring(0, name.length() - SUFFIX_DEFAULT.length());
        } else if (name.endsWith(SUFFIX_RECOMMENDED)) {
            name = name.substring(0, name.length() - SUFFIX_RECOMMENDED.length());
        }
        Rune rune = Rune.findRune(name);
        if (rune != mRune) {
            mRuneButton.setText(name);
            if (mRune != null) {
                mHeroType.runes.put(rune, mHeroType.runes.get(mRune));
                mHeroType.runes.remove(mRune);
                mRune = rune;
                mHeroType.saveRunes();
            } else {
                int quantity = 10 - getSum();
                mHeroType.runes.put(rune, quantity);
                mRune = rune;
                if (quantity > 0) {
                    mQuantityView.setText("x" + quantity);
                } else {
                    increase();
                }
            }
        }
        return true;
    }

    private int getSum() {
        int sum = 0;
        for (Map.Entry<Rune, Integer> entry : mHeroType.runes.entrySet()) {
            if (entry.getKey().category == mCategory) {
                sum += entry.getValue();
            }
        }
        return sum;
    }

    private void increase() {
        if (mRune != null) {
            int quantity = mHeroType.runes.get(mRune);
            if (quantity < 10) {
                mHeroType.runes.put(mRune, ++quantity);
                mQuantityView.setText("x" + quantity);
                mHeroType.saveRunes();
                if (getSum() > 10) {
                    mAnotherItem.decrease();
                }
            }
        }
    }

    private void decrease() {
        if (mRune != null) {
            int quantity = mHeroType.runes.get(mRune);
            if (--quantity > 0) {
                mHeroType.runes.put(mRune, quantity);
                mQuantityView.setText("x" + quantity);
                mHeroType.saveRunes();
            } else {
                delete();
            }
        }
    }

    private void delete() {
        if (mRune != null) {
            mHeroType.runes.remove(mRune);
            mRune = null;
            mRuneButton.setText(null);
            mQuantityView.setText(null);
            mHeroType.saveRunes();
        }
    }
}

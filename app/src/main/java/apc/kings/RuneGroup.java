package apc.kings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import apc.kings.data.HeroType;
import apc.kings.data.Rune;

public class RuneGroup extends LinearLayout {

    private RuneItem[] mItems = new RuneItem[2];

    public RuneGroup(Context context) {
        super(context);
        init();
    }

    public RuneGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RuneGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public RuneGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        int category = getId();
        Context context = getContext();
        Resources resources = getResources();
        String res_name = resources.getResourceEntryName(category);
        String package_name = context.getPackageName();
        int background = resources.getIdentifier("round_" + res_name, "drawable", package_name);
        int color = resources.getColor(resources.getIdentifier(res_name, "color", package_name));
        for (int i = 0, n = mItems.length; i < n; ++i) {
            inflate(context, R.layout.item_rune, this);
            mItems[i] = (RuneItem) getChildAt(i);
            mItems[i].init(category, background, color);
        }
        mItems[0].mAnotherItem = mItems[1];
        mItems[1].mAnotherItem = mItems[0];
    }

    public void initItems(HeroType heroType) {
        int id = getId();
        int i = 0;
        int n = mItems.length;
        Rune[] runes = new Rune[n];
        for (Rune rune : heroType.runes.keySet()) {
            if (id == rune.category) {
                runes[i++] = rune;
            }
        }
        for (i = 0; i < n; ++i) {
            mItems[i].initRune(heroType, runes[i]);
        }
    }
}

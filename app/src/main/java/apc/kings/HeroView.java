package apc.kings;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import apc.kings.data.HeroType;
import apc.kings.data.Item;

public class HeroView extends RelativeLayout {

    HeroType heroType;

    public SimpleDraweeView image;
    public ViewGroup itemsGroup;
    private TextView nameView;

    public HeroView(Context context) {
        super(context);
        init();
    }

    public HeroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HeroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public HeroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_hero, this);
        image = (SimpleDraweeView) findViewById(R.id.image);
        nameView = (TextView) findViewById(R.id.name);
        itemsGroup = (ViewGroup) findViewById(R.id.items);
    }

    public void setHero(@NonNull String name) {
        HeroType heroType = HeroType.findHero(name);
        if (heroType != null) {
            this.heroType = heroType;
            image.setImageURI(heroType.getImageUri("poster"));
            nameView.setText(name);
            setItems(heroType.buildDefaultItems());
        }
    }

    public void setItems(Item[] items) {
        heroType.items = items;
        if (null == items) {
            items = new Item[6];
        }
        int n = items.length;
        for (int i = 0; i < 6; ++i) {
            Uri uri = null;
            if (i < n) {
                Item item = items[i];
                if (item != null) {
                    uri = Uri.parse("res://drawable/" + item.imageRes);
                }
            }
            ((SimpleDraweeView) itemsGroup.getChildAt(i)).setImageURI(uri);
        }
    }
}

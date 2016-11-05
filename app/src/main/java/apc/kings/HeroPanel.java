package apc.kings;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import apc.kings.data.HeroType;

public class HeroPanel extends RelativeLayout {

    HeroType heroType;

    public SimpleDraweeView image;
    public ItemGroup itemsGroup;
    private TextView nameView;

    public HeroPanel(Context context) {
        super(context);
        init();
    }

    public HeroPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HeroPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public HeroPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.panel_hero, this);
        image = (SimpleDraweeView) findViewById(R.id.image);
        nameView = (TextView) findViewById(R.id.name);
        itemsGroup = (ItemGroup) findViewById(R.id.items);
    }

    public void setHero(@NonNull String name) {
        HeroType heroType = HeroType.findHero(name);
        if (heroType != null) {
            this.heroType = heroType;
            image.setImageURI(heroType.getImageUri(getContext(), HeroType.TYPE_POSTER));
            nameView.setText(name);

            itemsGroup.setItems(heroType.items);
        }
    }
}

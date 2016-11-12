package apc.kings;

import android.content.Context;
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

import apc.kings.data.HeroType;

public class HeroFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_HERO = 1;
    private static final int REQUEST_ITEM = 2;

    HeroType mHeroType;
    private SimpleDraweeView mPosterView;
    private TextView mNameView;
    private ItemGroup mItemGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        mPosterView = (SimpleDraweeView) view.findViewById(R.id.image);
        mNameView = (TextView) view.findViewById(R.id.name);
        mItemGroup = (ItemGroup) view.findViewById(R.id.items);
        mPosterView.setOnClickListener(this);
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
        }
    }

    @Override
    public void onClick(View v) {
        Context context = getContext();
        switch (v.getId()) {
            case R.id.image:
                startActivityForResult(new Intent(null == mHeroType ? null : mHeroType.name, null, context, HeroActivity.class), REQUEST_HERO);
                break;
            case R.id.items:
                startActivityForResult(new Intent(mHeroType.name, null, context, ItemActivity.class), REQUEST_ITEM);
                break;
        }
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
}

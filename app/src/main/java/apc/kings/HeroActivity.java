package apc.kings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apc.kings.common.MapHolder;
import apc.kings.common.AbsAdapter;
import apc.kings.common.SdView;
import apc.kings.data.HeroType;

@SuppressWarnings("ConstantConditions")
public class HeroActivity extends AppCompatActivity implements View.OnClickListener {

    View mLockButton;
    List<HeroType> mHeroTypes = new ArrayList<>();
    HeroType mSelectedHeroType;

    private int mCategory;
    private AbsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        mLockButton = findViewById(R.id.lock);
        mAdapter = new Adapter();
        ((RecyclerView) findViewById(R.id.heroes)).setAdapter(mAdapter);

        String name = getIntent().getAction();
        if (!TextUtils.isEmpty(name)) {
            mSelectedHeroType = HeroType.findHero(name);
        } else {
            mLockButton.setEnabled(false);
        }
        onCategoryChanged(R.id.cat_all);

        mLockButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock:
                setResult(RESULT_OK, new Intent(mSelectedHeroType.name, null));
                finish();
                break;
            default:
                onCategoryChanged(v.getId());
        }
    }

    private void onCategoryChanged(int id) {
        if (id != mCategory) {
            if (mCategory > 0) {
                findViewById(mCategory).setSelected(false);
            }
            findViewById(id).setSelected(true);
            mCategory = id;

            if (id == R.id.cat_all) {
                mHeroTypes = Arrays.asList(HeroType.ALL_HEROES);
            } else {
                mHeroTypes = new ArrayList<>();
                for (HeroType heroType : HeroType.ALL_HEROES) {
                    if (id == heroType.category) {
                        mHeroTypes.add(heroType);
                    }
                }
                for (HeroType heroType : HeroType.ALL_HEROES) {
                    if (id == heroType.subCategory) {
                        mHeroTypes.add(heroType);
                    }
                }
            }

            mAdapter.notifyDataSetChanged();

            int selected = -1;
            if (mSelectedHeroType != null) {
                for (int i = 0, n = mHeroTypes.size(); i < n; ++i) {
                    if (mHeroTypes.get(i) == mSelectedHeroType) {
                        selected = i;
                        break;
                    }
                }
            }
            mAdapter.setSelected(selected);
        }
    }

    private class Adapter extends AbsAdapter {

        Adapter() {
            super(R.layout.item_hero);
        }

        @Override
        public void onBindViewHolder(MapHolder holder, int position) {
            SdView view = (SdView) holder.itemView;
            view.setImage(mHeroTypes.get(position).getImageUri(view.getContext(), HeroType.TYPE_HERO), position == mSelected);
        }

        @Override
        public int getItemCount() {
            return mHeroTypes.size();
        }

        @Override
        protected void onItemChanged(int position) {
            if (position >= 0) {
                mSelectedHeroType = mHeroTypes.get(position);
                mLockButton.setEnabled(true);
            } else {
                mLockButton.setEnabled(false);
            }
        }
    }
}

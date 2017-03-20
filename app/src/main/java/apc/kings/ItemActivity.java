package apc.kings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import apc.kings.common.AbsAdapter;
import apc.kings.common.MapHolder;
import apc.kings.common.SdView;
import apc.kings.data.HeroType;
import apc.kings.data.Item;

@SuppressWarnings("ConstantConditions")
public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    List<Item> mItems = new ArrayList<>();
    Item mSelectedItem;

    private HeroType mHeroType;
    private int mCategory;
    private Adapter mAdapter;
    private View mEditButton;
    private View mCancelButton;
    private ItemGroup mItemGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mHeroType = HeroType.findHero(getIntent().getAction());
        ((SimpleDraweeView) findViewById(R.id.hero)).setImageURI(mHeroType.getImageUri(this, HeroType.TYPE_HERO));
        mEditButton = findViewById(R.id.edit);
        mCancelButton = findViewById(R.id.cancel);
        mItemGroup = (ItemGroup) findViewById(R.id.item_set);
        mItemGroup.setItems(mHeroType.items);

        mAdapter = new Adapter();
        ((RecyclerView) findViewById(R.id.items)).setAdapter(mAdapter);
        onCategoryChanged(R.id.item_weapon);

        mEditButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        findViewById(R.id.reset_default).setOnClickListener(this);
        if (mHeroType.recommended_items != null) {
            View recommendedButton = findViewById(R.id.reset_recommended);
            recommendedButton.setVisibility(View.VISIBLE);
            recommendedButton.setOnClickListener(this);
        }
    }

    public void onSelect(View v) {
        onCategoryChanged(v.getId());
    }

    private void onCategoryChanged(int id) {
        if (id != mCategory) {
            if (mCategory > 0) {
                findViewById(mCategory).setSelected(false);
            }
            findViewById(id).setSelected(true);
            mCategory = id;

            mItems.clear();
            for (Item item : Item.ALL_ITEMS) {
                if (id == item.category) {
                    mItems.add(item);
                }
            }
            mAdapter.notifyDataSetChanged();

            int selected = -1;
            if (mSelectedItem != null) {
                for (int i = 0, n = mItems.size(); i < n; ++i) {
                    if (mItems.get(i) == mSelectedItem) {
                        selected = i;
                        break;
                    }
                }
            }
            mAdapter.setSelected(selected);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit:
                if (mEditButton.isSelected()) {
                    mEditButton.setSelected(false);
                    mCancelButton.setVisibility(View.GONE);
                    mHeroType.setItems(mItemGroup.editDone());
                } else {
                    mEditButton.setSelected(true);
                    mCancelButton.setVisibility(View.VISIBLE);
                    mItemGroup.edit();
                }
                break;
            case R.id.cancel:
                onCancel();
                break;
            case R.id.reset_recommended:
            case R.id.reset_default:
                showConfirmDialog(id);
                break;
        }
    }

    public void onFill(View v) {
        if (mSelectedItem != null) {
            mItemGroup.editItem(v.getId(), mSelectedItem);
        }
    }

    public void onCancel() {
        mEditButton.setSelected(false);
        mCancelButton.setVisibility(View.GONE);
        mItemGroup.editCancel();
    }

    @Override
    public void onBackPressed() {
        if (mEditButton.isSelected()) {
            onCancel();
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmDialog(final int id) {
        Resources resources = getResources();
        new AlertDialog.Builder(this).setTitle("提示").setMessage(resources.getIdentifier(resources.getResourceEntryName(id), "string", getPackageName()))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditButton.setSelected(false);
                        mCancelButton.setVisibility(View.GONE);
                        switch (id) {
                            case R.id.reset_recommended:
                                mItemGroup.setItems(mHeroType.recommended_items);
                                mHeroType.setItems(mHeroType.recommended_items);
                                break;
                            case R.id.reset_default:
                                mItemGroup.setItems(mHeroType.default_items);
                                mHeroType.setItems(mHeroType.default_items);
                                break;
                        }
                    }
                }).create().show();
    }

    private class Adapter extends AbsAdapter {

        Adapter() {
            super(R.layout.item_item);
        }

        @Override
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(MapHolder holder, int position) {
            SdView image = holder.get(R.id.image);
            TextView name = holder.get(R.id.name);
            TextView price = holder.get(R.id.price);
            View view = holder.get(R.id.experimental);

            Item item = mItems.get(position);
            image.setImage(Uri.parse("res:///" + item.imageRes), position == mSelected);
            name.setText(item.name);
            price.setText(Integer.toString(item.price));
            view.setVisibility(item.experimental ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        protected void onItemChanged(int position) {
            mSelectedItem = position < 0 ? null : mItems.get(position);
        }
    }
}

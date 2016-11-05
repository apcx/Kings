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
import apc.kings.common.SdView;
import apc.kings.data.HeroType;
import apc.kings.data.Item;

@SuppressWarnings("ConstantConditions")
public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    List<Item> mItems = new ArrayList<>();
    Item mSelectedItem;

    private HeroType heroType;
    private int mCategory;
    private Adapter mAdapter;
    private View mEditButton;
    private View mCancelButton;
    private ItemGroup mItemGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        heroType = HeroType.findHero(getIntent().getAction());
        ((SimpleDraweeView) findViewById(R.id.hero)).setImageURI(heroType.getImageUri(this, HeroType.TYPE_HERO));
        mEditButton = findViewById(R.id.edit);
        mCancelButton = findViewById(R.id.cancel);
        mItemGroup = (ItemGroup) findViewById(R.id.item_set);
        mItemGroup.setItems(heroType.items);

        mAdapter = new Adapter();
        ((RecyclerView) findViewById(R.id.items)).setAdapter(mAdapter);
        onSelected(R.id.item_weapon);

        mEditButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        findViewById(R.id.reset_recommended).setOnClickListener(this);
        findViewById(R.id.reset_default).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit:
                if (mEditButton.isSelected()) {
                    mEditButton.setSelected(false);
                    mCancelButton.setVisibility(View.GONE);
                    heroType.setItems(mItemGroup.editDone());
                } else {
                    mEditButton.setSelected(true);
                    mCancelButton.setVisibility(View.VISIBLE);
                    mItemGroup.edit();
                }
                break;
            case R.id.cancel:
                mEditButton.setSelected(false);
                mCancelButton.setVisibility(View.GONE);
                mItemGroup.editCancel();
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

    public void onSelect(View v) {
        onSelected(v.getId());
    }

    private void onSelected(int id) {
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
                                mItemGroup.setItems(heroType.recommendedItems);
                                heroType.setItems(heroType.recommendedItems);
                                break;
                            case R.id.reset_default:
                                mItemGroup.setItems(heroType.defaultItems);
                                heroType.setItems(heroType.defaultItems);
                                break;
                        }
                    }
                }).create().show();
    }

    private class Adapter extends AbsAdapter<Holder> {

        Adapter() {
            super(R.layout.item_item, Holder.class);
        }

        @Override
        public void onItemChanged(int position) {
            mSelectedItem = position < 0 ? null : mItems.get(position);
        }

        @Override
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(Holder holder, int position) {
            Item item = mItems.get(position);
            holder.image.setImage(Uri.parse("res:///" + item.imageRes), position == mSelected);
            holder.name.setText(item.name);
            holder.price.setText(Integer.toString(item.price));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {

        SdView image;
        TextView name;
        TextView price;

        public Holder(View itemView) {
            super(itemView);
            image = (SdView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}

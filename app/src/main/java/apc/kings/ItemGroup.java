package apc.kings;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import apc.kings.common.BambooView;
import apc.kings.data.Item;

public class ItemGroup extends BambooView {

    private static final Uri FILL_SLOT = Uri.parse("res:///" + R.drawable.item_fill);

    private Item[] mItems;
    private Item[] mEditItems;
    private SimpleDraweeView[] mImageViews;
    private View[] mDeleteButtons;

    public ItemGroup(Context context) {
        super(context);
    }

    public ItemGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public ItemGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(GONE);
                int index = (int) v.getTag();
                mEditItems[index] = null;
                mImageViews[index].setSelected(true);
                mImageViews[index].setImageURI(FILL_SLOT);
            }
        };

        mEditItems = new Item[Item.SLOTS];
        mImageViews = new SimpleDraweeView[Item.SLOTS];
        mDeleteButtons = new View[Item.SLOTS];
        for (int i = 0; i < Item.SLOTS; ++i) {
            View view = getChildAt(i);
            mImageViews[i] = (SimpleDraweeView) view.findViewById(R.id.image);
            mDeleteButtons[i] = view.findViewById(R.id.delete);
            mDeleteButtons[i].setTag(i);
            mDeleteButtons[i].setOnClickListener(listener);
        }
    }

    public void setItems(@NonNull Item[] items) {
        mItems = items;
        for (int i = 0; i < Item.SLOTS; ++i) {
            mImageViews[i].setSelected(false);
            mImageViews[i].setImageURI(null == mItems[i] ? null : Uri.parse("res:///" + items[i].imageRes));
            mDeleteButtons[i].setVisibility(GONE);
        }
    }

    public void edit() {
        for (int i = 0; i < Item.SLOTS; ++i) {
            mEditItems[i] = mItems[i];
            if (null == mItems[i]) {
                mImageViews[i].setSelected(true);
                mImageViews[i].setImageURI(FILL_SLOT);
            } else {
                mDeleteButtons[i].setVisibility(VISIBLE);
            }
        }
    }

    public void editCancel() {
        setItems(mItems);
    }

    public Item[] editDone() {
        setItems(mEditItems);
        return mEditItems;
    }

    public void editItem(int index, @NonNull Item item) {
        if (index >= 0 && index < Item.SLOTS) {
            mEditItems[index] = item;
            mImageViews[index].setSelected(false);
            mImageViews[index].setImageURI(Uri.parse("res:///" + item.imageRes));
            mDeleteButtons[index].setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int index = v.getId();
        if (index >= 0 && index < Item.SLOTS && mImageViews[index].isSelected()) {
            super.onClick(v);
        }
    }
}

package apc.kings.common;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

public abstract class AbsAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    protected int mSelected = -1;
    private int mItemRes;
    private Constructor<VH> mHolderConstructor;

    public AbsAdapter(@LayoutRes int itemRes, Class<VH> holderClass) {
        mItemRes = itemRes;
        try {
            mHolderConstructor = holderClass.getConstructor(View.class);
            mHolderConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // ignore
        }
    }

    public abstract void onItemChanged(int position);

    public void setSelected(int position) {
        if (position != mSelected) {
            int old = mSelected;
            mSelected = position;
            if (old >= 0) {
                notifyItemChanged(old);
            }
            if (position >= 0) {
                notifyItemChanged(position);
            }
            onItemChanged(position);
        }
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof RecyclerView.ViewHolder) {   // check tag-changed case
            setSelected(((RecyclerView.ViewHolder) tag).getAdapterPosition());
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = null;
        if (mHolderConstructor != null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(mItemRes, parent, false);
            try {
                holder = mHolderConstructor.newInstance(view);
                view.setTag(holder);
                view.setOnClickListener(this);
            } catch (Exception e) {
                // ignore
            }
        }
        return holder;
    }
}

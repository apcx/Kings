package apc.kings.common;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

public abstract class AbsAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    protected int selected = -1;
    private int itemRes;
    private Constructor<VH> holderConstructor;

    public AbsAdapter(@LayoutRes int itemRes, Class<VH> holderClass) {
        this.itemRes = itemRes;
        try {
            holderConstructor = holderClass.getConstructor(View.class);
        } catch (NoSuchMethodException e) {
            // ignore
        }
    }

    public abstract void onItemChanged(int position);

    public void setSelected(int position) {
        if (position != selected) {
            int old = selected;
            selected = position;
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
        if (holderConstructor != null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(itemRes, parent, false);
            try {
                holder = holderConstructor.newInstance(view);
                view.setTag(holder);
                view.setOnClickListener(this);
            } catch (Exception e) {
                // ignore
            }
        }
        return holder;
    }
}

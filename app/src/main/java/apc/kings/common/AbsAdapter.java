package apc.kings.common;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbsAdapter extends RecyclerView.Adapter<MapHolder> implements View.OnClickListener {

    protected int mSelected = -1;
    private int mItemRes;

    public AbsAdapter(@LayoutRes int itemRes) {
        mItemRes = itemRes;
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
    public MapHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemRes, parent, false);
        MapHolder holder = new MapHolder(view);
        view.setTag(holder);
        view.setOnClickListener(this);
        return holder;
    }
}

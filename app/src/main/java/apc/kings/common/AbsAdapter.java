package apc.kings.common;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("WeakerAccess")
public abstract class AbsAdapter extends RecyclerView.Adapter<MapHolder> implements View.OnClickListener {

    protected int mSelected = -1;

    private RecyclerView mRecyclerView;
    private boolean mClick;
    private int mItemRes;

    public AbsAdapter(@LayoutRes int itemRes) {
        mItemRes = itemRes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public MapHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemRes, parent, false);
        MapHolder holder = new MapHolder(view);
        view.setTag(holder);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof RecyclerView.ViewHolder) {   // check tag-changed case
            mClick = true;
            onItemClick(((RecyclerView.ViewHolder) tag).getAdapterPosition());
            mClick = false;
        }
    }

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
        if (!mClick && mRecyclerView != null && position >= 0) {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    protected void onItemClick(int position) {
        setSelected(position);
    }

    protected void onItemChanged(int position) {

    }
}

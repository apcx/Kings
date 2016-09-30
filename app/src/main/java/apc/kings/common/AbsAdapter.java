package apc.kings.common;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbsAdapter extends RecyclerView.Adapter<Holder> implements View.OnClickListener {

    protected int selected = -1;
    private int itemRes;

    public AbsAdapter(@LayoutRes int itemRes) {
        this.itemRes = itemRes;
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
        setSelected(((Holder) v.getTag()).getAdapterPosition());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemRes, parent, false);
        view.setOnClickListener(this);
        return new Holder(view);
    }
}

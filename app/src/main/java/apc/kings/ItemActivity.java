package apc.kings;

import android.annotation.SuppressLint;
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
import apc.kings.data.Item;

@SuppressWarnings("ConstantConditions")
public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    List<Item> items = new ArrayList<>();

    private int category;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        onSelected(R.id.item_weapon);
    }

    @Override
    public void onClick(View v) {
        onSelected(v.getId());
    }

    private void onSelected(int id) {
        if (id != category) {
            if (category > 0) {
                findViewById(category).setSelected(false);
            }
            findViewById(id).setSelected(true);
            category = id;

            items.clear();
            for (Item item : Item.ALL_ITEMS) {
                if (id == item.category) {
                    items.add(item);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private class Adapter extends AbsAdapter<Holder> {

        Adapter() {
            super(R.layout.item_item, Holder.class);
        }

        @Override
        public void onItemChanged(int position) {

        }

        @Override
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(Holder holder, int position) {
            Item item = items.get(position);
            holder.image.setImageURI(Uri.parse("res://drawable/" + item.imageRes));
            holder.name.setText(item.name);
            holder.price.setText(Integer.toString(item.price));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {

        SimpleDraweeView image;
        TextView name;
        TextView price;

        public Holder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}

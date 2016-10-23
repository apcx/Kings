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
import apc.kings.common.SdView;
import apc.kings.data.HeroType;
import apc.kings.data.Item;

@SuppressWarnings("ConstantConditions")
public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    List<Item> items = new ArrayList<>();
    Item selectedItem;

    private int category;
    private Adapter adapter;
    private View editButton;
    private View cancelButton;
    private View itemGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ((SimpleDraweeView) findViewById(R.id.hero)).setImageURI(HeroType.findHero(getIntent().getAction()).getImageUri(this, HeroType.TYPE_HERO));

        adapter = new Adapter();
        ((RecyclerView) findViewById(R.id.items)).setAdapter(adapter);
        onSelected(R.id.item_weapon);

        editButton = findViewById(R.id.edit);
        cancelButton = findViewById(R.id.cancel);
        itemGroup = findViewById(R.id.item_set);

        editButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                if (editButton.isSelected()) {
                    editButton.setSelected(false);
                    cancelButton.setVisibility(View.GONE);
                } else {
                    editButton.setSelected(true);
                    cancelButton.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cancel:
                editButton.setSelected(false);
                cancelButton.setVisibility(View.GONE);
                break;
        }
    }

    public void onSelect(View v) {
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

            int selected = -1;
            if (selectedItem != null) {
                for (int i = 0, n = items.size(); i < n; ++i) {
                    if (items.get(i) == selectedItem) {
                        selected = i;
                        break;
                    }
                }
            }
            adapter.setSelected(selected);
        }
    }

    private class Adapter extends AbsAdapter<Holder> {

        Adapter() {
            super(R.layout.item_item, Holder.class);
        }

        @Override
        public void onItemChanged(int position) {
            selectedItem = position < 0 ? null : items.get(position);
        }

        @Override
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(Holder holder, int position) {
            Item item = items.get(position);
            holder.image.setImage(Uri.parse("res://drawable/" + item.imageRes), position == selected);
            holder.name.setText(item.name);
            holder.price.setText(Integer.toString(item.price));
        }

        @Override
        public int getItemCount() {
            return items.size();
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

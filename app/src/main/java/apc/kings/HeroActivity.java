package apc.kings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apc.kings.common.AbsAdapter;
import apc.kings.common.Holder;
import apc.kings.common.SdView;
import apc.kings.data.HeroType;

@SuppressWarnings("ConstantConditions")
public class HeroActivity extends AppCompatActivity implements View.OnClickListener {

    View lockButton;
    List<HeroType> heroTypes = new ArrayList<>();
    HeroType selectedHeroType;

    private int category;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        lockButton = findViewById(R.id.lock);
        adapter = new Adapter();
        ((RecyclerView) findViewById(R.id.heroes)).setAdapter(adapter);

        String name = getIntent().getAction();
        if (!TextUtils.isEmpty(name)) {
            selectedHeroType = HeroType.findHero(name);
        } else {
            lockButton.setEnabled(false);
        }
        onSelected(R.id.cat_all);

        lockButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock:
                setResult(RESULT_OK, new Intent(selectedHeroType.name, null));
                finish();
                break;
            default:
                onSelected(v.getId());
        }
    }

    private void onSelected(int id) {
        if (id != category) {
            if (category > 0) {
                findViewById(category).setSelected(false);
            }
            findViewById(id).setSelected(true);
            category = id;

            if (id == R.id.cat_all) {
                heroTypes = Arrays.asList(HeroType.ALL_HEROES);
            } else {
                heroTypes = new ArrayList<>();
                for (HeroType heroType : HeroType.ALL_HEROES) {
                    if (id == heroType.category) {
                        heroTypes.add(heroType);
                    }
                }
                for (HeroType heroType : HeroType.ALL_HEROES) {
                    if (id == heroType.subCategory) {
                        heroTypes.add(heroType);
                    }
                }
            }

            adapter.notifyDataSetChanged();

            int selected = -1;
            if (selectedHeroType != null) {
                for (int i = 0, n = heroTypes.size(); i < n; ++i) {
                    if (heroTypes.get(i) == selectedHeroType) {
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
            super(R.layout.item_hero, Holder.class);
        }

        @Override
        public void onItemChanged(int position) {
            lockButton.setEnabled(true);
            if (position >= 0) {
                selectedHeroType = heroTypes.get(position);
            }
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            SdView view = (SdView) holder.itemView;
            view.setImage(heroTypes.get(position).getImageUri(view.getContext(), HeroType.TYPE_HERO), position == selected);
        }

        @Override
        public int getItemCount() {
            return heroTypes.size();
        }
    }
}

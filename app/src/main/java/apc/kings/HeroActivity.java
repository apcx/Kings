package apc.kings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apc.kings.data.HeroType;

@SuppressWarnings("ConstantConditions")
public class HeroActivity extends AppCompatActivity implements View.OnClickListener {

    List<HeroType> heroTypes = new ArrayList<>();
    HeroType selectedHeroType;

    private int category;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        adapter = new Adapter();
        ((RecyclerView) findViewById(R.id.heroes)).setAdapter(adapter);

        String name = getIntent().getAction();
        if (!TextUtils.isEmpty(name)) {
            selectedHeroType = HeroType.findHero(name);
        }
        onSelected(R.id.cat_all);
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
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_hero, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            HeroType heroType = heroTypes.get(position);
            SimpleDraweeView view = (SimpleDraweeView) holder.itemView;
            view.setImageURI(heroType.getImageUri("hero"));

            GenericDraweeHierarchy hierarchy = view.getHierarchy();
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            roundingParams.setCornersRadii(3, 14, 3, 14);
            if (heroType == selectedHeroType) {
                roundingParams.setBorder(0xfffae58f, 3);
            } else {
                roundingParams.setBorder(0xff1e4e66, 2);
            }
            hierarchy.setRoundingParams(roundingParams);

            final String name = heroType.name;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK, new Intent(null, Uri.parse(name)));
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return heroTypes.size();
        }
    }
}

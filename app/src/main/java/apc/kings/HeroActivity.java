package apc.kings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apc.kings.common.AbsAdapter;
import apc.kings.common.GrayProcessor;
import apc.kings.common.Holder;
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
                setResult(RESULT_OK, new Intent(null, Uri.parse(selectedHeroType.name)));
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
            for (int i = 0, n = heroTypes.size(); i < n; ++i) {
                if (selectedHeroType == heroTypes.get(i)) {
                    selected = i;
                    break;
                }
            }
            adapter.setSelected(selected);
        }
    }

    private class Adapter extends AbsAdapter<Holder> {

        Adapter() {
            super(R.layout.recycler_hero, Holder.class);
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
            SimpleDraweeView view = (SimpleDraweeView) holder.itemView;
            Uri uri = heroTypes.get(position).getImageUri(view.getContext(), "hero");

            GenericDraweeHierarchy hierarchy = view.getHierarchy();
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            roundingParams.setCornersRadii(3, 14, 3, 14);
            if (position == selected) {
                roundingParams.setBorder(0xfffae58f, 3);
                view.setController(Fresco.newDraweeControllerBuilder()
                        .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri).setPostprocessor(new GrayProcessor(uri)).build())
                        .build());
            } else {
                roundingParams.setBorder(0xff1e4e66, 2);
                view.setImageURI(uri);
            }
            hierarchy.setRoundingParams(roundingParams);
        }

        @Override
        public int getItemCount() {
            return heroTypes.size();
        }
    }
}

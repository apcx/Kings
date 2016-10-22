package apc.kings.common;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class HeroView extends SimpleDraweeView {

    @SuppressWarnings("unused")
    public HeroView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public HeroView(Context context) {
        super(context);
    }

    public HeroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeroView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("unused")
    public HeroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("ConstantConditions")
    public void setImage(@NonNull Uri uri, boolean selected) {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (selected) {
            roundingParams.setBorderColor(0xfffae58f);
            setController(Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri).setPostprocessor(new GrayProcessor(uri)).build())
                    .build());
        } else {
            roundingParams.setBorderColor(0xff1e4e66);
            setImageURI(uri);
        }
        hierarchy.setRoundingParams(roundingParams);
    }
}

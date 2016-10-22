package apc.kings.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import apc.kings.R;

public class SdView extends SimpleDraweeView {

    private static int[] STATE_SELECTED = {android.R.attr.state_selected};

    private ColorStateList colorStateList;
    private float borderWidth;
    private float selectedBorderWidth;
    private boolean selectedProcessor;

    @SuppressWarnings("unused")
    public SdView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public SdView(Context context) {
        super(context);
    }

    public SdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @SuppressWarnings("unused")
    public SdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SdView);
        colorStateList = ta.getColorStateList(R.styleable.SdView_roundingBorderColor);
        borderWidth = ta.getDimension(R.styleable.SdView_roundingBorderWidth, 0);
        selectedBorderWidth = ta.getDimension(R.styleable.SdView_selectedBorderWidth, 0);
        selectedProcessor = ta.getBoolean(R.styleable.SdView_selectedProcessor, false);
        ta.recycle();
    }

    public void setImage(@NonNull Uri uri, boolean selected) {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (roundingParams != null) {
            int color = colorStateList.getDefaultColor();
            if (selected) {
                roundingParams.setBorder(colorStateList.getColorForState(STATE_SELECTED, color), selectedBorderWidth);
            } else {
                roundingParams.setBorder(color, borderWidth);
            }
            hierarchy.setRoundingParams(roundingParams);
        }

        if (selected && selectedProcessor) {
            setController(Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri).setPostprocessor(new GrayProcessor(uri)).build())
                    .build());
        } else {
            setImageURI(uri);
        }
    }
}

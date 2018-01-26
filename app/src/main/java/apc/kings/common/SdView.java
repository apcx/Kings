package apc.kings.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import apc.kings.R;

public class SdView extends SimpleDraweeView {

    private static int[] STATE_SELECTED = {android.R.attr.state_selected};

    private ColorStateList mColorStateList;
    private Uri mUri;
    private float mBorderWidth;
    private float mSelectedBorderWidth;
    private boolean mSelectedProcessor;

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
        mColorStateList = ta.getColorStateList(R.styleable.SdView_roundingBorderColor);
        mBorderWidth = ta.getDimension(R.styleable.SdView_roundingBorderWidth, 0);
        mSelectedBorderWidth = ta.getDimension(R.styleable.SdView_selectedBorderWidth, 0);
        mSelectedProcessor = ta.getBoolean(R.styleable.SdView_selectedProcessor, false);
        ta.recycle();

        float minRadii = Math.max(mBorderWidth, mSelectedBorderWidth);
        if (minRadii > 0) {
            GenericDraweeHierarchy hierarchy = getHierarchy();
            RoundingParams params = hierarchy.getRoundingParams();
            if (params != null && !params.getRoundAsCircle()) {
                float[] radii = params.getCornersRadii();
                for (int i = 0; i < 8; ++i) {
                    if (radii[i] < minRadii) {
                        radii[i] = minRadii;   // For some devices, corner radii can't be smaller than the border width.
                    }
                }
            }
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        if ((null == uri && mUri != null) || (uri != null && !uri.equals(mUri))) {
            mUri = uri;
            updateView();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected != isSelected()) {
            super.setSelected(selected);
            updateView();
        }
    }

    private void updateView() {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        RoundingParams params = hierarchy.getRoundingParams();
        if (params != null) {
            int color = mColorStateList.getDefaultColor();
            if (isSelected()) {
                params.setBorder(mColorStateList.getColorForState(STATE_SELECTED, color), mSelectedBorderWidth);
            } else {
                params.setBorder(color, mBorderWidth);
            }
            hierarchy.setRoundingParams(params);
        }

        if (isSelected() && mSelectedProcessor) {
            setController(Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(mUri).setPostprocessor(new GrayProcessor(mUri)).build())
                    .build());
        } else {
            super.setImageURI(mUri);
        }
    }
}

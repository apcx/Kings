package apc.kings.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

import apc.kings.R;

public class BambooView extends LinearLayout implements View.OnClickListener {

    @LayoutRes protected int mItemRes;
    protected int mWeightSum;
    private Method mClickMethod;

    public BambooView(Context context) {
        super(context);
    }

    public BambooView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BambooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public BambooView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BambooView);
        mItemRes = ta.getResourceId(R.styleable.BambooView_android_layout, 0);
        mWeightSum = (int) ta.getFloat(R.styleable.BambooView_android_weightSum, 0);
        String onClick = ta.getString(R.styleable.BambooView_android_onClick);
        ta.recycle();

        Context context = getContext();
        if (!TextUtils.isEmpty(onClick)) {
            try {
                mClickMethod = context.getClass().getMethod(onClick, View.class);
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }

        initUI();
    }

    protected void initUI() {
        if (mItemRes > 0 && mWeightSum > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            boolean vertical = getOrientation() == VERTICAL;
            for (int i = 0; i < mWeightSum; ++i) {
                View item = inflater.inflate(mItemRes, this, false);
                LinearLayout.LayoutParams params = (LayoutParams) item.getLayoutParams();
                params.weight = 1;
                if (vertical) {
                    params.height = 0;
                } else {
                    params.width = 0;
                }
                addView(item, params);

                if (mClickMethod != null) {
                    item.setId(i);
                    item.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mClickMethod != null) {
            try {
                mClickMethod.invoke(getContext(), v);
            } catch (Exception e) {
                // ignore
            }
        }
    }
}

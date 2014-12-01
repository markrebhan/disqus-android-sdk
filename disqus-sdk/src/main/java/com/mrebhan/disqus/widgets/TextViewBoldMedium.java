package com.mrebhan.disqus.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewBoldMedium extends TextView {

    public TextViewBoldMedium(Context context) {
        super(context);
    }

    public TextViewBoldMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewBoldMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextViewBoldMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setTypeFace() {
        Typeface typeface = TypeFaceCache.getTypeface(getContext(), "fonts/Roboto-Medium.ttf");
        setTypeface(typeface);
    }
}

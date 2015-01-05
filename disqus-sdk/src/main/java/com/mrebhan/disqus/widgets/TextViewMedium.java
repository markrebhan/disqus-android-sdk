package com.mrebhan.disqus.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewMedium extends TextView {

    public TextViewMedium(Context context) {
        super(context);
        setFont();
    }

    public TextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public TextViewMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    public TextViewMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFont();
    }

    private void setFont() {
        Typeface typeface = TypeFaceCache.getTypeface(getContext(), "fonts/Roboto-Medium.ttf");
        setTypeface(typeface);
    }
}

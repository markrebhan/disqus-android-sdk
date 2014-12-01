package com.mrebhan.disqus.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewLight extends TextView {

    public TextViewLight(Context context) {
        super(context);
        setTypeFace();
    }

    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace();
    }

    public TextViewLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace();
    }

    public TextViewLight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeFace();
    }

    private void setTypeFace() {
        Typeface typeface = TypeFaceCache.getTypeface(getContext(), "fonts/Roboto-Light.ttf");
        setTypeface(typeface);
    }
}

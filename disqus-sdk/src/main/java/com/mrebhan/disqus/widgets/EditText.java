package com.mrebhan.disqus.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditText extends android.widget.EditText {

    public EditText(Context context) {
        super(context);
        setTypeFace();
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace();
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace();
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeFace();
    }

    private void setTypeFace() {
        Typeface typeface = TypeFaceCache.getTypeface(getContext(), "fonts/Roboto-Light.ttf");
        setTypeface(typeface);
    }
}

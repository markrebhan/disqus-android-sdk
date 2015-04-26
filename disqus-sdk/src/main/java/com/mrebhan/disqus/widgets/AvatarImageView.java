package com.mrebhan.disqus.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Image view that overrides standard implementation of drawable and uses custom {@link com.mrebhan.disqus.widgets.AvatarDrawable}
 */
public class AvatarImageView extends ImageView {

    private Drawable avatarDrawable;

    public AvatarImageView(Context context) {
        super(context);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        avatarDrawable = AvatarDrawable.fromBitmap(bm, 10);
        super.setImageDrawable(avatarDrawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        avatarDrawable = AvatarDrawable.fromDrawable(drawable, 10);
        super.setImageDrawable(avatarDrawable);
    }

    @Override
    public void setBackground(Drawable background) {
        avatarDrawable = AvatarDrawable.fromDrawable(background, 10);
        super.setBackground(avatarDrawable);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        avatarDrawable = AvatarDrawable.fromDrawable(background, 10);
        super.setBackgroundDrawable(avatarDrawable);
    }
}

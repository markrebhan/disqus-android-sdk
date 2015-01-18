package com.mrebhan.disqus.widgets;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Avatar drawable takes a bitmap, gets put into a bitmap shader and is drawn on a rounded rectangle.
 *
 * This reduces the need to make a copy of a bitmap!
 */
public class AvatarDrawable extends Drawable {

    private final float radius;
    private final RectF rectF = new RectF();
    private final BitmapShader bitmapShader;
    private final Paint paint;

    public static AvatarDrawable fromBitmap(Bitmap bitmap, float radius) {
        if (bitmap == null) {
            return null;
        } else {
            return new AvatarDrawable(bitmap, radius);
        }
    }

    public static Drawable fromDrawable(Drawable drawable, float radius) {
        if (drawable != null) {
            if (drawable instanceof AvatarDrawable) {
                return drawable;
            }

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                return new AvatarDrawable(bitmap, radius);
            } else {
                Log.e("", "Failed to create bitmap from drawable");
            }
        }

        return drawable;
    }


    public AvatarDrawable(Bitmap bitmap, float radius) {
        this.radius = radius;
        this.bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setShader(bitmapShader);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        rectF.set(0, 0, bounds.width(), bounds.height());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

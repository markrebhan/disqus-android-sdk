package com.mrebhan.disqus.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by markrebhan on 11/28/14.
 */
public class ImageAvatarView extends ImageView {

    public ImageAvatarView(Context context) {
        super(context);
    }

    public ImageAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageAvatarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        Bitmap bitmapOriginal =  ((BitmapDrawable)drawable).getBitmap() ;
        Bitmap bitmapCopy = bitmapOriginal.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap roundedBitmap  = getRoundedBitmap(bitmapCopy, 6);
        canvas.drawBitmap(roundedBitmap, 0, 0, null);
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap, int radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0x00000000);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}

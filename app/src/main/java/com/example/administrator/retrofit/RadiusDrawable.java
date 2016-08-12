package com.example.administrator.retrofit;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Created by LiuXiaocong on 7/5/2016.
 */
public class RadiusDrawable extends Drawable {
    float mTopLeftRadius = 0;
    float mTopRightRadius = 0;
    float mBottomRight = 0;
    float mBottomLeft = 0;
    int mColor;

    public RadiusDrawable(float topLeftRadius, float topRightRadius, float bottomRight, float bottomLeft, @ColorInt int color) {
        mTopLeftRadius = topLeftRadius;
        mTopRightRadius = topRightRadius;
        mBottomRight = bottomRight;
        mBottomLeft = bottomLeft;
        mColor = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        RectF rectF;
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        Path path = new Path();

        path.moveTo(getBounds().left, getBounds().top - mTopLeftRadius);
        rectF = new RectF(getBounds().left, getBounds().top, getBounds().left + mTopLeftRadius * 2, getBounds().top + mTopLeftRadius * 2);
        path.arcTo(rectF, 180, 90);

        path.lineTo(getBounds().right - mTopRightRadius, getBounds().top);
        rectF = new RectF(getBounds().right - mTopRightRadius * 2, getBounds().top, getBounds().right, getBounds().top + mTopRightRadius * 2);
        path.arcTo(rectF, 270, 90);


        path.lineTo(getBounds().right, getBounds().bottom - mBottomRight);
        rectF = new RectF(getBounds().right - mBottomRight * 2, getBounds().bottom - mBottomRight * 2, getBounds().right, getBounds().bottom);
        path.arcTo(rectF, 0, 90);

        path.lineTo(getBounds().left + mBottomLeft * 2, getBounds().bottom);
        rectF = new RectF(getBounds().left, getBounds().bottom - mBottomLeft * 2, getBounds().left + mBottomLeft * 2, getBounds().bottom);
        path.arcTo(rectF, 90, 90);

        path.close();
        canvas.drawPath(path, paint);
    }


    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 1;
    }
}

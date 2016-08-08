package com.android.project.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.project.R;

/**
 * Created by Lobster on 29.07.16.
 */

public class SquareImageView extends ImageView {

    private float mCornerRadius;

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SquareImageView,
                0, 0);

        try {
            mCornerRadius = a.getFloat(R.styleable.SquareImageView_cornerRadius, 0.0f);
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        canvas.drawRoundRect(createRect(), getCornerRadius(), getCornerRadius(), createPaint(drawable));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    private Bitmap createCropScaleBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bitmap = Bitmap.createBitmap(bitmap,
                    (bitmap.getWidth() - bitmap.getHeight()) / 2, 0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        } else {

            bitmap = Bitmap.createBitmap(bitmap, 0,
                    (bitmap.getHeight() - bitmap.getWidth()) / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        return Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
    }

    private RectF createRect() {
        return new RectF(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());

    }

    private Paint createPaint(Drawable drawable) {
        Bitmap bitmap = createCropScaleBitmap(((BitmapDrawable) drawable).getBitmap());
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        return paint;
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        mCornerRadius = cornerRadius;
    }
}
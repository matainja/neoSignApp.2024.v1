package com.matainja.bootapplication.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RotatedImageView extends AppCompatImageView {

    private float rotation;

    public RotatedImageView(Context context) {
        super(context);
        init();
    }

    public RotatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotatedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Set scale type to FIT_XY
        setScaleType(ScaleType.FIT_XY);
    }

    public void setRotationDegrees(float degrees) {
        rotation = degrees;
        invalidate(); // Trigger a redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Save the current canvas state
        canvas.save();

        // Rotate the canvas
        canvas.rotate(rotation, getWidth() , getHeight() );

        // Call the superclass to draw the image
        super.onDraw(canvas);

        // Restore the canvas state
        canvas.restore();
    }
}


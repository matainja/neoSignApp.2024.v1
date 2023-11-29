package com.matainja.bootapplication.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;

public class RotatableMediaController extends MediaController {

    private int rotationAngle = 0;

    public RotatableMediaController(Context context) {
        super(context);
    }

    public void setRotationAngle(int angle) {
        this.rotationAngle = angle;
        rotateControllerView(angle);
    }

    private void rotateControllerView(int angle) {
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            view.setRotation(angle);
        }
    }
}

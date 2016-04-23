package com.eo5.amoeba.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

public class ArcView extends ImageView {
    private int r,b;
    private Paint mArcPaint;


    public ArcView(Context context, int color) {
        super(context);
        this.setFocusable(true);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        setClickable(true);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.FILL);
        setColor(color);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawCircle(centerX, centerY, outerRadius, circlePaint);
        canvas.drawArc(0, 0, r, b, 180, 90,true, mArcPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        r = w;
        b = h;



    }

    public void setColor(int color) {
        mArcPaint.setColor(color);
        this.invalidate();
    }


}
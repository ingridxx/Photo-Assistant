package com.example.photoassistant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * unused class for potential future upgrades
 */
public class LineView extends View
{
    private Paint paint = new Paint();

    private PointF pointA, pointB;

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        paint.setColor(Color.RED);

        paint.setStrokeWidth(5);

        canvas.drawLine(pointA.x,pointA.y,pointB.x,pointB.y,paint);

        super.onDraw(canvas);
    }

    public void setPointA(PointF point)
    {
        pointA = point ;
    }

    public void setPointB(PointF point)
    {
        pointB = point ;
    }

    public void draw()
    {
        invalidate();
        requestLayout();
    }
}
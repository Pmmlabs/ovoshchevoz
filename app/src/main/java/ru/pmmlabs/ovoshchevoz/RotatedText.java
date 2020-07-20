package ru.pmmlabs.ovoshchevoz;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class RotatedText extends AppCompatButton {

    public RotatedText(Context context) {
        super(context);
    }

    public RotatedText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatedText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(Double.valueOf(-19.3058*getHeight()/getWidth()+3.14679).floatValue(), getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}

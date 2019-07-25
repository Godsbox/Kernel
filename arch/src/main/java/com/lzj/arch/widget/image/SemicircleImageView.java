package com.lzj.arch.widget.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import lombok.Setter;

/**
 * 自定义圆角.
 */
@Setter
public class SemicircleImageView extends AppCompatImageView {

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids = {10f, 10f, 10f, 10f, 0f, 0f, 0f, 0f};
    Path path = new Path();

    public SemicircleImageView(Context context) {
        super(context);
        init();
    }

    public SemicircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemicircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {//API 18之下
            setLayerType(LAYER_TYPE_SOFTWARE, null);//禁用硬件加速
        }
    }

    /**
     * 画图
     */
    @Override
    protected void onDraw(Canvas canvas) {
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), rids, Path.Direction.CW);
        //如果开启硬件加速 API 18及其之上 才有效
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
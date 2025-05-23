//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.DashPathEffect;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.view.View;
//
//public class DashedLineView extends View {
//    private Paint paint;
//
//    public DashedLineView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.GRAY);
//        paint.setStrokeWidth(4);
//        paint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
//    }
//}

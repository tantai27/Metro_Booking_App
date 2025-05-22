//package com.example.metro_booking_app.view;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Path;
//import android.util.AttributeSet;
//import androidx.appcompat.widget.AppCompatImageView;
//
//public class DiagonalImageView extends AppCompatImageView {
//
//    private Path path;
//    private float angle = 400f; // Điều chỉnh góc nghiêng tại đây
//
//    public DiagonalImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        path = new Path();
//        setLayerType(LAYER_TYPE_SOFTWARE, null); // ⚠️ Bắt buộc để clipPath hoạt động
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        float width = getWidth();
//        float height = getHeight();
//
//        path.reset();
//        path.moveTo(0, 0);
//        path.lineTo(width, 0);
//        path.lineTo(width, height - angle);
//        path.lineTo(0, height);
//        path.close();
//
//        canvas.clipPath(path);
//        super.onDraw(canvas);
//    }
//
//}
package com.example.metro_booking_app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class DiagonalImageView extends AppCompatImageView {

    private Path clipPath;
    private Paint borderPaint;
    private float angle = 400f; // Độ nghiêng đường chéo

    public DiagonalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        clipPath = new Path();

        setLayerType(LAYER_TYPE_SOFTWARE, null); // Cho phép clipPath hoạt động

        // Paint cho cạnh viền chéo
        borderPaint = new Paint();
        borderPaint.setColor(0xFFFFB74D); // Màu cam
        borderPaint.setStrokeWidth(40f);   // Độ dày đường viền
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        // Tạo clip path đường chéo
        clipPath.reset();
        clipPath.moveTo(0, 0);
        clipPath.lineTo(width, 0);
        clipPath.lineTo(width, height - angle);
        clipPath.lineTo(0, height);
        clipPath.close();

        // Cắt ảnh theo đường chéo
        canvas.clipPath(clipPath);
        super.onDraw(canvas);

        // Vẽ chỉ đường chéo phía dưới (từ bottom-right đến bottom-left)
        canvas.drawLine(width, height - angle, 0, height, borderPaint);
    }
}

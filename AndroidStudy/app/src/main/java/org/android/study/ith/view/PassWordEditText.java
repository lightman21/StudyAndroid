package org.android.study.ith.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;
import org.android.study.ith.R;

/**
 * Created by tanghao on 16/6/12.
 */
public class PassWordEditText extends EditText {

  public static final String TAG = "thDrawEt";
  private static final int defaultContMargin = 5;
  private float borderWidth = 1F;
  private int passwordLength = 6;
  private float passCircleRadius = 8;
  private Paint passPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private int textLength;
  private Rect outStrokeRect = new Rect();
  private Rect contentRect = new Rect();


  public PassWordEditText(Context context, AttributeSet attrs) {
    super(context, attrs);

    borderWidth = getResources().getDimension(R.dimen.px_1dp);

    resetPaint();

    passPaint.setStrokeWidth(passCircleRadius);
    passPaint.setStyle(Paint.Style.FILL);
    passPaint.setColor(Color.BLACK);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    if (outStrokeRect.right == 0) {

      outStrokeRect.left = 0;
      outStrokeRect.top = 0;
      outStrokeRect.right = getWidth();
      outStrokeRect.bottom = getHeight();

      contentRect.left = outStrokeRect.left + defaultContMargin;
      contentRect.top = outStrokeRect.top + defaultContMargin;
      contentRect.right = outStrokeRect.right - defaultContMargin;
      contentRect.bottom = outStrokeRect.bottom - defaultContMargin;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // 外边框
    drawOutRect(canvas);

    // 内容区
    drawContentRect(canvas);

    // 分割线
    drawVerticalLine(canvas);

    // 密码
    drawPassCircle(canvas);
  }

  private void resetPaint() {
    borderPaint.setColor(Color.BLACK);
    borderPaint.setStyle(Paint.Style.STROKE);
    borderPaint.setStrokeWidth(borderWidth);
  }

  private void drawVerticalLine(Canvas canvas) {
    resetPaint();
    float cw = borderPaint.getStrokeWidth();
    borderPaint.setStrokeWidth(cw / 2);
    int width = getWidth();
    int height = getHeight();
    int perW = width / passwordLength;
    for (int i = 1; i < passwordLength; i++) {
      float from = perW * i;
      canvas.drawLine(from, 0, from, height, borderPaint);
    }
  }

  private void drawOutRect(Canvas canvas) {
    resetPaint();
    canvas.drawRect(outStrokeRect, borderPaint);
  }

  private void drawContentRect(Canvas canvas) {
    borderPaint.setColor(Color.WHITE);
    borderPaint.setStyle(Paint.Style.FILL);
    canvas.drawRect(contentRect, borderPaint);
  }

  private void drawPassCircle(Canvas canvas) {
    int height = getHeight();
    int width = getWidth();
    float cx, cy = height / 2;
    float half = width / passwordLength / 2;
    for (int i = 0; i < textLength; i++) {
      cx = width * i / passwordLength + half;
      canvas.drawCircle(cx, cy, passCircleRadius, passPaint);
    }
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    super.onTextChanged(text, start, lengthBefore, lengthAfter);
    this.textLength = text.toString().length();
    invalidate();
  }
}
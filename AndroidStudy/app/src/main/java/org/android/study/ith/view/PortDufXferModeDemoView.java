package org.android.study.ith.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tanghao on 12/1/16.
 * from: http://blog.csdn.net/iispring/article/details/50472485
 *
 * 般我们在调用canvas.drawXXX()方法时都会传入一个画笔Paint对象
 * Android在绘图时会先检查该画笔Paint对象有没有设置Xfermode
 * 如果没有设置Xfermode,那么直接将绘制的图形覆盖Canvas对应位置原有的像素
 *
 * 如果设置了Xfermode,那么会按照Xfermode具体的规则来更新Canvas中对应位置的像素颜色
 * 当我们调用canvas.drawRect()绘制矩形时
 * 画笔Paint已经设置Xfermode的值为PorterDuff.Mode.CLEAR
 * 此时Android首先是在内存中绘制了这么一个矩形
 * 所绘制的图形中的像素称作源像素(source,简称src)
 * 所绘制的矩形在Canvas中对应位置的矩形内的像素称作目标像素(destination,简称dst)
 * 源像素的ARGB四个分量会和Canvas上同一位置处的目标像素的ARGB四个分量按照Xfermode定义的规则进行计算
 * 形成最终的ARGB值,然后用该最终的ARGB值更新目标像素的ARGB值
 *
 *
 * Canvas默认有一个layer,所有操作都会最终绘制到这个layer上
 *
 * 我们还可以通过canvas.saveLayer()新建一个layer.
 * 新建的layer放置在canvas默认layer的上方.
 * 当我们执行了canvas.saveLayer()之后.所有的绘制操作都绘制到了我们新建的layer上.
 *
 * canvas.saveLayer()方法所产生的ARGB值都是(0,0,0,0).即被新建出来的时候这个layer是完全透明的.
 *
 * canvas.saveLayer()方法会返回一个int值.用于表示layer的ID
 * 我们绘图工作完成之后总会调用canvas.restoreToCount()或者canvas.restoreToCount(layerID)
 * 就是要把这个新建的layer绘制到默认的layer上面去.
 */

public class PortDufXferModeDemoView extends View {
	public static final int YELLOW = 0xFFFFCC44;
	public static final int BLUE = 0xFF66AAFF;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public PortDufXferModeDemoView(Context context) {
		super(context);
	}

	public PortDufXferModeDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PortDufXferModeDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		third(canvas);
	}

	/**
	 * 首先,我们调用了canvas.drawARGB(255, 139, 197, 186)方法将整个Canvas都绘制成一个颜色,
	 * 再执行完这句代码后,canvas上所有像素的颜色值的ARGB颜色都是(255,139,197,186),
	 * 由于像素的alpha分量是255而不是0,所以此时所有像素都不透明.
	 *
	 * 当我们执行了canvas.drawCircle(r, r, r, paint)之后,
	 * Android会在所画圆的位置用黄颜色的画笔绘制一个黄色的圆形,
	 * 此时整个圆形内部所有的像素颜色值的ARGB颜色都是0xFFFFCC44,
	 * 然后用这些黄色的像素替换掉Canvas中对应的同一位置中颜色值ARGB为(255,139,197,186)的像素,
	 * 这样就将黄色圆形绘制到Canvas上了。
	 *
	 * 当我们执行了canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint)之后,
	 * Android会在所画矩形的位置用蓝色的画笔绘制一个蓝色的矩形,
	 * 此时整个矩形内部所有的像素颜色值的ARGB颜色都是0xFF66AAFF,
	 * 然后用这些蓝色的像素替换掉Canvas中对应的同一位置中的像素,
	 * 这样黄色的圆中的右下角部分的像素与其他一些背景色像素就被蓝色像素替换了,
	 * 这样就将蓝色矩形绘制到Canvas上了。
	 */
	private void first(Canvas canvas) {
		//设置背景色
		canvas.drawARGB(255, 139, 197, 186);

		int canvasWidth = canvas.getWidth();
		int r = canvasWidth / 3;

		//绘制黄色的圆形
		paint.setColor(YELLOW);
		canvas.drawCircle(r, r, r, paint);

		//绘制蓝色的矩形
		paint.setColor(BLUE);
		canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
	}

	private void second(Canvas canvas) {
		//设置背景色
		canvas.drawARGB(255, 139, 197, 186);

		int canvasWidth = canvas.getWidth();
		int r = canvasWidth / 3;

		//绘制黄色的圆形
		paint.setColor(YELLOW);
		canvas.drawCircle(r, r, r, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		//绘制蓝色的矩形
		paint.setColor(BLUE);
		canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);

		paint.setXfermode(null);
	}

	private void third(Canvas canvas) {
		//设置背景色
		canvas.drawARGB(255, 139, 197, 186);

		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

		int r = canvasWidth / 3;

		//绘制黄色的圆形
		paint.setColor(YELLOW);
		canvas.drawCircle(r, r, r, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		//绘制蓝色的矩形
		paint.setColor(BLUE);
		canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);

		paint.setXfermode(null);

		canvas.restoreToCount(layerId);
	}
}

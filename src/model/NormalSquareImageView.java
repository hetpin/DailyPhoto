package model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NormalSquareImageView extends ImageView {

	public NormalSquareImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NormalSquareImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NormalSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}
}

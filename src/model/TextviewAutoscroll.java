package model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextviewAutoscroll extends TextView {

	public TextviewAutoscroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		rotate();
	}

	public TextviewAutoscroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		rotate();
	}

	public TextviewAutoscroll(Context context) {
		super(context);
		init();
		rotate();
	}

	private void rotate() {
		setSelected(true);
	}

	private void init() {
		if (!isInEditMode()) {

		}
	}

}
package edu.sou.rover2013.widgets;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class DigitalTextBox extends TextView {

	//Custom Font found here: http://www.dafont.com/ds-digital.font
	
	
	public DigitalTextBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DigitalTextBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DigitalTextBox(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"font/ds_digital/ds_digib.ttf");
		setTypeface(tf, 1);
		setTextColor(Color.RED);
	}
}
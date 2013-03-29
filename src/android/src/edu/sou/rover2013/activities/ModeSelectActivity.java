package edu.sou.rover2013.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;

/**
 * Activity that shows available control modes, and allows selection.
 * 
 * @author Ryan Dempsey
 *
 */
// TODO replace with preference screen. No need for whole activity.
public class ModeSelectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode_select);

		final Button simple = (Button) findViewById(R.id.simple);
		final Button complex = (Button) findViewById(R.id.complex);

		simple.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				launchSimple();
			}
		});
		
		complex.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				launchComplex();
			}


		});
		
	}

	private void launchComplex() {
		Intent intent = new Intent(this, ControlComplexActivity.class);
		startActivity(intent);
		finish();
	}
	private void launchSimple() {
		Intent intent = new Intent(this, ControlSimpleActivity.class);
		startActivity(intent);
		finish();
	}

}
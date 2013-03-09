package edu.sou.rover2013;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Activity that shows available control modes, and allows selection.
// TODO replace with preference screen. No need for whole activity.
public class ModeSelectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_mode_select);

		final Button simple = (Button) findViewById(R.id.simple);

		simple.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				launchSimple();
			}
		});
	}

	protected void launchSimple() {
		Intent intent = new Intent(this, ControlSimpleActivity.class);
		startActivity(intent);
	}

}
package edu.sou.rover2013;

import android.os.Bundle;

// Activity that shows available control modes, and allows selection.
// TODO replace with preference screen. No need for whole activity.
public class ModeSelectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_mode_select);
	}

}
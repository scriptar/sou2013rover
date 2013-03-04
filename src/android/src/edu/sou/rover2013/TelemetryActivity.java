package edu.sou.rover2013;

import android.os.Bundle;

// Will display information regarding rover output/sensors here.
// TODO replace with preference-style popup, rather than whole activity
public class TelemetryActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_telemetry);
	}
	
}
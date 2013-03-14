package edu.sou.rover2013.activities;

import android.os.Bundle;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;

/**
 *  Will display information regarding rover output/sensors here.
 * 
 * @author Ryan Dempsey
 *
 */
// TODO replace with preference-style popup, rather than whole activity
public class TelemetryActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_telemetry);
	}
	
}
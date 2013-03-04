package edu.sou.rover2013;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

//Simply pulling out common activity code into parent class
public class BaseActivity extends Activity {

	@Override
	// Inflate the menu; adds items to the action bar if it is present.
	public boolean onCreateOptionsMenu(Menu menu) {
		// call parent to include system items as well
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return true;
	}

	@Override
	// Handles menu item selection, returns true if successful
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.bluetooth_settings:
			intent = new Intent(this, ConnectionActivity.class);
			startActivity(intent);
			return true;
		case R.id.telemetry_settings:
			intent = new Intent(this, TelemetryActivity.class);
			startActivity(intent);
			return true;
		case R.id.control_mode_settings:
			intent = new Intent(this, ModeSelectActivity.class);
			startActivity(intent);
			return true;
		case R.id.exit:
			// TODO have events cascade-close, rather than simply returning to
			// prior activity.
			System.exit(0);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

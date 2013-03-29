package edu.sou.rover2013;

import edu.sou.rover2013.activities.ConnectionActivity;
import edu.sou.rover2013.activities.ModeSelectActivity;
import edu.sou.rover2013.activities.TelemetryActivity;
import edu.sou.rover2013.activities.WiFiServerActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Pulling common activity code for our application into a parent abstract
 * class. When writing a new activity, extend this rather than Activity to
 * inherit the standard menu bar and actions.
 * 
 * @author Ryan Dempsey
 * 
 */
public abstract class BaseActivity extends Activity {

	@Override
	/**
	 *  Inflate the menu; adds items to the action bar if it is present.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	/**
	 *  Handles menu item selection, returns true if successful
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.bluetooth_settings:
			intent = new Intent(this, ConnectionActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.telemetry_settings:
			intent = new Intent(this, TelemetryActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.control_mode_settings:
			intent = new Intent(this, ModeSelectActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.wifi_server:
			intent = new Intent(this, WiFiServerActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.exit:
			System.exit(0);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

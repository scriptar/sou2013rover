package edu.sou.rover2013;

import edu.sou.rover2013.activities.ConnectionActivity;
import edu.sou.rover2013.activities.ControlComplexActivity;
import edu.sou.rover2013.activities.ControlSimpleActivity;
import edu.sou.rover2013.activities.ControlWebActivity;
import edu.sou.rover2013.activities.TelemetryActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Pulling common activity code for our application into a parent abstract
 * class. When writing a new activity, extend this rather than Activity to
 * inherit standard menu bar and actions.
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
			// Display Selection List
			// User can select one of two options, and appropriate control mode
			// is launched.
			final String simpleMode = "Simple Direct Control";
			final String complexMode = "Advanced Programming Control";
			final String webMode = "Web Control";
			final CharSequence[] list = { simpleMode, complexMode, webMode };
			// Build list dialog box
			AlertDialog.Builder chooserBox = new AlertDialog.Builder(this);
			chooserBox.setTitle("Select a Rover Control Mode:");
			chooserBox.setItems(list, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (list[item].equals(simpleMode)) {
						Intent intent = new Intent(getApplicationContext(),
								ControlSimpleActivity.class);
						startActivity(intent);
						finish();
					} else if (list[item].equals(complexMode)) {
						Intent intent = new Intent(getApplicationContext(),
								ControlComplexActivity.class);
						startActivity(intent);
						finish();
					}else if (list[item].equals(webMode)) {
						Intent intent = new Intent(getApplicationContext(),
								ControlWebActivity.class);
						startActivity(intent);
						finish();
					}
				}
			});
			AlertDialog alert = chooserBox.create();
			alert.show();
			return true;
		case R.id.exit:
			//Exited Dialog
			this.finish();
		default:
			//Nothing Selected
			return super.onOptionsItemSelected(item);
		}
	}
}
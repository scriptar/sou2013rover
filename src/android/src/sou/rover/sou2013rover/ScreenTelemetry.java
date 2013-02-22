package sou.rover.sou2013rover;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ScreenTelemetry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_telemetry);
	}

	@Override
	// Inflate the menu; adds items to the action bar if it is present.	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return true;
	}
	
	@Override
	// Handles menu item selection, returns true if successful
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.bluetooth_settings:
			// Open Bluetooth Activity
			Log.d("test", "Open Bluetooth Activity");
			intent = new Intent(this, ScreenBluetooth.class);
			startActivity(intent);
			//
			return true;
		case R.id.telemetry_settings:
			// Open Console & Sensor Activity
			Log.d("test", "Open Console & Sensor Activity");
			intent = new Intent(this, ScreenTelemetry.class);
			startActivity(intent);
			return true;
		case R.id.control_mode_settings:
			// Open Mode Selection Menu
			Log.d("test", "Open Mode Selection Menu");
			intent = new Intent(this, ScreenModeSelect.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

}

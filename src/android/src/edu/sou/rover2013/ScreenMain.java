package edu.sou.rover2013;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


//Initial Activity
//Will be initial screen, with menu options and instructions on connecting rover. 
//Creates menu, launches other activities.

public class ScreenMain extends Activity {

	//Lifecycle Method List
	//	protected void onCreate(Bundle savedInstanceState);
	//	protected void onStart();
	//	protected void onRestart();
	//	protected void onResume();
	//	protected void onPause();
	//	protected void onStop();
	//	protected void onDestroy();
	
	// TODO find a way to implement menu in all activities, rather than dupe code.
	// TODO Replace bluetooth, mode select, and telemetry activities with proper preference screens
	// TODO find a way to have bluetooth persist through application, rather than activities.
	

	@Override
	// Called when activity is first created
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_main);
	}

	@Override
	// Inflate the menu; adds items to the action bar if it is present.
	public boolean onCreateOptionsMenu(Menu menu) {
		//call parent to include system items as well
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
			intent = new Intent(this, ScreenBluetooth.class);
			startActivity(intent);
			return true;
		case R.id.telemetry_settings:
			intent = new Intent(this, ScreenTelemetry.class);
			startActivity(intent);
			return true;
		case R.id.control_mode_settings:
			intent = new Intent(this, ScreenModeSelect.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


}

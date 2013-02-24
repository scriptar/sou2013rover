package edu.sou.rover2013;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.sou.rover2013.RogoApplication;

public class ScreenBluetooth extends Activity {

	private static final int REQUEST_ENABLE_BT = 5;
	private RogoApplication app;
	private BluetoothConnection bluetooth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_bluetooth);

		app = (RogoApplication) getApplication();

		// in-line button listener
		final Button button = (Button) findViewById(R.id.button_test_bluetooth);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Log.d("test", "Button Pressed");
				testBluetooth();
			}
		});
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

	// Just a temporary non-oop function where I'm trying to hash out how
	// Bluetooth
	// functions in Android. Will be broken up and moved. --Ryan
	protected void testBluetooth() {

		bluetooth = app.getBluetoothConnection();

		if (!bluetooth.isAvailable()) {
			Log.d("test", "Device does not support Bluetooth");
			return;
		} else {
			Log.d("test", "Device supports Bluetooth");
		}

		if (!bluetooth.isEnabled()) {
			Log.d("test", "Bluetooth Not Enabled, Enabling");
			bluetooth.enableBluetooth();
		} else {
			Log.d("test", "Bluetooth Already Enabled");
		}

		Log.d("test", "Available Device Count: " + bluetooth.pairedDeviceCount());
		//
		// BluetoothDevice lastDevice = null;
		// if (bluetooth.pairedDeviceCount() > 0) {
		// // Loop through paired devices
		// TextView text = (TextView) findViewById(R.id.textView2);
		// text.setText("");
		// text.append("List of paired devices:\n");
		//
		// for (BluetoothDevice device : bluetooth.pairedDevices()) {
		// // For each device, add name & address to label
		// text.append(device.getName() + " " + device.getAddress() + "\n");
		// lastDevice = device;
		// }
		//
		// UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		// // Standard
		// // SerialPortService
		// // ID
		// BluetoothSocket mmSocket;
		// OutputStream mmOutputStream;
		// InputStream mmInputStream = null;
		// try {
		// mmSocket = lastDevice.createRfcommSocketToServiceRecord(uuid);
		// mmSocket.connect();
		// mmOutputStream = mmSocket.getOutputStream();
		// mmInputStream = mmSocket.getInputStream();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// Log.v("test", "Bluetooth connected.");
		//
		//
		// Log.v("test", "output"+app.getBluetoothConnection());
		//
		// //do {
		// // text.append(mmInputStream.toString());
		// //} while (true);
		//
		// }
	}
}

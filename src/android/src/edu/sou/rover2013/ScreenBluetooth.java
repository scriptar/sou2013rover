package edu.sou.rover2013;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScreenBluetooth extends Activity {

	private RogoApplication app;
	private BluetoothConnection bluetooth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_bluetooth);

		app = (RogoApplication) getApplication();
		bluetooth = app.getBluetoothConnection();

		// inline button listeners
		final Button button1 = (Button) findViewById(R.id.button_enable_bluetooth);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.output);
				if (!bluetooth.isEnabled()) {
					bluetooth.enableBluetooth();
					outputText.append("Bluetooth Enabled\n");
				} else {
					outputText.append("Bluetooth Already Enabled\n");
				}
			}
		});

		final Button button2 = (Button) findViewById(R.id.button_disable_bluetooth);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.output);
				if (bluetooth.isEnabled()) {
					bluetooth.disableBluetooth();
					outputText.append("Bluetooth Disabled\n");
				} else {
					outputText.append("Bluetooth Already Disabled\n");
				}
			}
		});

		final Button button3 = (Button) findViewById(R.id.button_list_devices);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.output);
				outputText.append("\nList of paired & available devices:\n");
				for (BluetoothDevice device : bluetooth.pairedDevices()) {
					outputText.append("   " + device.getName() + " "
							+ device.getAddress() + "\n");
				}
				outputText.append("\n");
			}
		});

		final Button button4 = (Button) findViewById(R.id.button_connect_bluetooth);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				testBluetooth();
			}
		});

	}

	// Just a temporary non-oop function where I'm trying to hash out how
	// Bluetooth functions in Android. Will be moved. --Ryan

	protected void testBluetooth() {
		TextView outputText = (TextView) findViewById(R.id.output);
		outputText.append("\n");

		if (!bluetooth.isBluetoothCapable()) {
			outputText.append("Android: Does not support Bluetooth\n");
			return;
		} else {
			outputText.append("Android: Supports Bluetooth\n");
		}

		if (!bluetooth.isEnabled()) {
			outputText.append("Status: Bluetooth not enabled, enabling\n");
			bluetooth.enableBluetooth();
		} else {
			outputText.append("Status: Bluetooth Enabled\n");
		}

		outputText.append("Paired devices Count: "
				+ bluetooth.pairedDeviceCount() + "\n");

		if (bluetooth.pairedDeviceCount() <= 0) {
			outputText.append("No Devices Available\n");
			return;
		}

		// TODO Replace with dynamic list view allowing selection
		outputText.append("List of paired devices:\n");
		BluetoothDevice rover = null;
		for (BluetoothDevice device : bluetooth.pairedDevices()) {
			outputText.append("   " + device.getName() + " "
					+ device.getAddress() + "\n");
			rover = device;
		}

		if (bluetooth.isConnected()) {
			outputText.append("Bluetooth Connection Already Exists\n");
			return;
		}

		if (bluetooth.connectDevice(rover)) {
			outputText.append("Bluetooth connection established\n");
		} else {
			outputText.append("Bluetooth connection failed\n");
		}
		outputText.append("\n");

		InputStream input = bluetooth.getMmInputStream();
		String packet = "";

		while (true) {
			try {
				int get_byte = input.read();
				char get_char = (char) get_byte;

				if (get_char != '\u0003') {
					Log.v("test", "Adding packet raw data: \"" + get_char
							+ "\"");
					packet += get_char;
				} else {
					Log.v("test", "Packet received:\n" + packet);
					outputText.append("Packet Contents: packet");
					packet = "";
				}
			} catch (IOException e) {
				Log.v("test", "Connection lost.");
				break;
			}
		}

	}

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
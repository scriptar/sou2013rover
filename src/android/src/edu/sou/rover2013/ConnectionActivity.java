package edu.sou.rover2013;

import java.io.IOException;
import java.io.InputStream;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConnectionActivity extends BaseActivity {

	private RogoApplication app;
	private WirelessConnection connection;
	private TextView outputText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_bluetooth);

		app = (RogoApplication) getApplication();
		connection = app.getWirelessConnection();
		outputText = (TextView) findViewById(R.id.outputText);

		// inline button listeners
		final Button button1 = (Button) findViewById(R.id.button_enable_bluetooth);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.outputText);
				if (!WirelessConnection.isBluetoothEnabled()) {
					try {
						connection.enableBluetooth();
						outputText.append("Bluetooth Enabled\n");
					} catch (Exception e) {
						outputText.append(e.toString() + "\n");
					}
				} else {
					outputText.append("Bluetooth Already Enabled\n");
				}
			}
		});

		final Button button2 = (Button) findViewById(R.id.button_disable_bluetooth);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.outputText);
				if (WirelessConnection.isBluetoothEnabled()) {
					try {
						connection.disableBluetooth();
						outputText.append("Bluetooth Disabled\n");
					} catch (Exception e) {
						outputText.append(e.toString());
						outputText.append("\n");
					}
				} else {
					outputText.append("Bluetooth Already Disabled\n");
				}
			}
		});

		final Button button3 = (Button) findViewById(R.id.button_list_devices);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.outputText);
				outputText.append("List of paired devices:\n");
				try {
					for (BluetoothDevice device : connection.pairedDevices()) {
						outputText.append("   " + device.getName() + " "
								+ device.getAddress() + "\n");
					}
				} catch (Exception e) {
					outputText.append(e.toString() + "\n");
				}
			}
		});

		final Button button4 = (Button) findViewById(R.id.button_connect_bluetooth);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				connectBluetooth();
			}
		});

		final Button button5 = (Button) findViewById(R.id.display_data);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				displayBufferedInput();
			}
		});

		final Button button6 = (Button) findViewById(R.id.Clear);
		button6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				outputText.setText("");
			}
		});

		final Button button7 = (Button) findViewById(R.id.ToggleLight);
		button7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				toggleRemoteLight();
			}
		});

	}

	// Create a Bluetooth Connection
	private void connectBluetooth() {

		BluetoothDevice rover = null;

		// The code below selects the last item listed as the device to connect
		// to.
		// TODO Replace with dynamic list view allowing selection
		outputText.append("List of paired devices:\n");
		try {
			for (BluetoothDevice device : connection.pairedDevices()) {
				outputText.append("   " + device.getName() + " "
						+ device.getAddress() + "\n");
				rover = device;
			}
		} catch (Exception e) {
			outputText.append(e.toString() + "\n");
			return;
		}
		outputText.append("Device Selected for Connection: " + rover.getName()
				+ "\n");

		// Attempt Connection
		try {
			connection.connectDevice(rover);
			outputText.append("Bluetooth connection established\n");
		} catch (Exception e) {
			outputText.append("Bluetooth connection failed\n");
			outputText.append("Error: " + e.toString() + "\n");
		}
	}

	/**
	 * Grabs Bluetooth Buffered Input, and prints it on the screen.
	 * TODO Eventually, buffered input will be handled by Wireless Connection Class.
	 */
	private void displayBufferedInput() {
		if (!connection.isConnected()) {
			outputText.append("No Bluetooth Connection\n");
			return;
		}
		InputStream input = connection.getInputStream();
		byte[] buffer = new byte[1024];
		try {
			int r = input.read(buffer);
			String message = new String(buffer, 0, r);
			outputText.setText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simple transmit example, used to change a single pin through bluetooth
	 */
	private void toggleRemoteLight() {
		if(connection.transmitByte((byte) 1)){
			outputText.append("Light Toggle Transmitted.\n");
		} else {
			outputText.append("Light Toggle Failed.\n");	
		}
		
	}
}
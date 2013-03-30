package edu.sou.rover2013.activities;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.utility.BluetoothService;

/**
 * View for establishing a bluetooth connection. Currently very cluttered, is in
 * testing mode. Will clean up massively, simplify, etc.
 * 
 * @author Ryan Dempsey
 * 
 */
public class ConnectionActivity extends BaseActivity {

	private BluetoothService connection;
	private TextView outputText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);

		connection = BluetoothService.getConnection();

		outputText = (TextView) findViewById(R.id.outputText);

		// inline button listeners
		final Button button1 = (Button) findViewById(R.id.button_enable_bluetooth);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView outputText = (TextView) findViewById(R.id.outputText);
				if (!BluetoothService.isBluetoothEnabled()) {
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
				if (BluetoothService.isBluetoothEnabled()) {
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
				outputText.append("List of bonded devices:\n");
				try {
					for (BluetoothDevice device : connection
							.getBondedBluetoothDevices()) {
						outputText.append("   " + device.getName() + " "
								+ device.getAddress() + "\n");
					}
				} catch (Exception e) {
					outputText.append(e.getLocalizedMessage()+"\n");
				}
			}
		});
		final Button button4 = (Button) findViewById(R.id.button_connect_bluetooth);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				connectBluetooth();
			}
		});
		final Button button6 = (Button) findViewById(R.id.Clear);
		button6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				outputText.setText("");
				Toast.makeText(getApplicationContext(), "Console Cleared",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// Create a Bluetooth Connection
	private void connectBluetooth() {

		BluetoothDevice rover = null;

		// The code below selects the last item listed as the device to connect
		// to.
		// TODO Replace with dynamic list view allowing selection
		outputText.append("List of bonded devices:\n");
		try {
			for (BluetoothDevice device : connection
					.getBondedBluetoothDevices()) {
				outputText.append("   " + device.getName() + " "
						+ device.getAddress() + "\n");
				rover = device;
			}
		} catch (Exception e) {
			outputText.append(e.getLocalizedMessage()+"\n");
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

}
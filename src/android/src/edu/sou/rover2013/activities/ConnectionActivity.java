package edu.sou.rover2013.activities;

import java.util.ArrayList;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.utility.*;

/**
 * View for establishing a bluetooth connection. Currently very cluttered, is in
 * testing mode. Will clean up massively, simplify, etc.
 * 
 * @author Ryan Dempsey
 * 
 */
public class ConnectionActivity extends BaseActivity {

	// Constants
	protected static final int REQUEST_ENABLE_BT = 200;

	// Class Variables
	private BluetoothService connection;
	private ArrayList<BluetoothDevice> devices;
	private ArrayAdapter<BluetoothDevice> devicesArrayAdapter;
	private BroadcastReceiver mReceiver;

	// ***************************
	// UI Elements
	// ***************************
	private TextView outputText;
	private Button buttonEnableBluetooth;
	private Button buttonDisableBluetooth;
	private Button buttonStartDiscovery;
	private ListView listviewDevices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);

		// Variables
		connection = BluetoothService.getConnection();

		// devices.clear();
		devices = new ArrayList<BluetoothDevice>();
		devicesArrayAdapter = new ArrayAdapter<BluetoothDevice>(this,
				android.R.layout.simple_list_item_1, devices);

		// ***************************
		// Set UI Elements
		// ***************************
		outputText = (TextView) findViewById(R.id.outputText);
		outputText.setText("");
		buttonEnableBluetooth = (Button) findViewById(R.id.button_enable_bluetooth);
		buttonDisableBluetooth = (Button) findViewById(R.id.button_disable_bluetooth);
		buttonStartDiscovery = (Button) findViewById(R.id.button_start_discovery);
		listviewDevices = (ListView) findViewById(R.id.listview_devices);
		listviewDevices.setAdapter(devicesArrayAdapter);
		// Allows only one item to be selected
		listviewDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// Selected Item gets this color
		listviewDevices.setSelector(android.R.color.darker_gray);
		listviewDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				String item = ((TextView) view).getText().toString();
				outputText.append("Connecting to: "+item+"\n");
				Toast.makeText(getBaseContext(), "Connecting to: "+item, Toast.LENGTH_LONG).show();
				//Cancels search if ongoing
				   if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
					   BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			        }
				connection.connectDevice(item);
			}
		});

		// ***************************
		// Button Listeners
		// ***************************
		// Enable Bluetooth
		buttonEnableBluetooth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!BluetoothService.isBluetoothCapable()) {
					outputText.append("Bluetooth Not Supported\n");
					return;
				}
				if (BluetoothService.isEnabled()) {
					outputText.append("Bluetooth Already Enabled\n");
					return;
				}
				outputText.append("Enabling Bluetooth...\n");
				// connection.enableBluetooth();
				if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
			}
		});
		// Disable Bluetooth
		buttonDisableBluetooth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!BluetoothService.isBluetoothCapable()) {
					outputText.append("Bluetooth Not Supported\n");
					return;
				}
				if (!BluetoothService.isEnabled()) {
					outputText.append("Bluetooth Already Disabled\n");
					return;
				}
				outputText.append("Disabling Bluetooth...\n");
				connection.disableBluetooth();
				outputText.append("Bluetooth Disabled\n");
				letsToast("Bluetooth Disabled");
			}
		});
		// Start Device Discovery
		buttonStartDiscovery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!BluetoothService.isBluetoothCapable()) {
					outputText.append("Bluetooth Not Supported\n");
					return;
				}
				if (!BluetoothService.isEnabled()) {
					outputText.append("Bluetooth Not Enabled\n");
					return;
				}
				devices.clear();
				BluetoothAdapter.getDefaultAdapter().startDiscovery();
				outputText.append("Device Discovery Started\n");
				letsToast("Device Discovery Started");
			}
		});

		// ***************************
		// Broadcast Receiver
		// ***************************
		// Responds to broadcasts listing discovered devices
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When a device is found
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					outputText.append(device.getName() + "\n"
							+ device.getAddress() + "\n");
					// http://developer.android.com/guide/topics/ui/declaring-layout.html#AdapterViews
					devices.add(device);
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
						.equals(action)) {
					outputText.append("Device Discovery Completed\n");
					letsToast("Device Discovery Completed");
				}
			}
		};
		// Registering our custom broadcast receiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);

	}

	// Takes action on return of Bluetooth enable activity.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				outputText.append("Bluetooth Enabled\n");
				letsToast("Bluetooth Enabled");
			} else {
				outputText.append("Bluetooth NOT Enabled\n");
				letsToast("Bluetooth NOT Enabled");
			}
		}
	}

	// simple way to throw alerts on the string.
	private void letsToast(String string_arg) {
		Toast toast = Toast.makeText(getApplicationContext(), string_arg,
				Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}

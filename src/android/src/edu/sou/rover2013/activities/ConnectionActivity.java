package edu.sou.rover2013.activities;

import java.util.ArrayList;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.utility.*;

/**
 * Activity for establishing a bluetooth connection.
 */
// TODO Have thread perform connection to prevent hang
// TODO Don't allow connections until discovery reports that it is
// cancelled/completed.
// TODO Debug Connection Issues
// TODO Load Current status on activity launch, and enable/disable buttons to
// fit.
// TODO Show current status
// TODO Show alert when connection succeeds
// TODO Currently crashes when rover disconnected yet we push reset. Connection
// went void?
// TODO Show Device Name, rather than Address
public class ConnectionActivity extends BaseActivity {

	// *******************************
	// Class Constants
	// *******************************
	protected static final int REQUEST_ENABLE_BT = 200;
	// Valid rovers
	private static final String rover1 = "Jeff's Rover";
	private static final String rover1Address = "00:06:66:4B:FA:07";
	private static final String rover2 = "Shaun's Rover";
	private static final String rover2Address = "00:06:66:4C:00:43";
	private static final String rover3 = "Ryan's Rover";
	private static final String rover3Address = "00:06:66:4B:F2:13";
	private static final String[] roverAddresses = { rover1Address,
			rover2Address, rover3Address };
	// *******************************
	// Class Fields
	// *******************************
	private BluetoothService connection;
	private ArrayList<String> devices;
	private ArrayAdapter<String> devicesAdapter;

	// ***************************
	// UI Elements
	// ***************************
	private Button buttonResetConnection;
	private Button buttonStartDiscovery;
	private ListView listviewDevices;
	private TextView textStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);

		// Assign Variables
		connection = BluetoothService.getConnection();
		devices = new ArrayList<String>();
		devicesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, devices);

		// ***************************
		// Assign UI Elements
		// ***************************
		buttonResetConnection = (Button) findViewById(R.id.button_reset);
		buttonStartDiscovery = (Button) findViewById(R.id.button_start_discovery);
		listviewDevices = (ListView) findViewById(R.id.listview_devices);
		textStatus = (TextView) findViewById(R.id.text_status);

		listviewDevices.setAdapter(devicesAdapter);
		// Allows only one selected item
		// listviewDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// Sets selected item color
		listviewDevices.setSelector(android.R.color.darker_gray);

		// *******************************
		// Button Listeners
		// *******************************
		buttonStartDiscovery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				beginDeviceSearch();
			}

		});
		buttonResetConnection.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				resetConnection();
				textStatus.setText("Disconnected");
				toast("Connection Reset");
			}
		});
		/**
		 * Begins Connection when item selected
		 */
		listviewDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = ((TextView) view).getText().toString();
				// Cancel discovery process
				if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
				} else {
					// Connecting
					toast("Connecting to: " + item);
					textStatus.setText("Connecting");
					buttonStartDiscovery.setEnabled(false);
					if (item.equals(rover1)) {
						connection.connectDevice(rover1Address);
					} else if (item.equals(rover2)) {
						connection.connectDevice(rover2Address);
					} else if (item.equals(rover3)) {
						connection.connectDevice(rover3Address);
					}
				}
			}
		});

	}

	// *******************************
	// Button Methods
	// *******************************
	/**
	 * Begins Bluetooth Device Search. Enables Bluetooth first if not enabled,
	 * and then uses callback to continue discovery.
	 */
	private void beginDeviceSearch() {
		if (!BluetoothService.isBluetoothCapable()) {
			toast("Bluetooth Not Supported");
			return;
		}
		if (!BluetoothService.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			resetConnection();
			textStatus.setText("Searching");
			toast("Rover Discovery Started...");
			BluetoothAdapter.getDefaultAdapter().startDiscovery();
			buttonStartDiscovery.setEnabled(false);
			buttonResetConnection.setEnabled(false);
			listviewDevices.setEnabled(false);
		}
	}

	/**
	 * Resets the connection
	 */
	private void resetConnection() {
		connection.reset();
		devices.clear();
		devicesAdapter.notifyDataSetChanged();
		buttonStartDiscovery.setEnabled(true);
		buttonResetConnection.setEnabled(true);
	}

	// *******************************
	// Additional Methods
	// *******************************
	/**
	 * Takes action on return of Bluetooth enable activity. Begins Device
	 * Discovery, as that button prompts the user to enable bluetooth.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				beginDeviceSearch();
			} else {
				toast("Bluetooth Not Enabled");
			}
		}
	}

	// *******************************
	// Broadcast Receiver Methods
	// *******************************
	protected void broadcastReceiverBluetoothDeviceDiscoveryDone() {
		toast("Device Discovery Completed");
		textStatus.setText("Ready");
		buttonStartDiscovery.setEnabled(true);
		buttonResetConnection.setEnabled(true);
		listviewDevices.setEnabled(true);

	}

	/**
	 * Receives all info on incoming Bluetooth devices, and takes action if
	 * valid rover is found.
	 */
	protected void broadcastReceiverBluetoothDeviceFound(Intent intent) {
		// Found Device
		BluetoothDevice device = intent
				.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		for (String s : roverAddresses) {
			int i = s.indexOf(device.getAddress());
			if (i >= 0) {
				if (device.getAddress().equals(rover1Address)) {
					devices.add(rover1);
				} else if (device.getAddress().equals(rover2Address)) {
					devices.add(rover2);
				} else if (device.getAddress().equals(rover3Address)) {
					devices.add(rover3);
				}
				devicesAdapter.notifyDataSetChanged();
			}
		}
	}

}

package edu.sou.rover2013.activities;

import java.util.ArrayList;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
 * Activity for establishing a bluetooth connection.
 */
// TODO Have thread perform connection to prevent hang
// TODO Don't allow connections until discovery reports that it is cancelled.
// TODO Debug Connection Issues
// TODO Load Current status on activity launch, and enable/disable buttons to fit.
// TODO Show current status
// TODO Show alert when connection succeeds
public class ConnectionActivity extends BaseActivity {

	// *******************************
	// Class Constants
	// *******************************
	protected static final int REQUEST_ENABLE_BT = 200;

	// *******************************
	// Class Variables
	// *******************************
	private BluetoothService connection;
	private ArrayList<BluetoothDevice> devices;
	private ArrayAdapter<BluetoothDevice> devicesAdapter;
	private BroadcastReceiver mReceiver;

	// ***************************
	// UI Elements
	// ***************************
	private Button buttonResetConnection;
	private Button buttonStartDiscovery;
	private ListView listviewDevices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);

		// Assign Variables
		connection = BluetoothService.getConnection();
		devices = new ArrayList<BluetoothDevice>();
		devicesAdapter = new ArrayAdapter<BluetoothDevice>(this,
				android.R.layout.simple_list_item_1, devices);

		// ***************************
		// Assign UI Elements
		// ***************************
		buttonResetConnection = (Button) findViewById(R.id.button_reset);
		buttonStartDiscovery = (Button) findViewById(R.id.button_start_discovery);
		listviewDevices = (ListView) findViewById(R.id.listview_devices);
		listviewDevices.setAdapter(devicesAdapter);
		// Allows only one selected item
		listviewDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// Sets selected item color
		listviewDevices.setSelector(android.R.color.darker_gray);

		// *******************************
		// Button Listeners
		// *******************************
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
					toast("Cancelling Discovery...");
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
				}
				toast("Connecting to: " + item);
				connection.connectDevice(item);
				buttonStartDiscovery.setEnabled(false);
				listviewDevices.setEnabled(false);
			}
		});

		buttonResetConnection.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				resetConnection();
				toast("Connection Reset");
			}
		});
		buttonStartDiscovery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				beginDeviceSearch();
			}

		});

		// ***************************
		// Setup Broadcast Receiver
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
					toast("Found " + device.getName() + "\n"
							+ device.getAddress());
					// http://developer.android.com/guide/topics/ui/declaring-layout.html#AdapterViews
					devices.add(device);
					devicesAdapter.notifyDataSetChanged();
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
						.equals(action)) {
					toast("Device Discovery Completed");
					buttonStartDiscovery.setEnabled(true);
					buttonResetConnection.setEnabled(true);
				}
			}
		};
		// Registering our custom broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
	}
	
	// *******************************
	// Button Methods
	// *******************************
	/**
	 * Begins Bluetooth Device Search
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
			toast("Enabling Bluetooth...");
		}
		resetConnection();
		BluetoothAdapter.getDefaultAdapter().startDiscovery();
		toast("Device Discovery Started...");
		buttonStartDiscovery.setEnabled(false);
		buttonResetConnection.setEnabled(false);		
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
		listviewDevices.setEnabled(true);
	}

	// *******************************
	// Additional Methods
	// *******************************
	/**
	 * Takes action on return of Bluetooth enable activity
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				toast("Bluetooth Enabled");
			} else {
				toast("Bluetooth Not Enabled");
			}
		} 
	}

	/**
	 * Throws a short toast notification
	 */
	private void toast(String string_arg) {
		Toast toast = Toast.makeText(getApplicationContext(), string_arg,
				Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 *Removes the broadcast receiver on activity close 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}

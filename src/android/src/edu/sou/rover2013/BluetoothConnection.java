package edu.sou.rover2013;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

// This class handles creation of Android Bluetooth connections
public class BluetoothConnection {

	private static UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805f9b34fb");

	private BluetoothAdapter adapter = null;
	private BluetoothDevice device = null;
	private BluetoothSocket mmSocket = null;
	private OutputStream mmOutputStream = null;
	private InputStream mmInputStream = null;
	public InputStream getMmInputStream() {
		return mmInputStream;
	}

	public void setMmInputStream(InputStream mmInputStream) {
		this.mmInputStream = mmInputStream;
	}

	public OutputStream getMmOutputStream() {
		return mmOutputStream;
	}

	public void setMmOutputStream(OutputStream mmOutputStream) {
		this.mmOutputStream = mmOutputStream;
	}

	// TODO Consider adding pauses after enabling/disabling bluetooth
	
	// TODO check for status before operations. Will fail if changing
	
	// TODO Data Streams... how are we going to handle input/output?

	// Constructor
	public BluetoothConnection() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	// Check to see if bluetooth exists on device
	public boolean isBluetoothCapable() {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			return false;
		} else {
			return true;
		}
	}

	// Check to see if Bluetooth is currently enabled
	public boolean isEnabled() {
		if (!isBluetoothCapable()) {
			return false;
		}
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			return true;
		} else {
			return false;
		}
	}

	// TODO Currently connected?
	public boolean isConnected() {
		if (mmSocket == null) {
			return false;
		}
			return true;
		//return mmSocket.isConnected();
	}

	// Enables Bluetooth
	// TODO add error catching if not supported
	public void enableBluetooth() {
		if (!isEnabled())
			adapter.enable();
	}

	// Disables Bluetooth
	// TODO add error catching if not supported
	public void disableBluetooth() {
		if (isEnabled())
			adapter.disable();
	}

	// Returns a set containing all paired devices
	public Set<BluetoothDevice> pairedDevices() {
		return adapter.getBondedDevices();
	}

	// Returns count of paired devices.
	public int pairedDeviceCount() {
		return adapter.getBondedDevices().size();
	}

	// allows Bluetooth Device selection
	public void selectDevice(BluetoothDevice object) {
		device = object;
	}

	// Establishes connection with currently selected device
	public boolean connectSelectedDevice() {
		return connectDevice(device);
	}

	// Connection Code, bluetooth to device
	public boolean connectDevice(BluetoothDevice object) {
		try {
			mmSocket = object.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
			mmInputStream = mmSocket.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
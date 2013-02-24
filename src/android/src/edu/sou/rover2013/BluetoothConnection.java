package edu.sou.rover2013;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.bluetooth.*;

// This class handles creation of Android Bluetooth connections
public class BluetoothConnection {

	private BluetoothAdapter adapter;
	private BluetoothDevice device;

	// Constructor
	public BluetoothConnection() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	// Check to see if bluetooth exists on device
	public boolean isAvailable() {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			return false;
		} else {
			return true;
		}
	}

	// Check to see if Bluetooth is currently enabled
	public boolean isEnabled() {
		if (!isAvailable()) {
			return false;
		}
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			return true;
		} else {
			return false;
		}
	}

	// TODO Currently connected?
	public boolean isConnected(){
		return false;
	}

	// Enables Bluetooth
	public void enableBluetooth() {
		if (!isEnabled())
			BluetoothAdapter.getDefaultAdapter().enable();
	}

	// Disables Bluetooth
	public void disableBluetooth() {
		if (isEnabled())
			BluetoothAdapter.getDefaultAdapter().disable();
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
	public void selectDevice(BluetoothDevice item){
		device = item;
	}
	
	// Establishes connection with currently selected device
	public boolean connectDevice(){
		return connectDevice(device);
	}

	// TODO Connection Code here
	public boolean connectDevice(BluetoothDevice object){
		//connect to device...
		//False if not connected.
		return true;
	}
	
}

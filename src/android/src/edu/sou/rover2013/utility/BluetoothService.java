package edu.sou.rover2013.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;

/**
 * This class handles Bluetooth connections
 */
// TODO check for status before operations. Will fail if changing
public class BluetoothService{

	// *****************
	// Class Constants
	// *****************
	// Singleton
	private static BluetoothService singleton;
	// Bluetooth ID to look for when searching for BlueSmirf Module
	private final static UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805f9b34fb");
	public static final int REQUEST_ENABLE_BT = 200;

	// *****************
	// Instance Vars
	// *****************
	private final BluetoothAdapter adapter;
	private BluetoothSocket bluetoothSocket = null;
	private BluetoothServerSocket bluetoothServerSocket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;

	/**
	 * Constructor - Only accessed through Singleton
	 */
	private BluetoothService() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Returns Bluetooth Singleton
	 */
	public static BluetoothService getConnection() {
		if (singleton == null) {
			singleton = new BluetoothService();
			return singleton;
		} else
			return singleton;
	}

	/**
	 * Returns true if device is Bluetooth capable, false if not.
	 * 
	 * @return whether the device supports Bluetooth
	 */
	public static boolean isBluetoothCapable() {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns true if Bluetooth is enabled, false if disabled or unavailable
	 * 
	 * @return whether Bluetooth is enabled or not
	 */
	public static boolean isEnabled() {
		if (!isBluetoothCapable()) {
			return false;
		}
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			return true;
		} else {
			return false;
		}
	}

	public void connectDevice(String address){
		BluetoothDevice device = adapter.getRemoteDevice(address);
		// Connection Attempt
		// TODO don't hang app while connecting
		try {
			bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			outStream = bluetoothSocket.getOutputStream();
			inStream = bluetoothSocket.getInputStream();
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * Enables Bluetooth on Device
	 */
	public void enableBluetooth() {
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			BluetoothAdapter.getDefaultAdapter().enable();
		}
	}
	/**
	 * Disables Bluetooth on Device
	 */
	public void disableBluetooth() {
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			BluetoothAdapter.getDefaultAdapter().disable();
		}
	}


	/**
	 * Transmits single bytes over an established bluetooth connection.
	 * Can use this rather than grabbing the InputStream.
	 * 
	 * @param data
	 *            byte value to transmit
	 * @throws Exception
	 *             if bluetooth is not connected properly
	 * @throws IOException
	 *             if an error occurs while writing data out
	 */
	public void transmitByte(byte data) throws IOException, Exception {
		outStream.write(data);
		outStream.flush();
	}

	/**
	 * Transmits string through open bluetooth connection as a series of UTF-8
	 * bytes
	 * 
	 * @param stringArg
	 *            string to transmit
	 * @throws IOException
	 *             if connection fails or transmission errors
	 * @throws Exception
	 *             if connection not properly formed
	 */
	public void transmitString(String stringArg) throws IOException, Exception {
		byte[] outBytes = stringArg.getBytes();

		System.out.println("Debugging Transmit. Text, then bytes:");
		System.out.println(stringArg);
		for (Byte theByte : outBytes) {
			System.out.print(theByte + " ");
		}
		System.out.println();
		outStream.write(outBytes);
		outStream.flush();
	}

}

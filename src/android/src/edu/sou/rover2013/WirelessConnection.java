package edu.sou.rover2013;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/** This class handles creation of Android-Arduino Bluetooth connections */
public class WirelessConnection extends Activity {

	private BluetoothAdapter adapter = null;
	private BluetoothDevice device = null;
	private BluetoothSocket socket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;
	/** Used when discovering Bluetooth devices. */
	private static UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805f9b34fb");

	// TODO Consider adding pauses after enabling/disabling bluetooth
	// TODO check for status before operations. Will fail if changing

	/**
	 * Default Constructor.
	 */
	public WirelessConnection() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Returns true if Android is Bluetooth capable, false if not.
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
	 */
	public static boolean isBluetoothEnabled() {
		if (!isBluetoothCapable()) {
			return false;
		}
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Enables Bluetooth on Android
	 */
	public void enableBluetooth() throws Exception {
		if (isBluetoothCapable() && !isBluetoothEnabled()) {
			adapter.enable();
		}
	}

	/**
	 * Disables Bluetooth on Android
	 */
	public void disableBluetooth() {
		if (isBluetoothCapable() && isBluetoothEnabled()) {
			adapter.disable();
		}
	}

	/**
	 * Returns true if Android is currently connected to device.
	 * 
	 * // TODO if connection is interrupted, still returns true. Need to add a
	 * // check to return false if connection no longer valid
	 */
	public boolean isConnected() {
		if (socket == null) {
			return false;
		} else if (!socket.isConnected()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns a set containing all paired devices. If disabled or unavailable,
	 * returns null value
	 */
	public Set<BluetoothDevice> pairedDevices() throws Exception {
		if (!isBluetoothCapable()) {
			throw new Exception("Device Not Bluetooth Capable");
		}
		if (!isBluetoothEnabled()) {
			throw new Exception("Device Bluetooth Not Enabled.");
		}
		return adapter.getBondedDevices();
	}

	/**
	 * Returns count of paired devices.
	 */
	public int pairedDeviceCount() throws Exception {
		if (!isBluetoothCapable()) {
			throw new Exception("Device Not Bluetooth Capable");
		}
		if (!isBluetoothEnabled()) {
			throw new Exception("Device Bluetooth Not Enabled.");
		}
		return adapter.getBondedDevices().size();
	}

	/** Allows Bluetooth Device selection from pairedDevices */
	public void selectDevice(BluetoothDevice object) {
		device = object;
	}

	/**
	 * Attempts connection with selected device. Throws Exception if connection
	 * fails or no device selected
	 */
	public void connectSelectedDevice() throws Exception {
		if (device == null) {
			throw new Exception("No device selected.");
		}
		connectDevice(device);
	}

	/**
	 * Attempts connection with passed in device. Get the device from
	 * "pairedDevices" method. Throws Exception if connection fails
	 */
	public void connectDevice(BluetoothDevice object) throws Exception {
		// Check to see if Android is bluetooth capable, and set adapter if so
		if (!isBluetoothCapable()) {
			throw new Exception("Android device not Bluetooth capable.");
		} else {
			adapter = BluetoothAdapter.getDefaultAdapter();
		}

		// Check to see if Android has enabled Bluetooth.
		if (!isBluetoothEnabled()) {
			throw new Exception("Bluetooth not enabled on Android device.");
		}

		// Check to see if connection already open
		if (isConnected()) {
			throw new Exception("Connection already open.");
		}

		// TODO Check for valid passed-in object
		device = object;

		// Connection Attempt
		try {
			socket = device.createRfcommSocketToServiceRecord(uuid);
			socket.connect();
			outStream = socket.getOutputStream();
			inStream = socket.getInputStream();
		} catch (IOException e) {
			throw new Exception("Error establishing connection.");
		}

	}

	/**
	 * Get the Bluetooth input stream. Use this to receive information from the
	 * remote Bluetooth device.
	 * 
	 * @return The input stream as an InputStream object.
	 */
	public InputStream getInputStream() {
		return inStream;
	}

	/**
	 * Get the Bluetooth output stream. Use this to send data to the remote
	 * Bluetooth device.
	 * 
	 * @return The output stream as an OutputStream object.
	 */
	public OutputStream getOutputStream() {
		return outStream;
	}

	/**
	 * Used to transmit single bytes over an established bluetooth connection.
	 * Can use this rather than grabbing the InputStream.
	 * 
	 * @param data
	 *            is a byte value that you wish to transmit
	 * @return returns true if successful, false if an error occured
	 */
	public boolean transmitByte(byte data) {
		if (!isConnected()) {
			return false;
		}
		try {
			outStream.write(data);
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}

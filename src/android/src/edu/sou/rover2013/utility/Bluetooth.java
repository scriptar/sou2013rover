package edu.sou.rover2013.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * This class handles the Android Bluetooth connection. The Rover model uses this.
 * Handles client and server class coordination.
 */
// TODO check for status before operations. Will fail if changing
public class Bluetooth extends Activity {

	// Class Vars
	private static Bluetooth singleton;
	// Specific Bluetooth ID to look for when searching for BlueSmirf Module
	private final static UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805f9b34fb");

	// Instance Vars
	private final BluetoothAdapter adapter;
	private BluetoothSocket bluetoothSocket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;

	/**
	 * Constructor
	 */
	private Bluetooth() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Returns Singleton Instance
	 */
	public static Bluetooth getConnection() {
		if (singleton == null) {
			singleton = new Bluetooth();
			return singleton;
		} else
			return singleton;
	}

	/**
	 * Returns true if Android is Bluetooth capable, false if not.
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
	 * Closes any open connection, and resets Bluetooth service state.
	 */
	public void resetBluetooth() {
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inStream = null;
		}
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			outStream = null;
		}
		if (bluetoothSocket != null) {
			try {
				bluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bluetoothSocket = null;
		}
	}

	/**
	 * Returns true if Android is currently connected to device.
	 * 
	 * @return
	 */
	// TODO if connection is interrupted, still returns true.
	public boolean isConnected() {
		if (bluetoothSocket == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Enables Bluetooth on Android
	 */
	// TODO add pause after enabling bluetooth, check for success
	public void enableBluetooth() throws Exception {
		if (isBluetoothCapable() && !isBluetoothEnabled()) {
			adapter.enable();
		}
	}

	/**
	 * Disables Bluetooth and resets class.
	 */
	// TODO add pause after disabling bluetooth, check for success
	public void disableBluetooth() {
		if (isBluetoothCapable() && isBluetoothEnabled()) {
			adapter.disable();
		}
		resetBluetooth();
	}

	/**
	 * Returns a set containing all bonded Android devices. Not necessarily
	 * connectable, just configured.
	 * 
	 * @return A set containing all paired devices.
	 * @throws Exception
	 *             if bluetooth not supported or enabled.
	 */
	public Set<BluetoothDevice> getBondedBluetoothDevices() throws Exception {
		if (!isBluetoothCapable()) {
			throw new Exception("Bluetooth not available");
		}
		if (!isBluetoothEnabled()) {
			throw new Exception("Bluetooth not enabled");
		}
		return adapter.getBondedDevices();
	}

	/**
	 * Returns a count of bonded Android devices. Not necessarily connectable,
	 * just configured.
	 * 
	 * @return the number of bonded bluetooth devices.
	 * @throws Exception
	 *             if bluetooth not supported or enabled.
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

	/**
	 * Attempts connection with passed in device. Use
	 * "getBondedBluetoothDevices" to get bluetooth .
	 * 
	 * @param object
	 *            the bonded BluetoothDevice to connect to
	 * @throws Exception
	 *             if connection fails. Message contains details.
	 */
	public void connectDevice(BluetoothDevice deviceArg) throws Exception {
		if (!isBluetoothCapable()) {
			throw new Exception("Device not Bluetooth capable");
		}
		if (!isBluetoothEnabled()) {
			throw new Exception("Bluetooth not enabled");
		}
		if (isConnected()) {
			throw new Exception("Connection already open");
		}

		// Connection Attempt
		// TODO don't hang app while connecting
		try {
			bluetoothSocket = deviceArg.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			outStream = bluetoothSocket.getOutputStream();
			inStream = bluetoothSocket.getInputStream();
		} catch (IOException e) {
			resetBluetooth();
			throw new Exception("Error establishing connection");
		}
	}

	/**
	 * Used to transmit single bytes over an established bluetooth connection.
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
		if (!isConnected() || outStream == null) {
			throw new Exception();
		}
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
		if (!isConnected() || outStream == null) {
			throw new Exception();
		}
		
		byte[] outBytes = stringArg.getBytes();
		
		System.out.println("Debugging Transmit. Text, then bytes:");
		System.out.println(stringArg);
		for(Byte theByte : outBytes){
			System.out.print(theByte+" ");
		}
		System.out.println();
		outStream.write(outBytes);
		outStream.flush();
	}

}

package edu.sou.rover2013.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import edu.sou.rover2013.models.Rover;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class handles Bluetooth connections
 */
// TODO check for status before operations. Will fail if changing
public class BluetoothService {

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
	// Class Fields
	// *****************
	private final BluetoothAdapter adapter;
	private BluetoothSocket bluetoothSocket = null;
	private BluetoothServerSocket bluetoothServerSocket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;
	// vars for reader thread
	private boolean stopWorker;
	private int readBufferPosition;
	private byte[] readBuffer;
	private Thread workerThread;
	// one rover model at a time, holds current rover model
	private Rover rover = null;

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

	/**
	 * Returns whether a rover is connected or not.
	 * 
	 * @return Rover Connection established?
	 */
	public boolean isConnected() {
		if (rover == null) {
			return false;
		} else if (bluetoothSocket.isConnected()) {
			return true;
		}
		return false;
	}

	public void connectDevice(String address) {
		BluetoothDevice device = adapter.getRemoteDevice(address);
		// Connection Attempt
		try {
			bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			outStream = bluetoothSocket.getOutputStream();
			inStream = bluetoothSocket.getInputStream();
			rover = new Rover(this);
			startDataListener();
		} catch (IOException e) {

		}
	}

	private void startDataListener() {
		final Handler handler = new Handler();
		//ASCII Newline char
		final byte delimiter = 10;
		//Vars
		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						int bytesAvailable = inStream.available();
						if (bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							inStream.read(packetBytes);
							for (int i = 0; i < bytesAvailable; i++) {
								byte b = packetBytes[i];
								if (b == delimiter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0,
											encodedBytes, 0,
											encodedBytes.length);
									final String data = new String(
											encodedBytes, "US-ASCII");
									readBufferPosition = 0;

									handler.post(new Runnable() {
										public void run() {
											rover.addToRoverOutput(data);
											Log.d("data", data);
										}
									});
								} else {
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					} catch (IOException ex) {
						Log.d("error", "IOException Caught");
						stopWorker = true;
					}
				}
			}
		});

		workerThread.start();
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
			rover = null;
			stopWorker = true;
		}
	}

	/**
	 * Transmits single bytes over an established bluetooth connection. Can use
	 * this rather than grabbing the InputStream.
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

	/**
	 * Returns the current Rover Model
	 * 
	 * @return the current rover model
	 */
	public Rover getRover() {
		return rover;
	}
}

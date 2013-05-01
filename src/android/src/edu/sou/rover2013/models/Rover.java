package edu.sou.rover2013.models;

import java.io.IOException;
import java.util.ArrayList;

import edu.sou.rover2013.utility.BluetoothService;

/**
 * This class represents the Rogo rover. 
 * Holds an established bluetooth Service for communication.
 * All rover/bluetooth communication passes through here. The model maintains a
 * log of actions and errors.
 * 
 * @author Ryan Dempsey
 * 
 */
public class Rover {

	private BluetoothService bluetoothConnection;

	/**
	 * Rover Constructor
	 */
	public Rover() {
		bluetoothConnection = BluetoothService.getConnection();
	}

	/**
	 * Sends Rogo Script to the connected Rover. Scripts will have "start" and
	 * "stop" lines appended for use when receiving on rover.
	 * 
	 * @param scriptArg
	 *            Script to send to the Rogo Rover. Must be properly
	 *            constructed.
	 */
	public void transmitRogoScript(String scriptArg) {

			try {
				bluetoothConnection.transmitString(scriptArg);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

	}



}
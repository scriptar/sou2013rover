package edu.sou.rover2013.models;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;
import android.widget.ListView;

import edu.sou.rover2013.utility.BluetoothService;

/**
 * This class represents the Rogo rover. Holds an established bluetooth Service
 * for communication. All rover/bluetooth communication passes through here.
 */
public class Rover {

	private BluetoothService connection;
	private ArrayList<String> roverOutput;

	/**
	 * Rover Constructor
	 * 
	 * @param connectionArg
	 */
	public Rover(BluetoothService connectionArg) {
		connection = connectionArg;
		roverOutput = new ArrayList<String>(20000);

	}

	/**
	 * Sends Rogo Script to the connected Rover. Scripts will have "start" and
	 * "stop" lines appended for use when receiving on rover.
	 * 
	 * @param scriptArg
	 *            Script to send to the Rogo Rover. Must be properly
	 *            constructed.
	 */
	public void sendDataToRover(String scriptArg) {

		try {
			connection.transmitString(scriptArg);
		} catch (IOException e) {
			e.printStackTrace();
			// Transmit Failure
		} catch (Exception e) {
			e.printStackTrace();
			// Transmit Failure

		}

	}

	/**
	 * Strings sent out by the rover are appended to the rover's output array
	 * with this method.
	 * 
	 * @param data
	 */
	public void addToRoverOutput(String data) {
		if (roverOutput == null) {
			roverOutput = new ArrayList<String>(20000);
		}
		if (data.equals("")) {
			return;
		}
		roverOutput.add(data);
		
	}

	/**
	 * Returns the arraylist containing communication received from the rover.
	 * 
	 * @return the arraylist containing communication received from the rover.
	 */
	public ArrayList<String> getRoverOutput() {
		if (roverOutput == null) {
			roverOutput = new ArrayList<String>(20000);
		}
		return roverOutput;
	}

}
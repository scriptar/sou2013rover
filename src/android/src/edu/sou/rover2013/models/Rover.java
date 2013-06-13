package edu.sou.rover2013.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.sou.rover2013.utility.BluetoothService;

/**
 * This class represents the Rogo rover. Holds an established bluetooth Service
 * for communication. All rover/bluetooth communication passes through here.
 */
public class Rover {

	/*
	 * Question: Do we need to keep all rover output? Any value to this?
	 */

	private BluetoothService connection;
	private ArrayList<String> roverOutput;
	private boolean inDataPacket = false;
	private ArrayList<String> dataPacket;
	
	private StringTokenizer st;

	// Rover Values
	private int pingF;
	private int irFR;
	private int irFL;
	private double battLow; // Board, Sensor Power Supply -- Shown in Voltage
	private double battHigh; // Servo/Bluetooth/Laser Power Supply -- Shown in
	private int freeRam;
	private double xCoord;
	private double yCoord;
	private int roverHeading;

	private int laserAngle = 0;

	/**
	 * Rover Constructor
	 * 
	 * @param Bluetooth
	 *            Service to be used when transmitting data
	 */
	public Rover(BluetoothService connectionArg) {
		connection = connectionArg;
	}

	/**
	 * Sends Rogo Script to the connected Rover.
	 * 
	 * @param scriptArg
	 *            Script to send to the Rogo Rover. Must be properly
	 *            constructed.
	 */
	public boolean sendDataToRover(String scriptArg) {
		// exit if not connected
		if (!connection.isConnected()) {
			return false;
		}
		try {
			connection.transmitString(scriptArg);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns the arraylist containing communication received from the rover.
	 * 87
	 * @return the arraylist containing communication received from the rover.
	 */
	public ArrayList<String> getRoverData() {
		if (roverOutput == null) {
			roverOutput = new ArrayList<String>(2010);
		}
		return roverOutput;
	}

	/**
	 * Any data transmitted by the rover should be piped into this method, which
	 * will then parse through the output, and
	 * 
	 * TODO change from array to more suitable data structure
	 * 
	 * @param single
	 *            line output coming from the rover
	 */
	public void parseRoverOutput(String data) {
		// initialize if output log does not exist
		if (roverOutput == null) {
			roverOutput = getRoverData();
		}
		// skip blank lines
		if (data.equals("")) {
			return;
		}
		// TODO Error, too much data, currently resets. Use different
		// data structure?
		if (roverOutput.size() >= 2000) {
			roverOutput = new ArrayList<String>(100);
		}
		// Add to output Item
		roverOutput.add(data);

		// TODO Error if too much packet data, currently resets. Use different
		// data structure?
		if (dataPacket != null && dataPacket.size() >= 80) {
			dataPacket = new ArrayList<String>(100);
		}

		// If in data packet, save lines
		if (inDataPacket) {
			dataPacket.add(data);
		}

		//Check for Sensor Trip Outside of Packet
		if (data.equals("sensorTrip")) {
			//TODO Sensor Tripped, Show Alert.
		}
		
		// Check for start or stop of data packet.
		if (data.equals("start")) {
			inDataPacket = true;
			dataPacket = new ArrayList<String>(100);
			dataPacket.add(data);
		} else if (data.equals("end")) {
			inDataPacket = false;
			// packet complete, now parse
			consumeDataPacket();
		}
	}

	/**
	 * Data Packet Parser... Entries to be searched for go in here.
	 */
	private void consumeDataPacket() {
		if (dataPacket == null) {
			return;
		}
		Iterator<String> iterator = dataPacket.iterator();
		String string;
		while (iterator.hasNext()) {
			string = iterator.next();
			if (string.equals("pingF")) {
				pingF = ((int) Double.parseDouble(iterator.next()));
			} else if (string.equals("irFL")) {
				irFL = ((int) Double.parseDouble(iterator.next()));
			} else if (string.equals("irFR")) {
				irFR = ((int) Double.parseDouble(iterator.next()));
			} else if (string.equals("bLevelLow")) {
				battLow = (Double.parseDouble(iterator.next()));
			} else if (string.equals("bLevelHigh")) {
				battHigh = (Double.parseDouble(iterator.next()));
			} else if (string.equals("freeRam")) {
				freeRam = ((int) Double.parseDouble(iterator.next()));
			} else if (string.equals("loc")) {
			     st = new StringTokenizer(iterator.next(), ",()");
			     xCoord = Double.parseDouble(st.nextToken());
			     yCoord = Double.parseDouble(st.nextToken());
			     roverHeading =  Integer.parseInt(st.nextToken());
			}
		}
	}

	public int getInfaredFrontLeft() {
		return irFL;
	}

	public int getInfaredFrontRight() {
		return irFR;
	}

	public int getPingFront() {
		return pingF;
	}

	public int getLaserAngle() {
		return laserAngle;
	}

	public double getBattLow() {
		return battLow;
	}

	public double getBattHigh() {
		return battHigh;
	}

	public int getFreeRam() {
		return freeRam;
	}
	
	public double getXCoord() {
		return xCoord;
	}

	public double getYCoord() {
		return yCoord;
	}
	
	public int getHeading() {
		return roverHeading;
	}
	
	public void setLaserAngle(int laserAngleArg) {
		laserAngle = laserAngleArg;
	}

}
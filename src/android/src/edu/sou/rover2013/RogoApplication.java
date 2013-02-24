package edu.sou.rover2013;

import android.app.Application;
import android.util.Log;

// Extends application class, allowing for values/items to exist across all actions.
// Similar to Singleton design pattern. Alternative: passing data between actions.
public class RogoApplication extends Application {

	private BluetoothConnection bluetoothConnection;
	
	@Override
	public void onCreate() {
		super.onCreate();

		bluetoothConnection = new BluetoothConnection();

	}

	public BluetoothConnection getBluetoothConnection() {
		return bluetoothConnection;
	}
}

package edu.sou.rover2013;

import android.app.Application;

// Extends application class, allowing for values/items to exist across all actions.
// Similar to Singleton design pattern. Alternatives: passing data between actions.

// To access, use "connection = ((RogoApplication) getBluetoothConnection());"

// TODO Evalutate Android Solution:
//Singleton class
//You can take advantage of the fact that your application components run in the same process through the use of a singleton. 
//This is a class that is designed to have only one instance. It has a static method with a name such as getInstance() that
//returns the instance; the first time this method is called, it creates the global instance. 
//Because all callers get the same instance, they can use this as a point of interaction. For example activity A may retrieve 
//the instance and call setValue(3); later activity B may retrieve the instance and call getValue() to retrieve the last set value.

public class RogoApplication extends Application {

	private BluetoothConnection bluetoothConnection = null;

	public BluetoothConnection getBluetoothConnection() {
		if (bluetoothConnection == null) {
			bluetoothConnection = new BluetoothConnection();
		}
		return bluetoothConnection;
	}
}

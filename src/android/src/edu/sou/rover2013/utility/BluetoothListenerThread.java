package edu.sou.rover2013.utility;

import java.io.IOException;
import java.io.InputStream;

class BluetoothListenerThread extends Thread {

	private InputStream inputStream = null;

	public void run(InputStream inputStreamArg) {
		try {
			inputStream = inputStreamArg;
		} catch (Exception e) {
		}
		while (true) {
		}
	}

	/** Will cancel the listening socket, and cause the thread to finish */
	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
		}
	}
}
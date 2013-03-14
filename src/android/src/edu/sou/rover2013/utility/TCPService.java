package edu.sou.rover2013.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import android.util.Log;

/**
 * Borrowed Code
 * 
 * Hacked together solution that allows for TCP commands. Will remove/improve...
 * 
 */
public class TCPService implements Runnable {

	public static final int SERVERPORT = 4444;

	public void run() {
		try {
			Log.d("TCP", "S: Connecting...");
			ServerSocket serverSocket = new ServerSocket(SERVERPORT);

			while (true) {

				Socket client = serverSocket.accept();
				Log.d("TCP", "S: Receiving...");
				try {

					BufferedReader in = new BufferedReader(
							new InputStreamReader(client.getInputStream()));

					String str = in.readLine();

					while (str != null && !str.equals("end")) {
						str = in.readLine();
						Log.d("TCP", "S: Received: '" + str + "'");

						if (str.equals("forward")) {

						} else if (str.equals("reverse")) {

						} else if (str.equals("left")) {

						} else if (str.equals("right")) {

						}
					}

				} catch (Exception e) {
					Log.e("TCP", "S: Error", e);
				} finally {
					client.close();
					Log.d("TCP", "S: Done");
				}
			}
		} catch (Exception e) {
			Log.e("TCP", "S: Error", e);
		}
	}
}
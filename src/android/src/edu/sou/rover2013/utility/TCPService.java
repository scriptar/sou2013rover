package edu.sou.rover2013.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;
import edu.sou.rover2013.BaseActivity;

/**
 * Borrowed Code
 * 
 * Hacked together solution that allows for TCP commands. Will remove/improve...
 *
 */
// TODO Allow for listing of server thread status, and allow for closing of thread.
public class TCPService extends BaseActivity implements Runnable {

	public static int serverPort = 4444;
	public static BluetoothService connection;

	public TCPService(BluetoothService connection) {
		TCPService.connection = connection;
	}

	public TCPService(BluetoothService connection, int portArg ){
		TCPService.serverPort = portArg;
		TCPService.connection = connection;
	}
	
	public void run() {
		try {
			//wifiConsole.append("Thread Started\n");
			Log.d("test", "Thread Started");
			//wifiConsole.append("Waiting for Connection\n");
			ServerSocket serverSocket = new ServerSocket(serverPort);

			while (true) {

				Socket client = serverSocket.accept();
				//wifiConsole.append("Connection Established.\n");
				Log.d("test", "Connection Found");
				try {

					BufferedReader in = new BufferedReader(
							new InputStreamReader(client.getInputStream()));

					String str = in.readLine();

					while (str != null && !str.equals("end")) {
						str = in.readLine();
						//wifiConsole.append("Received: '" + str + "'\n");
						Log.d("test", "Received: '" + str + "'\n");
						if (str.equals("forward")) {
							connection.transmitByte((byte) 10);
						} else if (str.equals("forwardStop")) {
							connection.transmitByte((byte) 15);
						} else if (str.equals("reverse")) {
							connection.transmitByte((byte) 20);
						} else if (str.equals("reverseStop")) {
							connection.transmitByte((byte) 25);
						} else if (str.equals("left")) {
							connection.transmitByte((byte) 30);
						} else if (str.equals("leftStop")) {
							connection.transmitByte((byte) 35);
						} else if (str.equals("right")) {
							connection.transmitByte((byte) 40);
						} else if (str.equals("rightStop")) {
							connection.transmitByte((byte) 45);
						}
					}

				} catch (Exception e) {
					//wifiConsole.append("Error\n");
					Log.d("test", "Error");
				} finally {
					client.close();
					//wifiConsole.append("Connection closed\n");
					Log.d("test", "Closed");
				}
			}
		} catch (Exception e) {
			Log.e("TCP", "S: Error", e);
		}
	}
}
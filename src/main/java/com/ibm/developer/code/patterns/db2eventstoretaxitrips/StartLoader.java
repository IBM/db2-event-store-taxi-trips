package com.ibm.developer.code.patterns.db2eventstoretaxitrips;

import java.io.*;
import java.net.*;

public class StartLoader {
  public static void main(String[] args) throws IOException {
    Thread loader = null;
    EventStoreWriter writer = null;
    ServerSocket serverSocket = null;
    String IPAddress = null, username = null, pwd = null;

    if (args.length != 4) {
      System.err.println("Usage: java StartLoader <port number> <ip-address> <user> <password>");
      System.exit(1);
    }

    int portNumber = Integer.parseInt(args[0]);
    IPAddress = args[1];
    username = args[2];
    pwd = args[3];
    // System.out.println("IP address: " + IPAddress);
    // System.out.println("Port      : " + args[0]);
    // System.out.println("Username  : " + username);
    // System.out.println("Password  : " + pwd);

    // Get socket server connection
    try {
      serverSocket = new ServerSocket(Integer.parseInt(args[0]));
    } catch (IOException e) {
      System.out.println(
          "Exception caught when trying to listen on port "
              + portNumber
              + " or listening for a connection");
      System.out.println(e.getMessage());
    }
    // Wait for "clients" and process the request
    while (true) {
      try {
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in =
            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        inputLine = in.readLine();
        // Extract IP, rate, dbname, tabname
        String[] tokens = inputLine.split(",");

        // If there is a loader running, stop it.
        if (loader != null) {
          writer.keepRunning = 0;
          try {
            loader.join(2000); // Two seconds
            loader = null;
          } catch (InterruptedException e1) {
          }
        }
        // If the first argument is "stop", just continue
        if (0 == tokens[0].compareToIgnoreCase("stop")) {
          out.println("Insert process stopped");
          continue;
        }

        // Start a new loader
        String arg3 = tokens[3];
        if (IPAddress != null) arg3 = IPAddress;

        writer =
            new EventStoreWriter(
                arg3, Integer.parseInt(tokens[1]), tokens[2], tokens[3], username, pwd);
        loader = new Thread(writer);
        loader.start();

        out.println("Insert process started");

      } catch (IOException e) {
        System.out.println(
            "Exception caught when trying to listen on port "
                + portNumber
                + " or listening for a connection");
        System.out.println(e.getMessage());
      }
    }
  }
}

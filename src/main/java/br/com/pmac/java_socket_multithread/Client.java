package br.com.pmac.java_socket_multithread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Socket socket;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMessage() {
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                writer.write(message);
                writer.newLine();
                writer.flush();
            }

        } catch (Exception e) {
            System.out.println("aqui 1");
            e.printStackTrace();

            tryReconnect();
            // close();
        }
    }

    private void listen() {
        new Thread() {
            public void run() {
                try {
                    while (socket.isConnected()) {
                        String message = reader.readLine();
                        System.out.println(" listen(): " + message + "\n");
                        System.out.println(
                                "Connection:  " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                        // if message == null try reconect
                        if (message == null) {
                            close();
                            break;
                        }

                    }
                } catch (Exception e) {
                    System.out.println("aqui 2");
                    e.printStackTrace();
                    close();
                }
            }
        }.start();
    }

    private void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("aqui 3");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o seu nome: ");
        String username = scanner.nextLine();

        try {
            Socket socket = new Socket("localhost", 12345);
            Client client = new Client(socket, username);
            client.listen();
            client.sendMessage();
        } catch (Exception e) {
            System.out.println("aqui 4");
            e.printStackTrace();

            // tryReconnect();
        }
    }

    public static void tryReconnect() {
        // try 3x
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(3000);
                System.out.println("Tentando reconectar...");
                Socket socket = new Socket("localhost", 12345);
                Client client = new Client(socket, "username");
                client.listen();
                client.sendMessage();
                break;
            } catch (Exception e) {
                System.out.println("aqui 5");
                e.printStackTrace();
            }
        }

    }

}

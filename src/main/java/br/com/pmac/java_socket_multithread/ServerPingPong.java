package br.com.pmac.java_socket_multithread;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerPingPong {

    private ServerSocket serverSocket;

    public ServerPingPong(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        System.out.println("Iniciando servidor...");

        while (!serverSocket.isClosed()) {
            try {

                Socket socket = serverSocket.accept();
                System.out.println("Nova conexão com o cliente " + socket.getInetAddress().getHostAddress() + ":"
                        + socket.getPort());
                PingPongHandler pingPongHandler = new PingPongHandler(socket);

                Thread thread = new Thread(pingPongHandler);
                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        ServerPingPong server = new ServerPingPong(serverSocket);
        server.start();

    }

}

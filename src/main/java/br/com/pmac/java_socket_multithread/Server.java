package br.com.pmac.java_socket_multithread;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        System.out.println("Iniciando servidor...");

        while (!serverSocket.isClosed()) {
            try {

                Socket socket = serverSocket.accept();
                System.out.println("Nova conexão com o cliente " + socket.getInetAddress().getHostAddress() + ":"
                        + socket.getPort());
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
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
        Server server = new Server(serverSocket);
        server.start();

    }

}

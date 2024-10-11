package br.com.pmac.java_socket_multithread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {

        try {

            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();

            clientHandlers.add(this);

            broadcastMessage("O usuário " + clientUsername + " entrou no chat!");

        } catch (Exception e) {
            e.printStackTrace();
            closeEverything();
        }

    }

    public void broadcastMessage(String message) {

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                System.out.println(clientHandler.clientUsername + ": message resieved: " + message);
                if (clientHandler != this) {
                    clientHandler.bufferedWriter.write(clientUsername + " disse: " + message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
                if (clientHandler == this) {
                    if (message.equals("sair")) {
                        removeClientHandler();
                        break;
                    }
                }
            } catch (SocketException e) {
                System.err.println("Erro ao enviar mensagem para o cliente " + clientUsername);
                removeClientHandler(clientHandler.clientUsername);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void closeEverything() {
        try {

            // removeClientHandler();

            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }

            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }

            if (this.socket != null) {
                this.socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Iniciando cliente...");
        String messageFromClient;

        while (socket.isConnected()) {
            try {

                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null) {
                    broadcastMessage(messageFromClient);
                }

            } catch (Exception e) {
                // closeEverything();
                System.out.println(
                        "Erro ao enviar mensagem para o cliente " + clientUsername + " - " + e.getLocalizedMessage());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void removeClientHandler() {
        broadcastMessage(clientUsername + " saiu do chat!");
        clientHandlers.remove(this);
    }

    public void removeClientHandler(String user) {
        int i = 0;
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(user)) {
                break;
            }
            i++;
        }
        clientHandlers.remove(i);
        broadcastMessage(user + " saiu do chat!");
    }

}

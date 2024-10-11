package br.com.pmac.java_socket_multithread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class PingPongHandler implements Runnable {

    private ArrayList<PingPongHandler> pingPongHandlers = new ArrayList<PingPongHandler>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String playerName;
    private int hitScore = 0;

    public PingPongHandler(Socket socket) {

        try {

            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.playerName = bufferedReader.readLine();

            pingPongHandlers.add(this);
            broadcastMessage("O jogador " + playerName + " entrou no jogo!!!");

        } catch (Exception e) {
            System.out.println("aqui 1");
            e.printStackTrace();
            this.stop();
        }

    }

    public void broadcastMessage(String message) {

        for (PingPongHandler pingPongHandler : pingPongHandlers) {
            try {
                System.out.println(pingPongHandler.playerName + ": message resieved: " + message);
                // if (pingPongHandler == this) {
                if (message.equals("sair")) {
                    removePingPongHandler();
                    break;
                }

                pingPongHandler.bufferedWriter.write(message);
                pingPongHandler.bufferedWriter.newLine();
                pingPongHandler.bufferedWriter.flush();
                // }

            } catch (Exception e) {
                System.out.println("aqui 3");
                e.printStackTrace();

            }
        }
    }

    public void removePingPongHandler() {
        pingPongHandlers.remove(this);
        broadcastMessage("O jogador " + playerName + " saiu do jogo!");
        this.stop();
    }

    public void stop() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("aqui 3");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = bufferedReader.readLine();
                if (message.equals("sair")) {
                    removePingPongHandler();
                    break;
                }
                if (message.equals("ping")) {
                    hitScore++;
                    broadcastMessage("O jogador " + playerName + " acertou a bola " + hitScore + " vezes!");
                }
            }
        } catch (Exception e) {
            System.out.println("aqui 4");
            e.printStackTrace();
            removePingPongHandler();
        }
    }

}

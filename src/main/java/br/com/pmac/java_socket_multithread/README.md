# Java Socket Multithread

This project is a multithreaded socket server and client implementation using Java. It includes two main server types: a chat server and a ping-pong server, both capable of handling multiple clients simultaneously. The project also uses Spring Boot for application configuration and testing.

## Key Classes and Files

- **JavaSocketMultithreadApplication.java**: The entry point for the Spring Boot application.
- **Server.java**: Implements a multithreaded chat server.
- **ServerPingPong.java**: Implements a multithreaded ping-pong server.
- **Client.java**: Implements the client that connects to the servers.
- **ClientHandler.java**: Handles individual client connections for the chat server.
- **PingPongHandler.java**: Handles individual client connections for the ping-pong server.
- **application.properties**: Contains Spring Boot application properties.

## How to Run

1. **Build the Project**:

   ```sh
   ./mvnw clean install
   ```

2. Run the chat server

   ```sh
   java -cp target/java-socket-multithread-0.0.1-SNAPSHOT.jar br.com.pmac.java_socket_multithread.Server
   ```

3. Run the Ping-Pong Server:

   ```sh
   java -cp target/java-socket-multithread-0.0.1-SNAPSHOT.jar br.com.pmac.java_socket_multithread.ServerPingPong
   ```

4. Run the Client:

   ```sh
   java -cp target/java-socket-multithread-0.0.1-SNAPSHOT.jar br.com.pmac.java_socket_multithread.Client
   ```

## License

This project is licensed under the MIT License. See the LICENSE file for details.

package pt.tecnico.distledger.server;

//import pt.ulisboa.tecnico.distledger.contract.user.UserServiceGrpc;
//import pt.ulisboa.tecnico.distledger.contract.user.AdminServiceGrpc;
import pt.tecnico.distledger.server.domain.ServerState;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(ServerMain.class.getSimpleName());
        
        System.out.printf("Received %d arguments%n", args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.printf("arg[%d] = %s%n", i, args[i]);
        }

        if (args.length != 2) {
            System.err.println("ServerMain <serverPort> <serverQualificator>");
            System.exit(1);
        }

        final int port = Integer.parseInt(args[0]);
        final String qualificator = args[1];

        ServerState serverState = new ServerState();
        
        // Create a new server to listen on port
        Server server = ServerBuilder.forPort(port).addService(new UserService(serverState)).addService(new AdminService(serverState)).build();

        // Start the server
        server.start();
        
        // Server threads are running in the background.
        System.out.println("Server started");

        // Do not exit the main thread. Wait until server is terminated.
        server.awaitTermination();

    }
}
package pt.tecnico.distledger.adminclient;

import pt.tecnico.distledger.adminclient.grpc.AdminService;

public class AdminClientMain {

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != "true");

	private static void debug(String debugMessage) {
        if (DEBUG_FLAG)
			System.err.println(debugMessage);
	}
    public static void main(String[] args) {

        System.out.println(AdminClientMain.class.getSimpleName());

        // receive and print arguments
        debug(String.format("Received %d arguments", args.length));
		for (int i = 0; i < args.length; i++) {
			debug(String.format("arg[%d] = %s", i, args[i]));
        }

        // check arguments
        if (args.length != 2) {
            System.err.println("Argument(s) missing!");
            System.err.println("Usage: mvn exec:java -Dexec.args=<host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        CommandParser parser = new CommandParser(new AdminService(host, port));
        parser.parseInput();

    }

    public static void debugParser(String line, int option) {
        //activate 
        if (option == 1) {  
            final String messageDebug = "Debug: This operation 'Activate' turns the state of server to active"; 
            debug(messageDebug);
        }
        //deactivate
        else if (option == 2) {
            final String messageDebug = "Debug: This operation 'Deactivate' turns the state of server to inactive";
            debug(messageDebug);
        }
        //getLedgerState
        else if (option == 3) {
            final String messageDebug = "Debug : This operation 'GetLedgerState' provides you the content of the ledger";
            debug(messageDebug);
        }
        /*gossip
        else if (option == ?) {
            final String messageDebug = "Debug: This operation 'Balance' provides you to check the balance of an account by giving a server Qualificator and an UserID";
            debug(messageDebug);
        }*/
    }
}
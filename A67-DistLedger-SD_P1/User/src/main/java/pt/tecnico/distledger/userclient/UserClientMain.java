package pt.tecnico.distledger.userclient;


import pt.tecnico.distledger.userclient.grpc.UserService;

public class UserClientMain {

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != "true");

	private static void debug(String debugMessage) {
		if (DEBUG_FLAG)
			System.err.println(debugMessage);
	}

    public static void main(String[] args) {

        System.out.println(UserClientMain.class.getSimpleName());

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

        CommandParser parser = new CommandParser(new UserService(host,port));
        parser.parseInput();

    }

    public static void debugParser(String line, int option) {
        //createAccount 
        if (option == 1) {  
            final String messageDebug = "Debug: This operation 'CreateAccount' provides you to create an account by giving a server Qualificator and an UserID"; 
            debug(messageDebug);
        }
        //deleteAccount
        else if (option == 2) {
            final String messageDebug = "Debug: This operation 'DeleteAccount' provides you to delete an account by giving a server Qualificator and an UserID";
            debug(messageDebug);
        }
        //balance
        else if (option == 3) {
            final String messageDebug = "Debug: This operation 'Balance' provides you to check the balance of an account by giving a server Qualificator and an UserID";
            debug(messageDebug);
        }
        //transferTo
        else if (option == 4) {
            final String messageDebug = "Debug : This operation 'TransferTo' provides you to transfer an amount of money from one account to another by giving a server Qualificator, an UserID (sender), a DestID (receiver) and the amount of money to transfer";
            debug(messageDebug);
        }
    }
}

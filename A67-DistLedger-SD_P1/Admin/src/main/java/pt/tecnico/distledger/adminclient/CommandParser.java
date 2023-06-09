package pt.tecnico.distledger.adminclient;

import pt.tecnico.distledger.adminclient.grpc.AdminService;

import java.util.Scanner;

public class CommandParser {

    private static final String SPACE = " ";
    private static final String ACTIVATE = "activate";
    private static final String DEACTIVATE = "deactivate";
    private static final String GET_LEDGER_STATE = "getLedgerState";
    private static final String GOSSIP = "gossip";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    private final AdminService adminService;
    public CommandParser(AdminService adminService) {
        this.adminService = adminService;
    }
    void parseInput() {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            String cmd = line.split(SPACE)[0];

            switch (cmd) {
                case ACTIVATE:
                    this.activate(line);
                    break;

                case DEACTIVATE:
                    this.deactivate(line);
                    break;

                case GET_LEDGER_STATE:
                    this.dump(line);
                    break;

                case GOSSIP:
                    this.gossip(line);
                    break;

                case HELP:
                    this.printUsage();
                    break;

                case EXIT:
                    this.adminService.shutdown();
                    exit = true;
                    break;

                default:
                    break;
            }

        }
    }

    private void activate(String line) {
        String[] split = line.split(SPACE);

        if (split.length != 2 && split.length != 3) {
            this.printUsage();
            return;
        }
        String server = split[1];

        if (split.length == 3) {
            AdminClientMain.debugParser(line, 1);
        } 

        this.adminService.activate();
    }

    private void deactivate(String line) {
        String[] split = line.split(SPACE);

        if (split.length != 2 && split.length != 3) {
            this.printUsage();
            return;
        }
        String server = split[1];

        if (split.length == 3) {
            AdminClientMain.debugParser(line, 2);
        } 

        this.adminService.deactivate();
    }

    private void dump(String line) {
        String[] split = line.split(SPACE);

        if (split.length != 2 && split.length != 3) {
            this.printUsage();
            return;
        }
        String server = split[1];

        if (split.length == 3) {
            AdminClientMain.debugParser(line, 3);
        }

        this.adminService.getLedgerState();
    }

    @SuppressWarnings("unused")
    private void gossip(String line) {
        /* TODO Phase-3 */
        System.out.println("TODO: implement gossip command (only for Phase-3)");
    }
    private void printUsage() {
        System.out.println("Usage:\n" +
                "- activate <server>\n" +
                "- deactivate <server>\n" +
                "- getLedgerState <server>\n" +
                "- gossip <server>\n" +
                "- exit\n");
    }

}
package pt.tecnico.distledger.userclient.grpc;

import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.user.UserServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class UserService {

    /*TODO: The gRPC client-side logic should be here.
        This should include a method that builds a channel and stub,
        as well as individual methods for each remote operation of this service. */

    private ManagedChannel channel;
    private UserServiceGrpc.UserServiceBlockingStub stub;

    public UserService(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = UserServiceGrpc.newBlockingStub(this.channel);
    }

    public void createAccount(String userId) {

        CreateAccountRequest request = CreateAccountRequest.newBuilder().setUserId(userId).build();

        CreateAccountResponse response = this.stub.createAccount(request);
        displayMSG(response.getError());
    }

    public void balance(String userId) {
        BalanceRequest request = BalanceRequest.newBuilder().setUserId(userId).build();

        BalanceResponse response = this.stub.balance(request);
        displayMSG(response.getError());
        if (response.getError() == ErrorMsg.OK && response.getValue() >= 0)
            System.out.println(response.getValue());
    }

    public void transferTo(String accountFrom, String accountTo, int amount) {
        TransferToRequest request = TransferToRequest.newBuilder().setAccountFrom(accountFrom).setAccountTo(accountTo).setAmount(amount).build();

        TransferToResponse response = this.stub.transferTo(request);
        displayMSG(response.getError());
    }

    public void deleteAccount(String userId) {
        DeleteAccountRequest request = DeleteAccountRequest.newBuilder().setUserId(userId).build();

        DeleteAccountResponse response = this.stub.deleteAccount(request);
        displayMSG(response.getError());
    }

    public void shutdown() {
        this.channel.shutdown();
    }

    private static void displayMSG(ErrorMsg msg) {
		switch (msg) {
            case OK:
                System.out.println("OK");
                break;
            case ERROR_CREATE:
                System.out.println("ACCOUNT ALREADY EXISTS");
                break;
            case ERROR_BALANCE:
                System.out.println("NO ACCOUNT TO CHECK BALANCE");
                break;
            case ERROR_DELETE:
                System.out.println("NO ACCOUNT TO DELETE");
                break;
            case ERROR_TRANSFER_ACCOUNT_FROM:
                System.out.println("ORIGIN OF TRANSFER DONT EXISTS");
                break;
            case ERROR_TRANSFER_ACCOUNT_DEST:
                System.out.println("DESTINATION OF TRANSFER DONT EXISTS");
                break;
            case ERROR_TRANSFER_AMOUNT:
                System.out.println("DONT HAVE BALANCE ENOUGH TO MAKE THE TRANSFER");
                break;
            case ERROR_UNAVAILABLE:
                System.out.println("UNAVAILABLE");
                break;
            case ERROR_BALANCE_NOT_ZERO:
                System.out.println("BALANCE NOT ZERO");
                break;
            case ERROR_CANNOT_BROKER:
                System.out.println("CANNOT REMOVE BROKER ACCOUNT");
                break;
		    default:
			    System.out.println("UNKNOWN");
			    break;
		}
	}

}

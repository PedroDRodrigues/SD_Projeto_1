package pt.tecnico.distledger.server;

import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.user.UserServiceGrpc;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.domain.operation.Operation;

import io.grpc.stub.StreamObserver;


/* MISSING : VERIFY POSSIBLE EXCEPTIONS , AND THROW THEM */

public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private ServerState state;

    public UserService(ServerState state){
        this.state = state;
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<CreateAccountResponse> responseObserver) {

        CreateAccountResponse response;

        if(this.state.getActive() != 1) {
            response = CreateAccountResponse.newBuilder().setError(ErrorMsg.ERROR_UNAVAILABLE).build();
        }
        else {
            if (this.state.createAccount(request.getUserId(), 0) > 0) {
                response = CreateAccountResponse.newBuilder().setError(ErrorMsg.OK).build();
            }
            else {
                response = CreateAccountResponse.newBuilder().setError(ErrorMsg.ERROR_CREATE).build();
            }
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {

        BalanceResponse response;

        if(this.state.getActive() != 1) {
            response = BalanceResponse.newBuilder().setError(ErrorMsg.ERROR_UNAVAILABLE).build();
        }
        else {
            int balance = this.state.balance(request.getUserId());
            if (balance >= 0) {
                response = BalanceResponse.newBuilder().setValue(balance).setError(ErrorMsg.OK).build();
            }
            else {
                response = BalanceResponse.newBuilder().setValue(0).setError(ErrorMsg.ERROR_BALANCE).build();
            }
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAccount(DeleteAccountRequest request, StreamObserver<DeleteAccountResponse> responseObserver) {

        DeleteAccountResponse response;

        if(this.state.getActive() != 1) {
            response = DeleteAccountResponse.newBuilder().setError(ErrorMsg.ERROR_UNAVAILABLE).build();
        }
        else {
            int check = this.state.deleteAccount(request.getUserId());
            if (check > 0) {
                response = DeleteAccountResponse.newBuilder().setError(ErrorMsg.OK).build();
            }
            else if (check == -1){
                response = DeleteAccountResponse.newBuilder().setError(ErrorMsg.ERROR_DELETE).build();
            }
            else if (check == -2){
                response = DeleteAccountResponse.newBuilder().setError(ErrorMsg.ERROR_BALANCE_NOT_ZERO).build();
            }
            else {
                response = DeleteAccountResponse.newBuilder().setError(ErrorMsg.ERROR_CANNOT_BROKER).build();
            }
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void transferTo(TransferToRequest request, StreamObserver<TransferToResponse> responseObserver) {

        TransferToResponse response;

        if(this.state.getActive() != 1) {
            response = TransferToResponse.newBuilder().setError(ErrorMsg.ERROR_UNAVAILABLE).build();
        }
        else {
            int check = this.state.transferTo(request.getAccountFrom(),request.getAccountTo(),request.getAmount());

            if (check == -1) {
                response = TransferToResponse.newBuilder().setError(ErrorMsg.ERROR_TRANSFER_ACCOUNT_FROM).build();
            }
            else if (check == -2) {
                response = TransferToResponse.newBuilder().setError(ErrorMsg.ERROR_TRANSFER_ACCOUNT_DEST).build();
            }
            else if (check == -3) {
                response = TransferToResponse.newBuilder().setError(ErrorMsg.ERROR_TRANSFER_AMOUNT).build();
            }
            else {
                response = TransferToResponse.newBuilder().setError(ErrorMsg.OK).build();
            }
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
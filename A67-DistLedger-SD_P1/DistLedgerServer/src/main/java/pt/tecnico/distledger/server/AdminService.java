package pt.tecnico.distledger.server;

import io.grpc.*;
import pt.tecnico.distledger.server.domain.operation.TransferOp;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminServiceGrpc;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.domain.operation.Operation;

import io.grpc.stub.StreamObserver;


public class AdminService extends AdminServiceGrpc.AdminServiceImplBase {

    private ServerState serverState;

    public AdminService(ServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public void activate(ActivateRequest request, StreamObserver<ActivateResponse> responseObserver) {
        this.serverState.activate();
        ActivateResponse response = ActivateResponse.newBuilder().build(); //getActivateMethod
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deactivate(DeactivateRequest request, StreamObserver<DeactivateResponse> responseObserver) {
        this.serverState.deactivate();
        DeactivateResponse response = DeactivateResponse.newBuilder().build(); //getDeactivateMethod
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void gossip(GossipRequest request, StreamObserver<GossipResponse> responseObserver) {
        GossipResponse response = GossipResponse.newBuilder().build(); //getGossipMethod
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getLedgerState(getLedgerStateRequest request, StreamObserver<getLedgerStateResponse> responseObserver) {
        getLedgerStateResponse.Builder responseBuilder = getLedgerStateResponse.newBuilder();

        DistLedgerCommonDefinitions.LedgerState.Builder ledgerStateBuilder = DistLedgerCommonDefinitions
                .LedgerState.newBuilder();
        for(Operation operation : this.serverState.getLedger()) {
            DistLedgerCommonDefinitions.Operation.Builder operationBuilder = DistLedgerCommonDefinitions
                    .Operation.newBuilder();
            operationBuilder.setType(operation.getType());
            operationBuilder.setAccount(operation.getAccount());
            if(operation instanceof TransferOp) {
                operationBuilder.setDestAccount(((TransferOp) operation).getDestAccount());
                operationBuilder.setAmount(((TransferOp) operation).getAmount());
            }
            operationBuilder.build();
            ledgerStateBuilder.addLedger(operationBuilder);
        }
        responseBuilder.addLedgerState(ledgerStateBuilder.build());
        getLedgerStateResponse response = responseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
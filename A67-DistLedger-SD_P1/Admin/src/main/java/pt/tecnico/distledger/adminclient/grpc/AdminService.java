package pt.tecnico.distledger.adminclient.grpc;

import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AdminService {
    private ManagedChannel channel;

    private AdminServiceGrpc.AdminServiceBlockingStub stub;

    public AdminService(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = AdminServiceGrpc.newBlockingStub(this.channel);
    }

    public void activate() {
        ActivateRequest request = ActivateRequest.newBuilder().build();
        ActivateResponse response = this.stub.activate(request);
        System.out.println("OK");
    }

    public void deactivate() {
        DeactivateRequest request = DeactivateRequest.newBuilder().build();
        DeactivateResponse response = this.stub.deactivate(request);
        System.out.println("OK");
    }

    public void getLedgerState() {
        getLedgerStateRequest request = getLedgerStateRequest.newBuilder().build();
        getLedgerStateResponse response = this.stub.getLedgerState(request);
        System.out.println("OK");
        for(DistLedgerCommonDefinitions.LedgerState ledgerState : response.getLedgerStateList()) {
            System.out.println("ledgerState {");
            for(DistLedgerCommonDefinitions.Operation ledger : ledgerState.getLedgerList()) {
                System.out.println("  ledger {");
                DistLedgerCommonDefinitions.OperationType type = ledger.getType();
                System.out.println("    type: " + type);
                System.out.println("    userId: \"" + ledger.getAccount() + "\"");
                if(type == DistLedgerCommonDefinitions.OperationType.OP_TRANSFER_TO) {
                    System.out.println("    destUserId: \"" + ledger.getDestAccount() + "\"");
                    System.out.println("    amount: \"" + ledger.getAmount() + "\"");
                }
                System.out.println("  }");
            }
            System.out.println("}");
        }
    }

    public void gossip() {
    }

    public void shutdown() {
        this.channel.shutdown();
    }
}
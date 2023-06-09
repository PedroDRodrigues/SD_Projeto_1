package pt.tecnico.distledger.server.domain.operation;

import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.*;

public class CreateOp extends Operation {

    public CreateOp(String account) {
        super(account);
        this.setType(DistLedgerCommonDefinitions.OperationType.OP_CREATE_ACCOUNT);
    }
}
package pt.tecnico.distledger.server.domain;

import pt.tecnico.distledger.server.domain.operation.CreateOp;
import pt.tecnico.distledger.server.domain.operation.DeleteOp;
import pt.tecnico.distledger.server.domain.operation.Operation;
import pt.tecnico.distledger.server.domain.operation.TransferOp;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ServerState {
    private List<Operation> ledger;
    private HashMap<String, Integer> accounts;
    private int active;

    public ServerState() {
        this.ledger = new ArrayList<>();
        this.accounts = new HashMap<>();
        this.accounts.put("broker",1000);
        this.active = 1;
    }

    public List<Operation> getLedger() {
        return ledger;
    }

    public void setLedger(List<Operation> ledger) {
        this.ledger = ledger;
    }

    public HashMap<String, Integer> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<String, Integer> accounts) {
        this.accounts = accounts;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void addOperation(Operation operation) {
        this.ledger.add(operation);
    }

    public int createAccount(String userId, int balance) {           // Check If userId already have an account , if not creates it
        if (this.accounts.containsKey(userId))
            return -1;
        addOperation(new CreateOp(userId));
        this.accounts.put(userId,balance);
        return 1;
    }

    public int deleteAccount(String userId) {                    // Check if userId exists and has balance 0, to delete it from the map

        if (userId.equals("broker"))
            return -3;
        int balance = this.balance(userId);
        if (balance > 0)
            return -2;
        else if (balance < 0)
            return -1;
        else{
            this.accounts.remove(userId);
            addOperation(new DeleteOp(userId));
            return 1;
        }
    }

    public int balance(String userId) {                              // retorna -1 caso nao exista essa conta , ou retorna o saldo caso a conta exista
        if (!this.accounts.containsKey(userId))
            return -1;
        return this.accounts.get(userId);
    }

    public int transferTo(String userFrom, String userTo, int amount) {          // verifica o saldo da conta de origem , e se as contas existem , e realiza a transferencia
        int balance1 = balance(userFrom);
        int balance2 = balance(userTo);
        if (balance1 < 0)
            return -1;
        if (balance2 < 0)
            return -2;
        if (balance1 < amount)
            return -3;
        addOperation(new TransferOp(userFrom, userTo, amount));
        this.accounts.put(userFrom,balance1-amount);
        this.accounts.put(userTo,balance2 + amount);
        return 1;
    }

    public void activate() {
        this.active = 1;
    }

    public void deactivate() {
        this.active = 0;
    }

    @Override
    public String toString() {
        return "ServerState{" +
                "ledger=" + ledger +
                "accounts=" + accounts +
                '}';
    }
}
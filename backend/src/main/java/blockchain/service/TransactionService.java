package blockchain.service;

import blockchain.entity.Transaction;

import java.util.List;

public interface TransactionService {
    //发起合同
    public boolean sponsorTransaction(String initiatorOrgName, String recipientOrgName, String transID, String tranHash, String initiatorID, String recipientID, int amount, String transType, String initiatorSignature, String information,int weight);
    //接受合同
    public boolean verifyTransaction(String transID, String recipientID, String recipientSignature);
    //更新合同状态
    public boolean updateTransactionStatus(String transID, String transStatus);
    //查询合同
    public  Transaction queryTransactionByTransID(String transID);
    //查询所有合同
    public List<Transaction> queryAllTransactions();
    //查询发起的合同
    public List<Transaction> queryTransactionByInitiatorID(String initiatorID);
    //查询接收的合同
    public List<Transaction> queryTransactionByRecipientID(String recipientID);
    //查询合同hash
    public String queryTranHashByTransID(String transID);

}

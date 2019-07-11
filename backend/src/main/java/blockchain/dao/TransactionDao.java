package blockchain.dao;


import blockchain.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionDao {
    public void sponsorTransaction(@Param(value = "transaction")Transaction transaction);//新增发起的transaction记录
    public void verifyTransaction(@Param(value = "transID")String transID, @Param(value = "recipientID")String recipientID, @Param(value = "recipientSignature")String recipientSignature,@Param(value = "updateTimestamp")String updateTimestamp);//接收人确认
    public void updateTransactionStatus(@Param(value = "transID")String transID,@Param(value = "transStatus")String transStatus,@Param(value = "updateTimestamp")String updateTimestamp);//修改合同状态
    public Transaction queryTransactionByTransID(@Param(value = "transID")String transID);//通过transID查询交易
    public Transaction queryTransactionByTranHash(@Param(value = "tranHash")String tranHash);//通过tranHash查询交易
    public List<Transaction> queryTransactionByInitiatorID(@Param(value = "initiatorID")String initiatorID);//通过发起人id查询交易
    public List<Transaction> queryTransactionByRecipientID(@Param(value = "recipientID")String recipientID);//通过接收人id查询交易
    public List<Transaction> queryAllTransactions();
    public String queryTranHashByTransID(@Param(value = "transID")String transID);
    public List<Transaction> queryTranByStransStatus(@Param(value = "transStatus")String transStatus);
}

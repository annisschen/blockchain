package blockchain.service.Impl;

import blockchain.dao.OrganizationDao;
import blockchain.dao.TokenDao;
import blockchain.dao.TransactionDao;
import blockchain.entity.Organization;
import blockchain.entity.Transaction;
import blockchain.service.TransactionService;
import blockchain.utils.Utils;
import blockchain.solidity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private EnterpriseContract enterpriseContract;

    @Override
    public boolean sponsorTransaction(String initiatorOrgName, String recipientOrgName, String transID, String tranHash, String initiatorID, String recipientID, int amount, String transType, String initiatorSignature, String information,int weight)
    {
        //判断是否合法
        Transaction isTransaction = transactionDao.queryTransactionByTransID(transID);
        if(isTransaction!=null)
        {
            System.out.println("记录已经存在");
            return false;
        }
        Organization initiator = organizationDao.queryOrgnizationByOrgID(initiatorID);
        if(initiator==null)
        {
            System.out.println("发起人不存在");
            return false;
        }
        //合同hash的检查在controller做
        //合同金额是否可以授信的检查,暂时跳过

        String transStatus = "wait";
        long now  = System.currentTimeMillis();
        String time  = Utils.sdf(now);
        Transaction transaction = new Transaction();
        transaction.setInitiatorOrgName(initiatorOrgName);
        transaction.setRecipientOrgName(recipientOrgName);
        transaction.setTransID(transID);
        transaction.setTranHash(tranHash);
        transaction.setInitiatorID(initiatorID);
        transaction.setRecipientID(recipientID);
        transaction.setAmount(amount);
        transaction.setTransType(transType);
        transaction.setTransStatus(transStatus);
        transaction.setCreateTimestamp(time);
        transaction.setUpdateTimestamp(time);
        transaction.setInitiatorSignature(initiatorSignature);
        transaction.setInformation(information);
        transaction.setRecipientSignature("unset");
        transaction.setWeight(weight);

        try{
            System.out.println("新建transaction");
            System.out.println(transaction);
            transactionDao.sponsorTransaction(transaction);
            //区块链操作***************
            enterpriseContract.upload(transID,tranHash,transType,information,time,initiatorOrgName,recipientOrgName,initiatorSignature).send();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public boolean verifyTransaction(String transID, String recipientID, String recipientSignature)
    {
        Transaction trans = transactionDao.queryTransactionByTransID(transID);
        if(trans==null)
        {
            System.out.println("没有这份合同");
            return false;
        }
        Organization org = organizationDao.queryOrgnizationByOrgID(recipientID);
        if(org==null)
        {
            System.out.println("没有这个机构");
            return false;
        }
        long now  = System.currentTimeMillis();
        String time  = Utils.sdf(now);
        transactionDao.verifyTransaction(transID, recipientID, recipientSignature,time);
        transactionDao.updateTransactionStatus(transID,"signed",time);
        //区块链操作*********************
        try{
            enterpriseContract.sign(transID,time,recipientSignature).send();
        }
        catch (Exception e){ e.printStackTrace(); }
        return true;
    }
    @Override
    public boolean updateTransactionStatus(String transID, String transStatus)
    {
        //**********补充错误判断
        long now  = System.currentTimeMillis();
        String time  = Utils.sdf(now);
        transactionDao.updateTransactionStatus(transID,transStatus,time);
        return true;
    }
    @Override
    public  Transaction queryTransactionByTransID(String transID)
    {
        Transaction trans = transactionDao.queryTransactionByTransID(transID);
        return trans;
    }
    @Override
    public List<Transaction> queryAllTransactions()
    {
        return transactionDao.queryAllTransactions();
    }
    @Override
    public List<Transaction> queryTransactionByInitiatorID(String initiatorID)
    {
        return transactionDao.queryTransactionByInitiatorID(initiatorID);
    }
    @Override
    public List<Transaction> queryTransactionByRecipientID(String recipientID)
    {
        return transactionDao.queryTransactionByRecipientID(recipientID);
    }
    @Override
    public String queryTranHashByTransID(String transID)
    {
        return transactionDao.queryTranHashByTransID(transID);
    }


}

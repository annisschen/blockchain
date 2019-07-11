package blockchain.controller;

import blockchain.constant.keyConstant;
import blockchain.entity.*;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.Impl.TokenServiceImpl;
import blockchain.service.Impl.TransactionServiceImpl;
import blockchain.utils.RSAUtils;
import blockchain.utils.Utils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/token")
public class tokenController {
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;

    //申请授信
//    @RequestMapping("/apply")
//    public Result tokenApply(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
//                             @RequestParam("transType")String transType, @RequestParam("amount")int amount,
//                             @RequestParam("information")String information, @RequestParam("contractName")String contractName)
//    {
//        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,transType,amount,contractName,information);
//
////        String initiatorName = reqTransaction.getInitiatorName();
////        String recipientName = reqTransaction.getRecipientName();
////        String transType = reqTransaction.getTransType();
////        String information = reqTransaction.getInformation();//关于抵押物的信息
////        int amount = reqTransaction.getAmount();
//        //生成transaction
////            生成基本信息-
////            生成合同hash
////            生成发起人签名
//
//        System.out.println(initiatorName);
//        System.out.println(recipientName);
//
//        String initiatorID = organizationService.queryOrgIDByOrgName(initiatorName);
//        String recipientID = organizationService.queryOrgIDByOrgName(recipientName);
//        long now = System.currentTimeMillis();
//        String time = Utils.sdf(now);
//        String transStatus = "wait";
////      合同ID
//        String transID = UUID.randomUUID().toString();
////          发起人签名
//        try{
//            String privateKeyPath = keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String initiatorSignature = RSAUtils.sign("apply credit".getBytes(),privateKey);
//
//            transactionService.sponsorTransaction(initiatorName,recipientName, transID,"apply token",initiatorID,recipientID,amount,transType,initiatorSignature,information);
//
//            return new Result(true,transID,"200");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }
//    //    同意合同
//    @RequestMapping("/approve")
//    public Result tokenApprove(@RequestParam("transID")String transID,@RequestParam("tokenStatus")String tokenStatus)
//    {
//        Transaction trans = transactionService.queryTransactionByTransID(transID);
//        String recipientID = trans.getRecipientID();
//        String initiatorID = trans.getInitiatorID();
//
//        String recOrg = organizationService.queryOrganizationByOrgID(recipientID).getOrgName();
//        String initOrg = organizationService.queryOrganizationByOrgID(initiatorID).getOrgName();
//        if(trans==null)
//        {
//            System.out.println("不存在交易");
//            return new Result(false,"不存在交易","404");
//        }
//        Organization recipientOrg = organizationService.queryOrganizationByOrgID(recipientID);
//        Organization initiatorOrg = organizationService.queryOrganizationByOrgID(initiatorID);
//        if(recipientOrg==null)
//        {
//            System.out.println("不存在接受机构");
//            return new Result(false,"不存在机构","404");
//        }
////        获得签名
//        String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + recipientID + ".key";
//        String tranHash = transactionService.queryTranHashByTransID(transID);
//        try
//        {
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String recipientSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
//            transactionService.verifyTransaction(transID,recipientID,recipientSignature);
//
////            token
//            Token token = new Token();
//
//            long now = System.currentTimeMillis();
//            String time = Utils.sdf(now);
//
//
//            Organization organization = organizationService.queryOrganizationByOrgID(initiatorID);
////            String recipientID = transaction.getRecipientID();
//
//            token.setTransTimestamp(time);
//            token.setUpdateTimestamp(time);
//            token.setTokenID(UUID.randomUUID().toString());
//            token.setTokenStatus(tokenStatus);//授信
//            token.setBankID(recipientID);//自己就是银行
//            token.setRecOrg(recOrg);
//            token.setInitOrg(initOrg);
//            token.setAmount(trans.getAmount());
//            token.setPaid(0);
//
//            tokenService.addToken(token);
//
//
//            //修改钱包
//            int recCreditLimit = recipientOrg.getCreditLimit();
//            organizationService.setCreditLimit(recCreditLimit - trans.getAmount(),time,recipientID);
//            int initCreditLimit = initiatorOrg.getCreditLimit();
//            organizationService.setCreditLimit(initCreditLimit + trans.getAmount(),time,initiatorID);
//
//            //
//            return new Result(true,"success","200");
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }

    //    支付token
    @RequestMapping("/pay")
    public Result tokenPay(@RequestParam("fromOrg")String fromOrg,@RequestParam("recOrg")String recOrg,@RequestParam("amount")int amount,@RequestParam("password")String password)
    {
        //生成token
        String tokenID = UUID.randomUUID().toString();
        String initiatorID = organizationService.queryOrgIDByOrgName(fromOrg);
        String recipientID = organizationService.queryOrgIDByOrgName(recOrg);
        String bankID = organizationService.queryBankID(initiatorID);
        int creditLimit = organizationService.queryCreditLimit(initiatorID);
        if(organizationService.checkPasswordByOrgID(password,initiatorID)!=1)
        {
            System.out.println("密码错误");
            return new Result(false,"密码错误","404");
        }
        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);
        if(amount>creditLimit)
        {
            System.out.println("额度不足");
            return new Result(false,"额度不足","404");
        }
        String tokenStatus = "U";
        Token token = new Token();
        token.setTokenID(tokenID);
        token.setInitOrg(fromOrg);
        token.setRecOrg(recOrg);
        token.setBankID(bankID);
        token.setAmount(amount);
        token.setPaid(0);
        token.setTokenStatus(tokenStatus);
        token.setTransTimestamp(time);
        token.setUpdateTimestamp(time);

        tokenService.addToken(token);

        //更新钱包余额
        try
        {
            organizationService.setCreditLimit(creditLimit-amount,time,initiatorID);
//            更新接收人的钱包余额
            creditLimit = organizationService.queryCreditLimit(recipientID);
            organizationService.setCreditLimit(creditLimit+amount,time,recipientID);
            return new Result(true,"success","200");
        }catch(Exception e)
        {
            return new Result(false,Integer.toString(amount),"404");
        }
    }





//    //    查询某一企业某一状态的所有token
//    @GetMapping("/queryTokenStatus")
//    public List<Token> tokenQuery(@RequestParam("initOrg") String initOrg,@RequestParam("init_rec")int init_rec, @RequestParam("tokenStatus")String tokenStatus)
//    {
//        List<Token> tokens;
//        if(init_rec == 0){
//            tokens = tokenService.queryTokenByInitOrg(initOrg);}
//        else if(init_rec == 1){
//            tokens = tokenService.queryTokenByRecOrg(initOrg);
//        }else{
//            tokens = tokenService.queryTokenByInitOrg(initOrg);
//            tokens.addAll(tokenService.queryTokenByRecOrg(initOrg));
//        }
//
//        Iterator<Token> it = tokens.iterator();
//        while(it.hasNext())
//        {
//            Token token = it.next();
//            if(!token.getTokenStatus().equals(tokenStatus))
//            {
//                it.remove();
//            }
//        }
//        return tokens;
//    }

    //查询企业的所有token
    @RequestMapping("/queryOrgToken")
    public List<Token> queryToken(@RequestParam("orgName") String orgName,@RequestParam("init_rec")int init_rec, @RequestParam("tokenStatus")String tokenStatus){
        if(init_rec==0){
            List<Token> a = tokenService.queryTokenByInitOrg(orgName);
            Iterator<Token> iterator = a.iterator();
            while(iterator.hasNext())
            {
                Token token_temp = iterator.next();
                if(!token_temp.getTokenStatus().equals(tokenStatus))
                {
                    iterator.remove();
                }
            }
            return a;
        }
        else if(init_rec == 1){
            List<Token> a = tokenService.queryTokenByRecOrg(orgName);
            Iterator<Token> iterator = a.iterator();
            while(iterator.hasNext())
            {
                Token token_temp = iterator.next();
                if(!token_temp.getTokenStatus().equals(tokenStatus))
                {
                    iterator.remove();
                }
            }
            return a;
        }else{
            List<Token> a = tokenService.queryTokenByInitOrg(orgName);
            a.addAll(tokenService.queryTokenByRecOrg(orgName));
            Iterator<Token> iterator = a.iterator();
            while(iterator.hasNext())
            {
                Token token_temp = iterator.next();
                if(!token_temp.getTokenStatus().equals(tokenStatus))
                {
                    iterator.remove();
                }
            }
            return a;
        }
    }

    //查询企业的所有token
    @RequestMapping("/queryOrgTokenAll")
    public List<Token> queryTokenAll(@RequestParam("orgName")String orgName){

            List<Token> a = tokenService.queryTokenByInitOrg(orgName);
            a.addAll(tokenService.queryTokenByRecOrg(orgName));

            return a;
    }

    private List<Transaction> queryUtil(List<Transaction> transactions,String transType)
    {
        Iterator<Transaction> iterator = transactions.iterator();
        while(iterator.hasNext())
        {
            Transaction trans = iterator.next();
            if(!trans.getTransType().equals(transType))
            {
                iterator.remove();
            }
        }
        return transactions;
    }

}

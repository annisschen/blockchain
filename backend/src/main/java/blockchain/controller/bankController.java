package blockchain.controller;

import blockchain.entity.*;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.Impl.TokenServiceImpl;
import blockchain.service.Impl.TransactionServiceImpl;
import blockchain.service.TokenService;
import blockchain.utils.RSAUtils;
import blockchain.utils.Utils;
import ch.qos.logback.core.db.dialect.SybaseSqlAnywhereDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import blockchain.constant.uploadConstant;
import blockchain.constant.keyConstant;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping("/bank")
public class bankController {
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;

//    //同意授信
//    @RequestMapping("/approveCredit")
//    public Result bank_tokenApprove(@RequestParam("transID")String transID)
//    {
//        Transaction transaction = transactionService.queryTransactionByTransID(transID);
//        String initiatorID = transaction.getInitiatorID();
//        String recipientID = transaction.getRecipientID();
//
//        if(transaction==null)
//        {
//            System.out.println("不存在交易");
//            return new Result(false,"不存在交易","404");
//        }
//        Organization org = organizationService.queryOrganizationByOrgID(initiatorID);
//        if(org==null)
//        {
//            System.out.println("不存在机构");
//            return new Result(false,"不存在机构","404");
//        }
////    获得签名
//        String privateKeyPath = keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
//        String tranHash = transactionService.queryTranHashByTransID(transID);
//        try
//        {
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String recipientSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
//            transactionService.verifyTransaction(transID,initiatorID,recipientSignature);
//
////        token
//            Token token = new Token();
//
//            long now = System.currentTimeMillis();
//            String time = Utils.sdf(now);
//
////            String initiatorID = transaction.getInitiatorID();
//
//
//            System.out.println(initiatorID);
//            System.out.println(recipientID);
//
//            Organization initiatorOrg = organizationService.queryOrganizationByOrgID(initiatorID);
//            Organization recipientOrg = organizationService.queryOrganizationByOrgID(recipientID);
//////        String recipientID = transaction.getRecipientID();
//
//            token.setTransTimestamp(time);
//            token.setUpdateTimestamp(time);
//            token.setTokenID(UUID.randomUUID().toString());
//            token.setTokenStatus("credit");//授信
//            token.setBankID(recipientOrg.getOrgID());
//            token.setRecOrg(recipientOrg.getOrgName());
//            token.setInitOrg(initiatorOrg.getOrgName());
//            token.setAmount(transaction.getAmount());
//            token.setPaid(0);
//
//            tokenService.addToken(token);
//
////        修改钱包
//            int init_creditLimit = initiatorOrg.getCreditLimit();
//            organizationService.setCreditLimit(init_creditLimit+transaction.getAmount(),time,initiatorID);
//            int recip_creditLimit = recipientOrg.getCreditLimit();
//            organizationService.setCreditLimit(recip_creditLimit-transaction.getAmount(),time,recipientID);
//
//            return new Result(true,"success","200");
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }
//
//
//    //    不同意授信
//    @RequestMapping("/disapprove")
//    public Result tokenDisapprove(@RequestParam("transID")String transID)
//    {
//        Transaction trans = transactionService.queryTransactionByTransID(transID);
//        String recipientID = trans.getRecipientID();
//        String initiatoeID = trans.getInitiatorID();
//
//        if(trans==null)
//        {
//            System.out.println("不存在交易");
//            return new Result(false,"不存在交易","404");
//        }
//        Organization org = organizationService.queryOrganizationByOrgID(recipientID);
//        if(org==null)
//        {
//            System.out.println("不存在机构");
//            return new Result(false,"不存在机构","404");
//        }
////        获得签名
//        String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + recipientID + ".key";
//        String tranHash = transactionService.queryTranHashByTransID(transID);
//        try {
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String recipientSignature = RSAUtils.sign(tranHash.getBytes(), privateKey);
//            transactionService.verifyTransaction(transID, recipientID, recipientSignature);
//
//            transactionService.updateTransactionStatus(transID,"cancel");
//            return new Result(true,"不批准操作成功","404");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }

    //审核授信,查看申请人名单
    @RequestMapping("/queryRecName")
    public List<Transaction> queryRecName(@RequestParam("orgName")String orgName){

        String recID = organizationService.queryOrgIDByOrgName(orgName);
        List<Transaction> temp = transactionService.queryTransactionByRecipientID(recID);

        Iterator<Transaction> iterator = temp.iterator();
        while(iterator.hasNext()) {
            Transaction trans = iterator.next();
            if (!trans.getTransType().equals("credit")||!trans.getTransStatus().equals("wait")) {
                iterator.remove();
            }
        }


        return temp;
    }

    //同意承兑
    @RequestMapping("/accept")
    public Result tokenaccept(@RequestParam("tokenID")String tokenID,@RequestParam("tokenStatus")String tokenStatus){
        Token token = tokenService.queryTokenByTokenID(tokenID);
        token.setTokenStatus(tokenStatus);

        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);

        token.setUpdateTimestamp(time);

        tokenService.updateToken(token);

        if(token.getTokenStatus().equals("OK")){
            System.out.println(token.getTokenStatus());
            String recName = token.getRecOrg();
            String initName = token.getInitOrg();
            String recID = organizationService.queryOrgIDByOrgName(initName);
            String initID = organizationService.queryOrgIDByOrgName(recName);
            Organization rec = organizationService.queryOrganizationByOrgID(recID);
            Organization init = organizationService.queryOrganizationByOrgID(initID);
            int recCredit = rec.getCreditLimit();
            int initCredit = init.getCreditLimit();


           // 更新钱包余额
            try
            {
                organizationService.setCreditLimit(initCredit - token.getAmount(),time,initID);
                organizationService.setCreditLimit(recCredit + token.getAmount(),time,recID);


                return new Result(true,"success","200");
            }catch(Exception e)
            {
                return new Result(false,"承兑出现异常","404");
            }
        }
        else if(token.getTokenStatus().equals("reject")){
            return new Result(false,"银行拒绝承兑","200");
        }
        else{
            return new Result(false,"error,tokenStatus 错误","200");
        }
    }
}

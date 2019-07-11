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

//企业页面的控制器
@RestController
@RequestMapping("/enterprise")
public class enterpriceController {
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;


////  发起合同（订单合同，保险合同，发起物流合同。发起收货合同）
//    @RequestMapping("/transaction/sponsor/init")
//    public List<Organization> initSponsor(@RequestParam("orgName") String orgName)
//    {
//        System.out.println("/transaction/sponsor/init");
//        return organizationService.queryAll();
//    }

    //发起运输合同
    @RequestMapping("/ApplyDist")
    public Result ApplyTrans(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
                             @RequestParam("amount")int amount,
                             @RequestParam("information")String information, @RequestParam("contractName")String contractName,
                             @RequestParam("weight")int weight){
        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,"dist",amount,contractName,information);
        //生成transaction
//            生成基本信息-
//            生成合同hash
//            生成发起人签名
        String initiatorID = organizationService.queryOrgIDByOrgName(initiatorName);
        String recipientID = organizationService.queryOrgIDByOrgName(recipientName);
        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);
        String transStatus = "wait";
        //合同hash
        String contractPath = uploadConstant.path + File.separator + reqTransaction.getContractName();
        System.out.println(contractPath);
        try {
            String tranHash = Utils.getFileSHA256Str(contractPath);
//          合同ID
            String transID = UUID.randomUUID().toString();
//          发起人签名
            String privateKeyPath = keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String initiatorSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
            //合同添加到数据库（上链）
            transactionService.sponsorTransaction(initiatorName,recipientName,transID,tranHash,initiatorID,recipientID,amount,"dist",initiatorSignature,information,weight);

            return new Result(true,transID,"200");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }

    //审核授信,查看申请人名单
    @RequestMapping("/queryRecName")
    public List<Transaction> queryRecName(@RequestParam("orgName")String orgName,@RequestParam("transType")String transType){

        String recID = organizationService.queryOrgIDByOrgName(orgName);
        List<Transaction> temp = transactionService.queryTransactionByRecipientID(recID);

        Iterator<Transaction> iterator = temp.iterator();
        while(iterator.hasNext()) {
            Transaction trans = iterator.next();
            if (!trans.getTransType().equals(transType)||!trans.getTransStatus().equals("wait")) {
                iterator.remove();
            }
        }


        return temp;
    }

    //发起授信合同
    @PostMapping("/ApplyCredit")
    public Result sponsor(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
                          @RequestParam("amount")int amount,
                          @RequestParam("information")String information, @RequestParam("contractName")String contractName)
    {
        System.out.println("/transaction/sponsor");
//        String initiatorName = reqTransaction.getInitiatorName();
//        String recipientName = reqTransaction.getRecipientName();
//        String transType = reqTransaction.getTransType();
//        int amount = reqTransaction.getAmount();
        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,"credit",amount,contractName,information);
        //生成transaction
//            生成基本信息-
//            生成合同hash
//            生成发起人签名
        String initiatorID = organizationService.queryOrgIDByOrgName(initiatorName);
        String recipientID = organizationService.queryOrgIDByOrgName(recipientName);
        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);
        String transStatus = "wait";
        //合同hash
        String contractPath = uploadConstant.path + File.separator + reqTransaction.getContractName();
        System.out.println(contractPath);
        try {
            String tranHash = Utils.getFileSHA256Str(contractPath);
//          合同ID
            String transID = UUID.randomUUID().toString();
//          发起人签名
            String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String initiatorSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);

            transactionService.sponsorTransaction(initiatorName,recipientName,transID,tranHash,initiatorID,recipientID,amount,"credit",initiatorSignature,information,0);

            return new Result(true,transID,"200");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }





    //发起保险，订单，兑付合同
    @RequestMapping("/ApplyRest")
    public Result ApplyRest(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
                             @RequestParam("amount")int amount, @RequestParam("transType") String transType,
                             @RequestParam("information")String information, @RequestParam("contractName")String contractName){
        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,transType,amount,contractName,information);
        //生成transaction
//            生成基本信息-
//            生成合同hash
//            生成发起人签名
        String initiatorID = organizationService.queryOrgIDByOrgName(initiatorName);
        String recipientID = organizationService.queryOrgIDByOrgName(recipientName);
        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);
        String transStatus = "wait";
        //合同hash
        String contractPath = uploadConstant.path + File.separator + reqTransaction.getContractName();
        System.out.println(contractPath);
        try {
            String tranHash = Utils.getFileSHA256Str(contractPath);
//          合同ID
            String transID = UUID.randomUUID().toString();
//          发起人签名
            String privateKeyPath = keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String initiatorSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
            //合同添加到数据库（上链）
            transactionService.sponsorTransaction(initiatorName,recipientName,transID,tranHash,initiatorID,recipientID,amount,transType,initiatorSignature,information,0);
            Organization initiatorOrg = organizationService.queryOrganizationByOrgID(initiatorID);
            Organization recipientOrg = organizationService.queryOrganizationByOrgID(recipientID);

            if(transType.equals("token")){
                Token token = new Token();
                token.setTransTimestamp(time);
                token.setUpdateTimestamp(time);
                token.setTokenID(UUID.randomUUID().toString());
                token.setTokenStatus("wait");//授信
                token.setBankID(recipientOrg.getOrgID());
                token.setRecOrg(recipientOrg.getOrgName());
                token.setInitOrg(initiatorOrg.getOrgName());
                token.setAmount(amount);
                token.setPaid(0);

                tokenService.addToken(token);
            }


            return new Result(true,transID,"200");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }



    //承兑token
    @RequestMapping("/acceptApply")
    public Result tokenAcceptApply(@RequestParam("fromOrg")String fromOrg,@RequestParam("amount")int amount,@RequestParam("password")String password, @RequestParam("bankName")String bankName)
    {
        String orgID = organizationService.queryOrgIDByOrgName(fromOrg);
        String bankID = organizationService.queryOrgIDByOrgName(bankName);
        if(organizationService.checkPasswordByOrgID(password,orgID)!=1)
        {
            System.out.println("密码错误");
            return new Result(false,"密码错误","404");
        }
        int creditLimit = organizationService.queryCreditLimit(orgID);
        if(creditLimit<amount)
        {
            System.out.println("额度不足");
            return new Result(false,"额度不足","404");
        }
//        token的接收人为银行

        Token token = new Token();
        token.setInitOrg(fromOrg);
        token.setRecOrg(bankName);
        token.setPaid(0);
        token.setAmount(amount);
        token.setBankID(bankID);
        token.setTokenStatus("wait");
        token.setTokenID(UUID.randomUUID().toString());
        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);
        token.setUpdateTimestamp(time);
        token.setTransTimestamp(time);

        tokenService.addToken(token);

//        //更新钱包余额
//        try
//        {
//            organizationService.setCreditLimit(creditLimit-amount,time,orgID);
//            organizationService.setCreditLimit(creditLimit+amount,time,bankID);
//
//            return new Result(true,"success","200");
//        }catch(Exception e)
//        {
//            return new Result(false,"承兑出现异常","404");
//        }
        return new Result(true,"申请承兑成功","200");
    }


    // 入库合同决定
    @RequestMapping("/dealWarehouse")
    public Result tokenApprove(@RequestParam("transID")String transID,@RequestParam("transStatus")String transStatus)
    {
        Transaction trans = transactionService.queryTransactionByTransID(transID);
        String recipientID = trans.getRecipientID();
        String initiatorID = trans.getInitiatorID();
//        String distID = organizationService.queryOrgIDByOrgName(trans.getInformation());

        String recName = organizationService.queryOrganizationByOrgID(recipientID).getOrgName();
        String initName = organizationService.queryOrganizationByOrgID(initiatorID).getOrgName();
//        String distName = organizationService.queryOrganizationByOrgID(distID).getOrgName();

        if(trans==null)
        {
            System.out.println("不存在交易");
            return new Result(false,"不存在交易","404");
        }
        Organization recipientOrg = organizationService.queryOrganizationByOrgID(recipientID);
        Organization initiatorOrg = organizationService.queryOrganizationByOrgID(initiatorID);
//        Organization distOrg = organizationService.queryOrganizationByOrgID(distID);

        if(recipientOrg==null)
        {
            System.out.println("不存在接受机构");
            return new Result(false,"不存在机构","404");
        }
//        获得签名
        String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + recipientID + ".key";
        String tranHash = transactionService.queryTranHashByTransID(transID);
        try
        {
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String recipientSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
            transactionService.verifyTransaction(transID,recipientID,recipientSignature);

            long now = System.currentTimeMillis();
            String time = Utils.sdf(now);

            if(transStatus.equals("signed")){
//                //修改钱包
//                int recCreditLimit = recipientOrg.getCreditLimit();
//                organizationService.setCreditLimit(recCreditLimit - trans.getAmount(),time,recipientID);
//                int initCreditLimit = initiatorOrg.getCreditLimit();
//                organizationService.setCreditLimit(initCreditLimit + trans.getAmount(),time,initiatorID);

                //修改库存
              int recStore = recipientOrg.getStore();
              int initStore = initiatorOrg.getStore();
              organizationService.setStore(recStore+trans.getWeight(),time,recipientID);
              organizationService.setStore(initStore-trans.getWeight(),time,initiatorID);
                return new Result(true,"success","200");
            }
            else if (transStatus.equals(("cancel"))){
                return new Result(false,"已取消","200");
            }else{
                return new Result(false,"error","200");}
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }

    //处理order 保险 运输 授信 合同
    @RequestMapping("/dealRest")
    public Result dealRest(@RequestParam("transID")String transID,@RequestParam("transStatus")String transStatus){
        Transaction trans = transactionService.queryTransactionByTransID(transID);
        String recipientID = trans.getRecipientID();
        String initiatorID = trans.getInitiatorID();

        String recName = organizationService.queryOrganizationByOrgID(recipientID).getOrgName();
        String initName = organizationService.queryOrganizationByOrgID(initiatorID).getOrgName();


        if(trans==null)
        {
            System.out.println("不存在交易");
            return new Result(false,"不存在交易","404");
        }
        Organization recipientOrg = organizationService.queryOrganizationByOrgID(recipientID);
        Organization initiatorOrg = organizationService.queryOrganizationByOrgID(initiatorID);

        if(recipientOrg==null)
        {
            System.out.println("不存在接受机构");
            return new Result(false,"不存在机构","404");
        }
//        获得签名
        String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + recipientID + ".key";
        String tranHash = transactionService.queryTranHashByTransID(transID);
        try
        {
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String recipientSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
            transactionService.verifyTransaction(transID,recipientID,recipientSignature);

            long now = System.currentTimeMillis();
            String time = Utils.sdf(now);

//            trans.setTransStatus(transStatus);
            transactionService.updateTransactionStatus(transID,transStatus);

            if(transStatus.equals("signed")){

                //修改钱包
                int recCreditLimit = recipientOrg.getCreditLimit();
                organizationService.setCreditLimit(recCreditLimit + trans.getAmount(),time,recipientID);
                int initCreditLimit = initiatorOrg.getCreditLimit();
                organizationService.setCreditLimit(initCreditLimit - trans.getAmount(),time,initiatorID);

                //修改库存
                if(trans.getTransType().equals("dist")){
//                    String shouhuoName = trans.getInformation();
//                    Organization shouhuoOrg = organizationService.queryOrganizationByOrgName(shouhuoName);

                    int recStore = recipientOrg.getStore();
                    int initStore = initiatorOrg.getStore();
                    organizationService.setStore(recStore+trans.getWeight(),time,recipientID);
                    organizationService.setStore(initStore-trans.getWeight(),time,initiatorID);
                }


                return new Result(true,"success","200");
            }
            else if (transStatus.equals(("cancel"))){
                return new Result(false,"已取消","200");
            }else{
                return new Result(false,"error","200");}
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }


//    //确认合同hash（订单合同，保险合同，发起物流合同。发起收货合同）
//    @PostMapping("/verifyHash")
//    public Result verifyHash(@RequestParam("contract")MultipartFile file, @RequestParam("transID")String transID)
//    {
//        File filepath = new File(uploadConstant.path);
//        if(!filepath.exists()&&!filepath.isDirectory())
//        {
//            System.out.println("新建目录");
//            filepath.mkdirs();
//        }
//        String fileName = file.getOriginalFilename();
//        File targetFile = new File(uploadConstant.path, fileName);
//        try{
//            file.transferTo(targetFile);
//            System.out.println("上传成功");
//
//            String contractPath = uploadConstant.path+File.separator+fileName;
//            String tranHash = Utils.getFileSHA256Str(contractPath);
//            String tranHashReal = transactionService.queryTranHashByTransID(transID);
//            if(!tranHash.equals(tranHashReal))
//            {
//                //删除缓存的合同文件
//                Files.deleteIfExists(Paths.get(contractPath));
//                return new Result(false,"合同不同","404");
//            }
//            //删除缓存的合同文件
//            Files.deleteIfExists(Paths.get(contractPath));
//            return new Result(true,"success","200");
//
//        }
//        catch(Exception e)
//        {
//            System.out.println("上传失败");
//            e.printStackTrace();
//            return new Result(false,"上传失败"+e.getMessage(),"404");
//        }
//    }
//    //确认合同，需要前端保证验证过文件hash一致后才可以触发，这个方法中不检查是否验证过
//    @RequestMapping("/transaction/verify")
//    public Result verify(@RequestParam("transID")String transID,@RequestParam("recOrg")String recOrg)
//    {
//        String recipientID = organizationService.queryOrgIDByOrgName(recOrg);
//        Transaction trans = transactionService.queryTransactionByTransID(transID);
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
//        try
//        {
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String recipientSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
//            transactionService.verifyTransaction(transID,recipientID,recipientSignature);
//
//            return new Result(true,"操作成功","200");
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }

////    个人主页
//    @GetMapping("/profile")
//    public Organization profile(@RequestParam("orgName") String orgName)
//    {
//        // TODO: 2019/7/4 可能需要新建数据库的表来记录
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        return organizationService.queryOrganizationByOrgID(orgID);
//    }
////    修改信息
////    @RequestMapping("/profile/update")
////    public Result updateProfile(@RequestBody Organization organization)
////    {
////        try{
////            // TODO: 2019/7/4 updateOrganization实现
////            organizationService.updateOrganization(organization);
////            return new Result(true,"success","200");
////        }
////        catch (Exception e)
////        {
////            e.printStackTrace();
////            return new Result(false,e.getMessage(),"404");
////        }
////    }
////    修改密码
//    // TODO: 2019/7/4  修改密码


//    private List<Transaction> queryUtil(List<Transaction> transactions,String transType)
//    {
//        Iterator<Transaction> iterator = transactions.iterator();
//        while(iterator.hasNext())
//        {
//            Transaction trans = iterator.next();
//            if(!trans.getTransType().equals(transType))
//            {
//                iterator.remove();
//            }
//        }
//        return transactions;
//    }
//
//    private List<Transaction> queryUtil_states(List<Transaction> transactions,String transType, String transStates)
//    {
//        Iterator<Transaction> iterator = transactions.iterator();
//        while(iterator.hasNext())
//        {
//            Transaction trans = iterator.next();
//            if(!trans.getTransType().equals(transType)||!trans.getTransStatus().equals(transStates))
//            {
//                iterator.remove();
//            }
//        }
//        return transactions;
//    }
////    查询所有发起的合同（合同状态为wait，signed，cancel）
//    @RequestMapping("/transaction/queryFrom")
//    public List<Transaction> transQueryFrom(@RequestParam("orgName") String orgName)
//    {
//        System.out.println("/transaction/queryFrom");
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        List<Transaction> totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
//        return queryUtil(totalTrans,"order");
//    }
//
//    //    查询某一机构类型下的所有公司名称
//    @RequestMapping("/queryOrgType")
//    public List<Organization> QueryType(@RequestParam("orgType") String orgType)
//    {
//        System.out.println("/queryOrgType");
//        return organizationService.queryOrgType(orgType);
//    }
//
//    //    查询所有接受的合同（合同状态为wait，signed，cancel）
//    @RequestMapping("/transaction/queryRec")
//    public List<Transaction> transQueryRec(@RequestParam("orgName") String orgName)
//    {
//        System.out.println("/transaction/queryRec");
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        System.out.println(orgID);
//        List<Transaction> totalTrans = transactionService.queryTransactionByRecipientID(orgID);
//        return  queryUtil(totalTrans,"order");
//    }
//
//    //    查询所有接受的合同（合同状态为wait）
//    @RequestMapping("/transaction/queryRec_wait")
//    public List<Transaction> transQueryRec_wait(@RequestParam("orgName") String orgName)
//    {
//        System.out.println("/transaction/queryRec_wait");
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        System.out.println(orgID);
//        List<Transaction> totalTrans = transactionService.queryTransactionByRecipientID(orgID);
//        return  queryUtil_states(totalTrans,"order","wait");
//    }
//
////    查询物流合同
//    @RequestMapping("/distribution/query")
//    public List<Transaction> distriQuery(@RequestParam("orgName") String orgName)
//    {
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        List<Transaction> totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
//        return queryUtil(totalTrans,"dist");
//    }
////    查询保险合同
//    @RequestMapping("/insurance/query")
//    public List<Transaction> insuranceQuery(@RequestParam("orgName") String orgName)
//    {
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        List<Transaction> totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
//        return queryUtil(totalTrans,"insurance");
//    }
//    // TODO: 2019/7/4 查询库存信息 -- hong
////    @RequestMapping("/inventory/query")
////    public List<>
//
////    支付token
//    @RequestMapping("/token/pay")
//    public Result tokenPay(@RequestParam("fromOrg")String fromOrg,@RequestParam("recOrg")String recOrg,@RequestParam("amount")int amount,@RequestParam("password")String password)
//    {
//        //生成token
//            String tokenID = UUID.randomUUID().toString();
//            String initiatorID = organizationService.queryOrgIDByOrgName(fromOrg);
//            String recipientID = organizationService.queryOrgIDByOrgName(recOrg);
//            String bankID = organizationService.queryBankID(initiatorID);
//            int creditLimit = organizationService.queryCreditLimit(initiatorID);
//            if(organizationService.checkPasswordByOrgID(password,initiatorID)!=1)
//            {
//                System.out.println("密码错误");
//                return new Result(false,"密码错误","404");
//            }
//            long now = System.currentTimeMillis();
//            String time = Utils.sdf(now);
//            if(amount>creditLimit)
//            {
//                System.out.println("额度不足");
//                return new Result(false,"额度不足","404");
//            }
//            String tokenStatus = "U";
//            Token token = new Token();
//            token.setTokenID(tokenID);
//            token.setInitOrg(fromOrg);
//            token.setRecOrg(recOrg);
//            token.setBankID(bankID);
//            token.setAmount(amount);
//            token.setPaid(0);
//            token.setTokenStatus(tokenStatus);
//            token.setTransTimestamp(time);
//            token.setUpdateTimestamp(time);
//
//            tokenService.addToken(token);
//
//        //更新钱包余额
//            try
//            {
//                organizationService.setCreditLimit(creditLimit-amount,time,initiatorID);
////            更新接收人的钱包余额
//                creditLimit = organizationService.queryCreditLimit(recipientID);
//                organizationService.setCreditLimit(creditLimit+amount,time,recipientID);
//                return new Result(true,"success","200");
//            }catch(Exception e)
//            {
//                return new Result(false,Integer.toString(amount),"404");
//            }
//    }
//    //    承兑token
//    @RequestMapping("/token/accept")
//    public Result tokenAccept(@RequestParam("fromOrg")String fromOrg,@RequestParam("amount")int amount,@RequestParam("password")String password, @RequestParam("bankName")String bankName)
//    {
//        String orgID = organizationService.queryOrgIDByOrgName(fromOrg);
//        String bankID = organizationService.queryOrgIDByOrgName(bankName);
//        if(organizationService.checkPasswordByOrgID(password,orgID)!=1)
//        {
//            System.out.println("密码错误");
//            return new Result(false,"密码错误","404");
//        }
//        int creditLimit = organizationService.queryCreditLimit(orgID);
//        if(creditLimit<amount)
//        {
//            System.out.println("额度不足");
//            return new Result(false,"额度不足","404");
//        }
////        token的接收人为银行
//
//        Token token = new Token();
//        token.setInitOrg(fromOrg);
//        token.setRecOrg(bankName);
//        token.setPaid(0);
//        token.setAmount(amount);
//        token.setBankID(bankID);
//        token.setTokenStatus("C");
//        token.setTokenID(UUID.randomUUID().toString());
//        long now = System.currentTimeMillis();
//        String time = Utils.sdf(now);
//        token.setUpdateTimestamp(time);
//        token.setTransTimestamp(time);
//
//        tokenService.addToken(token);
//
//        //更新钱包余额
//        try
//        {
//            organizationService.setCreditLimit(creditLimit-amount,time,orgID);
//            organizationService.setCreditLimit(creditLimit+amount,time,bankID);
//
//            return new Result(true,"success","200");
//        }catch(Exception e)
//        {
//            return new Result(false,"承兑出现异常","404");
//        }
//    }
////    查询发起的订单交易token
//    @GetMapping("/token/query")
//    public List<Token> tokenQuery(@RequestParam("fromOrg") String fromOrg)
//    {
//        List<Token> tokens = tokenService.queryTokenByFromOrg(fromOrg);
//        Iterator<Token> it = tokens.iterator();
//        while(it.hasNext())
//        {
//            Token token = it.next();
//            if(!token.getTokenStatus().equals("U"))//非订单交易token
//            {
//                it.remove();
//            }
//        }
//        return tokens;
//    }
//
//    //查询token
//    @RequestMapping("/token/queryToken")
//    public List<Token> queryToken(@RequestParam("orgName") String orgName){
//        Token token_test = tokenService.queryTokenByTokenID("606f1da4-18b9-4f06-8f0a-9edf47cb83dd");
//        System.out.println(token_test);
//        return tokenService.queryTokenByRecOrg(orgName);
//    }
//
////    private List<Transaction> queryTrans_states(List<Transaction> transactions,String transType, String transStates)
////    {
////        Iterator<Transaction> iterator = transactions.iterator();
////        while(iterator.hasNext())
////        {
////            Transaction trans = iterator.next();
////            if(!trans.getTransType().equals(transType)||!trans.getTransStatus().equals(transStates))
////            {
////                iterator.remove();
////            }
////        }
////        return transactions;
////    }
//
//    //查询授信
//    @RequestMapping("/queryCredit")
//    public List<Transaction> queryCredit(@RequestParam("orgName") String orgName){
//
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        List<Transaction> totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
//        return queryUtil(totalTrans,"credit");
//    }
//
//    //申请授信
//    @RequestMapping("/token/apply")
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
//            String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String initiatorSignature = RSAUtils.sign("apply credit".getBytes(),privateKey);
//
//            transactionService.sponsorTransaction(initiatorName,recipientName, transID,"apply credit",initiatorID,recipientID,amount,transType,initiatorSignature,information);
//
//            return new Result(true,transID,"200");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }
////    同意授信
//    @RequestMapping("/token/approve")
//    public Result tokenApprove(@RequestParam("transID")String transID,@RequestParam("recOrg")String recOrg)
//    {
//        String recipientID = organizationService.queryOrgIDByOrgName(recOrg);
//        Transaction trans = transactionService.queryTransactionByTransID(transID);
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
//            Transaction transaction = transactionService.queryTransactionByTransID(transID);
//            String initiatorID = transaction.getInitiatorID();
//            Organization organization = organizationService.queryOrganizationByOrgID(initiatorID);
//            String fromOrg = transaction.getRecipientOrgName();
////            String recipientID = transaction.getRecipientID();
//
//            token.setTransTimestamp(time);
//            token.setUpdateTimestamp(time);
//            token.setTokenID(UUID.randomUUID().toString());
//            token.setTokenStatus("W");//授信
//            token.setBankID(recipientID);//自己就是银行
//            token.setRecOrg(recOrg);
//            token.setInitOrg(fromOrg);
//            token.setAmount(transaction.getAmount());
//            token.setPaid(0);
//
//            tokenService.addToken(token);
//
////            修改钱包
//            int creditLimit = organization.getCreditLimit();
//            organizationService.setCreditLimit(creditLimit+transaction.getAmount(),time,initiatorID);
//            return new Result(true,"success","200");
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }
////    不同意授信
//    @RequestMapping("/token/disapprove")
//    public Result tokenDisapprove(@RequestParam("transID")String transID,@RequestParam("recOrg")String recOrg)
//    {
//        String recipientID = organizationService.queryOrgIDByOrgName(recOrg);
//        Transaction trans = transactionService.queryTransactionByTransID(transID);
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

//    //申请贷款
//    @RequestMapping("/loan/apply")
//    public Result loanApply(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
//                             @RequestParam("transType")String transType, @RequestParam("amount")int amount,
//                             @RequestParam("information")String information, @RequestParam("contractName")String contractName)
//    {
//        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,transType,amount,contractName,information);
//        String initiatorID = organizationService.queryOrgIDByOrgName(initiatorName);
//        String recipientID = organizationService.queryOrgIDByOrgName(recipientName);
//        long now = System.currentTimeMillis();
//        String time = Utils.sdf(now);
//        String transStatus = "wait";
////      合同ID
//        String transID = UUID.randomUUID().toString();
////          发起人签名
//        try{
//            String privateKeyPath =keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String initiatorSignature = RSAUtils.sign("apply loan".getBytes(),privateKey);
//
//            transactionService.sponsorTransaction(initiatorName,recipientName, transID,"apply loan",initiatorID,recipientID,amount,transType,initiatorSignature,information);
//
//            return new Result(true,transID,"200");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }
//
//    //查询贷款
//    @RequestMapping("/queryLoan")
//    public List<Transaction> queryLoan(@RequestParam("orgName") String orgName){
//
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        List<Transaction> totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
//        return queryUtil(totalTrans,"loan");
//    }


}

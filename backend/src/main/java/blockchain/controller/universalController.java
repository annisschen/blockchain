package blockchain.controller;

import blockchain.constant.keyConstant;
import blockchain.constant.uploadConstant;
import blockchain.entity.*;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.Impl.TokenServiceImpl;
import blockchain.service.Impl.TransactionServiceImpl;
import blockchain.utils.RSAUtils;
import blockchain.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@RestController
public class universalController {
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;

    //  发起合同（订单合同，保险合同，发起物流合同。发起收货合同）
    @RequestMapping("/transaction/sponsor/init")
    public List<Organization> initSponsor(@RequestParam("orgName") String orgName)
    {
        System.out.println("/transaction/sponsor/init");
        return organizationService.queryAll();
    }

//    //发起合同
//    @PostMapping("/transaction/sponsor")
//    public Result sponsor(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
//                          @RequestParam("transType")String transType, @RequestParam("amount")int amount,
//                          @RequestParam("information")String information, @RequestParam("contractName")String contractName)
//    {
//        System.out.println("/transaction/sponsor");
////        String initiatorName = reqTransaction.getInitiatorName();
////        String recipientName = reqTransaction.getRecipientName();
////        String transType = reqTransaction.getTransType();
////        int amount = reqTransaction.getAmount();
//        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,transType,amount,contractName,information);
//        //生成transaction
////            生成基本信息-
////            生成合同hash
////            生成发起人签名
//        String initiatorID = organizationService.queryOrgIDByOrgName(initiatorName);
//        String recipientID = organizationService.queryOrgIDByOrgName(recipientName);
//        long now = System.currentTimeMillis();
//        String time = Utils.sdf(now);
//        String transStatus = "wait";
//        //合同hash
//        String contractPath = uploadConstant.path + File.separator + reqTransaction.getContractName();
//        System.out.println(contractPath);
//        try {
//            String tranHash = Utils.getFileSHA256Str(contractPath);
////          合同ID
//            String transID = UUID.randomUUID().toString();
////          发起人签名
//            String privateKeyPath = keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
//            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
//            String initiatorSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
//            //合同添加到数据库（上链）
//            transactionService.sponsorTransaction(initiatorName,recipientName,transID,tranHash,initiatorID,recipientID,amount,transType,initiatorSignature,information);
//
//            return new Result(true,transID,"200");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }

    //确认合同hash（订单合同，保险合同，发起物流合同。发起收货合同）
    @PostMapping("/verifyHash")
    public Result verifyHash(@RequestParam("contract") MultipartFile file, @RequestParam("transID")String transID)
    {
        File filepath = new File(uploadConstant.path);
        if(!filepath.exists()&&!filepath.isDirectory())
        {
            System.out.println("新建目录");
            filepath.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        File targetFile = new File(uploadConstant.path, fileName);
        try{
            file.transferTo(targetFile);
            System.out.println("上传成功");

            String contractPath = uploadConstant.path+File.separator+fileName;
            String tranHash = Utils.getFileSHA256Str(contractPath);
            String tranHashReal = transactionService.queryTranHashByTransID(transID);
            if(!tranHash.equals(tranHashReal))
            {
                //删除缓存的合同文件
                Files.deleteIfExists(Paths.get(contractPath));
                return new Result(false,"合同不同","404");
            }
            //删除缓存的合同文件
            Files.deleteIfExists(Paths.get(contractPath));
            return new Result(true,"success","200");

        }
        catch(Exception e)
        {
            System.out.println("上传失败");
            e.printStackTrace();
            return new Result(false,"上传失败"+e.getMessage(),"404");
        }
    }
    //确认合同，需要前端保证验证过文件hash一致后才可以触发，这个方法中不检查是否验证过
    @RequestMapping("/transaction/verify")
    public Result verify(@RequestParam("transID")String transID,@RequestParam("recOrg")String recOrg)
    {
        String recipientID = organizationService.queryOrgIDByOrgName(recOrg);
        Transaction trans = transactionService.queryTransactionByTransID(transID);
        if(trans==null)
        {
            System.out.println("不存在交易");
            return new Result(false,"不存在交易","404");
        }
        Organization org = organizationService.queryOrganizationByOrgID(recipientID);
        if(org==null)
        {
            System.out.println("不存在机构");
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

            return new Result(true,"操作成功","200");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }

    private List<Transaction> queryUtil_type(List<Transaction> transactions, String transType)
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

    private List<Transaction> queryUtil_states(List<Transaction> transactions, String transStates)
    {
        Iterator<Transaction> iterator = transactions.iterator();
        while(iterator.hasNext())
        {
            Transaction trans = iterator.next();
            if(!trans.getTransStatus().equals(transStates))
            {
                iterator.remove();
            }
        }
        return transactions;
    }
    //    查询所有和orgName有关的合同（合同状态为wait，signed，cancel）
    @RequestMapping("/transaction/queryType")
    public List<Transaction> transQueryFrom(@RequestParam("orgName") String orgName, @RequestParam("transType")String transType, @RequestParam("init_rec")int init_rec)
    {
        System.out.println("/transaction/queryType");
        String orgID = organizationService.queryOrgIDByOrgName(orgName);
        List<Transaction> totalTrans;
        if(init_rec == 0){
            totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
        }else if(init_rec==1){
            totalTrans = transactionService.queryTransactionByRecipientID(orgID);
        }else{
            totalTrans = transactionService.queryTransactionByRecipientID(orgID);
            totalTrans.addAll(transactionService.queryTransactionByInitiatorID(orgID));
        }
        return queryUtil_type(totalTrans,transType);
    }

    //    查询某一机构类型下的所有公司名称
    @RequestMapping("/queryOrgType")
    public List<Organization> QueryType(@RequestParam("orgType") String orgType)
    {
        System.out.println("/queryOrgType");
        return organizationService.queryOrgType(orgType);
    }

    //    查询所有接受的合同（合同状态为wait，signed，cancel）
    @RequestMapping("/transaction/queryStatus")
    public List<Transaction> transQueryRec(@RequestParam("orgName") String orgName,@RequestParam("transStatus")String transStatus,@RequestParam("init_rec")int init_rec)
    {
        System.out.println("/transaction/queryStatus");
        String orgID = organizationService.queryOrgIDByOrgName(orgName);
        List<Transaction> totalTrans;
        if (init_rec==0){
            totalTrans = transactionService.queryTransactionByInitiatorID(orgID);
        }
        else if(init_rec==1){
            totalTrans = transactionService.queryTransactionByRecipientID(orgID);}
        else{
            totalTrans = transactionService.queryTransactionByRecipientID(orgID);
            totalTrans.addAll(transactionService.queryTransactionByInitiatorID(orgID));
        }
        return  queryUtil_states(totalTrans,transStatus);
    }

//    //    查询所有wait的合同（合同状态为wait）
//    @RequestMapping("/transaction/queryRec_wait")
//    public List<Transaction> transQueryRec_wait(@RequestParam("orgName") String orgName)
//    {
//        System.out.println("/transaction/queryRec_wait");
//        String orgID = organizationService.queryOrgIDByOrgName(orgName);
//        System.out.println(orgID);
//        List<Transaction> totalTrans = transactionService.queryTransactionByRecipientID(orgID);
//        return  queryUtil_states(totalTrans,"order","wait");
//    }



    //查询授信(某个公司的token 数量)
    @RequestMapping("/queryCredit")
    public int queryCredit(@RequestParam("orgName") String orgName){

        String orgID = organizationService.queryOrgIDByOrgName(orgName);
        Organization org = organizationService.queryOrganizationByOrgID(orgID);
        return org.getCreditLimit();
    }

    @RequestMapping("/queryStore")
    public int queryStore(@RequestParam("orgName")String orgName){
        String orgID = organizationService.queryOrgIDByOrgName(orgName);
        Organization org = organizationService.queryOrganizationByOrgID(orgID);
        return org.getStore();
    }

//    @RequestMapping("/transaction/recQueryType")
//    public int
}

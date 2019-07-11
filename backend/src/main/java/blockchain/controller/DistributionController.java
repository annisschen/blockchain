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
@RequestMapping("/dist")
public class DistributionController {
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;

    //发起入库合同
    @RequestMapping("/ApplyWarehouse")
    public Result ApplyWarehouse(@RequestParam("initiatorName")String initiatorName, @RequestParam("recipientName")String recipientName,
                                 @RequestParam("weight") int weight,
                                 @RequestParam("information")String information, @RequestParam("contractName")String contractName){
        ReqTransaction reqTransaction = new ReqTransaction(initiatorName,recipientName,"warehouse",0,contractName,information);
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

            transactionService.sponsorTransaction(initiatorName,recipientName,transID,tranHash,initiatorID,recipientID,0,"warehouse",initiatorSignature,information,weight);



            return new Result(true,transID,"200");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,e.getMessage(),"404");
        }
    }



}

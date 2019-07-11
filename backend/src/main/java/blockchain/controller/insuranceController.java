package blockchain.controller;

import blockchain.constant.keyConstant;
import blockchain.constant.uploadConstant;
import blockchain.entity.ReqTransaction;
import blockchain.entity.Result;
import blockchain.entity.Transaction;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.Impl.TokenServiceImpl;
import blockchain.service.Impl.TransactionServiceImpl;
import blockchain.utils.RSAUtils;
import blockchain.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/insurance")
public class insuranceController {
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;




}

package blockchain.controller;
//用于上传合同文件

import blockchain.entity.Result;
import blockchain.entity.Transaction;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.Impl.TokenServiceImpl;
import blockchain.service.Impl.TransactionServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import blockchain.constant.uploadConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class uploadController {

    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TokenServiceImpl tokenService;


    @RequestMapping("/upload")
    public Result upload(@RequestParam("contract")MultipartFile file)
    {
        System.out.println("/upload");
        File filepath = new File(uploadConstant.path);
        if(!filepath.exists()&&!filepath.isDirectory())
        {
            System.out.println("新建目录");
            System.out.println(filepath);
            filepath.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        File targetFile = new File(uploadConstant.path, fileName);
        try{
            file.transferTo(targetFile);
            System.out.println("上传成功");
            System.out.println(targetFile);
            return new Result(true,"上传成功","200");
        }
        catch(Exception e)
        {
            System.out.println("上传失败");
            e.printStackTrace();
            return new Result(false,"上传失败"+e.getMessage(),"404");
        }
    }

//    //下载文件
//    @RequestMapping("/download")
//    public File download(@RequestParam("transID")String transID)
//    {
//
//        System.out.println("/download");
//        Transaction trans = transactionService.queryTransactionByTransID(transID);
//        String path = trans.getInformation();
//        File filepath = new File(uploadConstant.path,path);
//    }

    @RequestMapping ("/download")
    public ResponseEntity<byte[]> download(@RequestParam("transID")String transID) throws Exception{
        // 下载路径
        Transaction trans = transactionService.queryTransactionByTransID(transID);
        String filename = trans.getInformation();
        File file = new File(uploadConstant.path+File.separator + filename);
        HttpHeaders headers = new HttpHeaders();
        // 解决中文乱码
        String downloadfile =  new String(filename.getBytes("UTF-8"),"iso-8859-1");
        // 以下载方式打开文件
        headers.setContentDispositionFormData("attachment", downloadfile);
        // 二进制流
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);

    }
}

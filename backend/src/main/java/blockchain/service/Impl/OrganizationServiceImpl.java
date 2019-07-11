package blockchain.service.Impl;

import blockchain.dao.OrganizationDao;
import blockchain.entity.Organization;
import blockchain.service.OrganizationService;
import blockchain.utils.RSAUtils;
import blockchain.utils.Utils;
import blockchain.solidity.*;
import lombok.extern.java.Log;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import blockchain.constant.keyConstant;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private Wallet wallet;
    @Autowired
    private User user;

    @Override
    public boolean setCreditLimit(int amount, String updateTime, String orgID)throws ExecutionException, InterruptedException
    {
//        用于设置token额度
//        先检查creditLimit是否合法
//        再检查是否存在机构
        if(amount<0)
        {
            System.out.println("额度小于零");
            return false;
        }
        Organization org = organizationDao.queryOrgnizationByOrgID(orgID);
        if(org==null)
        {
            System.out.println("机构不存在");
            return false;
        }
        organizationDao.setCreditLimitByOrgID(amount,updateTime,orgID);

        //区块链操作**********
        try {
            wallet.credit(orgID, BigInteger.valueOf(amount)).send();
        }
        catch (Exception e){ e.printStackTrace(); }
        return true;
    }

    @Override
    public int queryCreditLimit(String orgID)
    {
//        查询机构的信用额度
        Organization org = organizationDao.queryOrgnizationByOrgID(orgID);
        if(org==null){
            System.out.println("机构不存在");
            return -1;
        }
        return organizationDao.queryCreditLimitByOrgID(orgID);
    }

    @Override
    public int checkPasswordByOrgID(String password, String orgID)
    {
//    登录判断用
//    -1表示未注册；
//    else
//        1表示密码机构类型正确；
//        0表示机构正确，密码错误；
        Organization org = organizationDao.queryOrgnizationByOrgID(orgID);
        if(org==null)
        {
            System.out.println("机构未注册");
            return -1;
        }
        String pwd = org.getPassword();
        if(Utils.getSHA256Str(password).equals(pwd))//后期可以考虑加盐
        {
            System.out.println("正确");
            return 1;
        }
        else
        {
            System.out.println("密码错误");
            return 0;
        }
    }

    @Override
    public Organization queryOrganizationByOrgID(String orgID)
    {
//        查询机构信息
//        不进行安全检查
//        检查的任务交给使用者做
        return organizationDao.queryOrgnizationByOrgID(orgID);
    }
    @Override
    public boolean addOrganization(String orgID, String orgName, String password,int creditLimit, String orgType, int store)throws InterruptedException, ExecutionException
    {
//        首先检查是否存在
//        接着检查输入数据是否合法
        Organization org = organizationDao.queryOrgnizationByOrgID(orgID);
        if(org==null)
        {
//            检查数据是否合法
            if(creditLimit<0){
                System.out.println("额度小于零，不合法");
                System.out.println("额度小于零，不合法");
                return false;
            }
            else if("B".equals(orgType)||"C".equals(orgType)||"I".equals(orgType)||"L".equals(orgType)||"U".equals(orgType))
            {
                Organization newOrg = new Organization();
                newOrg.setOrgID(orgID);
                newOrg.setOrgName(orgName);
                newOrg.setCreditLimit(creditLimit);
                long now = System.currentTimeMillis();
                String currentTime = Utils.sdf(now);
                newOrg.setPassword(Utils.getSHA256Str(password));
                newOrg.setCreateTimestamp(currentTime);
                newOrg.setUpdateTimestamp(currentTime);
                newOrg.setOrgType(orgType);
                newOrg.setStore(store);
//                生成密钥对
                try {
                    System.out.println("生成密钥ing");
                    Map<String, Object> keyPair = RSAUtils.genKeyPair();
                    System.out.println(keyConstant.keyPath+File.separator+"private"+File.separator+orgID+".key");
                    File privatekey = new File(keyConstant.keyPath+File.separator+"private"+File.separator+orgID+".key");
                    privatekey.createNewFile();
                    try(OutputStream os = new FileOutputStream(privatekey)){
                        byte[] data = RSAUtils.getPrivateKey(keyPair).getBytes();
                        os.write(data);
                    }catch(Exception e){
                        e.printStackTrace();
                        return false;
                    }
                    File publicKey = new File(keyConstant.keyPath+File.separator+"public"+File.separator+orgID+".key");
                    publicKey.createNewFile();
                    try(OutputStream os = new FileOutputStream(publicKey)){
                        byte[] data = RSAUtils.getPublicKey(keyPair).getBytes();
                        os.write(data);
                    }catch(Exception e){
                        e.printStackTrace();
                        return false;
                    }
                    organizationDao.addOrganization(newOrg);

                    //区块链操作**************
                    user.createUser(orgType,orgName,currentTime).send();
                    wallet.createAct(orgID).send();
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("生成密钥失败");
                    return false;
                }
            }
            else
            {
                System.out.println("机构类型不合法");
                return false;
            }
        }
        else
        {
            System.out.println("机构已存在");
            return false;
        }
    }
    @Override
    public Organization queryOrganizationByOrgName(String orgName)
    {
//        查询机构信息
//        不进行安全检查
//        检查的任务交给使用者做
        return organizationDao.queryOrgnizationByOrgName(orgName);
    }
    @Override
    public String queryOrgIDByOrgName(String orgName)
    {
        return organizationDao.queryOrgIDByOrgName(orgName);
    }
    @Override
    public String queryBankID(String orgID)
    {
        return organizationDao.queryBankID(orgID);
    }
    @Override
    public List<Organization> queryAll()
    {
        return organizationDao.queryAll();
    }
    @Override
    public List<Organization> queryOrgType(String orgType){
        return organizationDao.queryOrgType(orgType);
    }
    @Override
    public boolean setStore(int store,String updateTime, String orgID)throws ExecutionException, InterruptedException{
        if(store<0)
        {
            System.out.println("额度小于零");
            return false;
        }
        Organization org = organizationDao.queryOrgnizationByOrgID(orgID);
        if(org==null)
        {
            System.out.println("机构不存在");
            return false;
        }
        organizationDao.setStore(store,updateTime,orgID);

        //区块链操作**********
        return true;
    }
}

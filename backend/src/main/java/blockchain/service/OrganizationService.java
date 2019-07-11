package blockchain.service;

import blockchain.entity.Organization;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrganizationService {
//    添加机构
    public boolean addOrganization(String orgID, String orgName, String password,int creditLimit, String orgType, int store)throws InterruptedException, ExecutionException;
//    设置用户信用额度
    public boolean setCreditLimit(int amount, String updateTime, String orgID)throws ExecutionException, InterruptedException;
//    查询信用额度
    public int queryCreditLimit(String orgID);
//    回收白条，暂时不懂，没实现
//    public boolean recycleCredit(String CreditID,int amount) throws InterruptedException, ExecutionException;
//    登录判断用
//    -1表示未注册；
//    else
//        2表示机构类型不正确；
//        else
//            1表示密码机构类型正确；
//            0表示机构正确，密码错误；
    public int checkPasswordByOrgID(String password, String orgID);
//    查询机构信息
    public Organization queryOrganizationByOrgID(String orgID);
    public Organization queryOrganizationByOrgName(String orgName);
    public String queryOrgIDByOrgName(String orgName);
//    查询bankID
    public String queryBankID(String orgID);
    public List<Organization> queryAll();
    //根据企业类型名称返回所有类型企业
    public List<Organization> queryOrgType(String orgType);

    boolean setStore(int store, String updateTime, String orgID)throws ExecutionException, InterruptedException;
}

package blockchain.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import blockchain.entity.Organization;

@Mapper
public interface OrganizationDao {
    public void addOrganization(@Param(value = "organization")Organization organization);     //新增机构
    public void updateOrganization(@Param(value="organization")Organization organization);    //更新机构
    public Integer queryCreditLimitByOrgID(@Param(value = "orgID")String orgID);                //查找白条额度
    public void setCreditLimitByOrgID(@Param(value = "creditLimit")int creditLimit,@Param(value = "updateTimestamp")String updateTimestamp,@Param(value = "orgID")String orgID); //更新白条额度
    public Organization queryOrgnizationByOrgID(@Param(value = "orgID")String orgID);         //根据id查找机构信息
    public Organization queryOrgnizationByOrgName(@Param(value = "orgName")String orgName);         //根据名称查找机构信息
    public String queryOrgIDByOrgName(@Param(value = "orgName")String orgName);
    public String checkType(@Param(value = "orgID")String orgID);                             //查找机构类型
    public String checkPassword(@Param(value = "orgID")String orgID);                         //查找密码
    public String queryBankID(@Param(value = "orgID")String orgID);                             //查找授信银行ID
    public List<Organization> queryAll();

    public List<Organization> queryOrgType(@Param(value = "orgType")String orgType);
    public void setStore(@Param(value = "store")int store,@Param(value = "updateTimestamp")String updateTimestamp,@Param(value = "orgID")String orgID);
}

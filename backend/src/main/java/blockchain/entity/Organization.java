package blockchain.entity;
import lombok.Data;

import java.util.Map;

//机构实体类，通过lombok.Data添加setter和getter
@Data
public class Organization {
    private String orgID;           //机构ID
    private String orgName;         //机构名称
    private String password;        //机构密码
    private String orgType;         //机构类型，B—银行，C—核心，I—保险，L—物流，U—上游
    private String bankID;          //授信银行
    private int creditLimit;        //可用授信额度
    private String createTimestamp; //创建时间
    private String updateTimestamp; //更新时间
    private int store;              //库存
//    private int tokenLimit;         //可用token
//    public Organization(){
//
//    }
//    public Organization(String orgName,String password,String orgType){
//        this.orgName = orgName;
//        this.password = password;
//        this.orgType = orgType;
//    }

}

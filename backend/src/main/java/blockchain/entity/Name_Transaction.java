package blockchain.entity;

import lombok.Data;
//合同实体类，通过lombok.Data进行setter和getter自动生成

@Data
public class Name_Transaction {
    private String transID;             //合同ID-+
    private String tranHash;            //合同哈希-+
    private String initiatorID;         //发起人ID-+
    private String recipientID;         //接收人ID-+
    private int amount;                  //合同金额-+
    private String transType;           //合同类型，order—订单合同，dist—物流合同，warehouse—入库合同，insurance—保险合同，credit—授信
    private String transStatus;         //合同状态，wait—待签，signed—已签，cancel—作废-+
    private String createTimestamp;     //创建时间-+
    private String updateTimestamp;     //更新时间-+
    private String information;         //文字信息
    private String initiatorSignature;   //发起人签名-+
    private String recipientSignature;   //接收人签名

    private String initiatorOrgName;    //发起人name
    private String recipientOrgName;    //接收人name
}
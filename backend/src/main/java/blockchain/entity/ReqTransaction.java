package blockchain.entity;

import lombok.Data;

@Data
public class ReqTransaction {
    private String initiatorName;
    private String recipientName;
    private String transType; //合同类型，order—订单合同，dist—物流合同，warehouse—入库合同，insurance—保险合同，credit—授信
    private int amount;
    private String contractName;
    private String information;//表示运输和入库合同中的额外文字信息

    public ReqTransaction(String initiatorName, String recipientName, String transType, int amount, String contractName, String information) {
        this.initiatorName = initiatorName;
        this.recipientName = recipientName;
        this.transType = transType;
        this.amount = amount;
        this.contractName = contractName;
        this.information = information;
    }
}

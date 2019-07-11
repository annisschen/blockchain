package blockchain.entity;

import lombok.Data;

@Data
public class Token {
    private String tokenID;//tokenID
    private String initOrg;//发起机构名称
    private String recOrg;//接受机构名称
    private String bankID;//银行机构ID
    private int amount;//金额
    private int paid;//已还,不会用到
    private String tokenStatus;//token状态：order表示订单合同交易   dist-运输合同  insurance-保险  credit表示授信  wait表示申请承兑待确认  OK表示同意承兑  reject表示拒绝承兑
    private String transTimestamp;//创建时间
    private String updateTimestamp;//更新时间
}

CREATE Database if NOT EXISTS supply CHARACTER SET UTF8;
use supply;
create table if not exists `Organization`(
  `orgID` varchar(64) NOT NULL COMMENT '机构ID',
  `orgName` varchar(100) NOT NULL unique COMMENT '机构名称',
  `orgType` varchar(4) NOT NULL COMMENT '机构类型',
  `password` varchar(300) NOT NULL COMMENT '登录密码',
  `bankID` varchar(64) NOT NULL COMMENT '银行ID',
  `creditLimit` int NOT NULL COMMENT '白条额度',
  `createTimestamp` varchar(32) NOT NULL COMMENT '创建时间',
  `updateTimestamp` varchar(32) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`orgID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构';

Create Table If Not Exists `Transaction`(
  `transID` varchar(64) NOT NULL COMMENT '合同ID',
  `initiatorID` varchar(64) NOT NULL COMMENT '销售方机构',
  `recipientID` varchar(64) NOT NULL COMMENT '购买方机构',
  `transType` varchar(20) NOT NULL COMMENT '交易类型',
  `amount` int NOT NULL COMMENT '白条金额',
  `tranHash` varchar(300) NOT NULL COMMENT '合同hash',
  `transStatus` varchar(6) NOT NULL COMMENT '合同状态',
  `information` varchar(300) NOT NULL COMMENT '文字信息',
  `createTimestamp` varchar(32) NOT NULL COMMENT '交易时间',
  `updateTimestamp` varchar(32) NOT NULL COMMENT '更新时间',
  `initiatorSignature` varchar(256) NOT NULL COMMENT '发起人签名',
  `recipientSignature` varchar(256) NOT NULL COMMENT '接收人签名',
  PRIMARY KEY (`transID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同';

Create Table If Not Exists `Token`(
  `tokenID` varchar(64) NOT NULL COMMENT 'tokenID',
  `fromOrg` varchar(64) NOT NULL COMMENT '支付方机构',
  `recOrg` varchar(64) NOT NULL COMMENT '接受方机构',
  `bank` varchar(64) NOT NULL COMMENT '发行机构',
  `tokenStatus` varchar(4) NOT NULL COMMENT '交易状态，N未完成，C已完成',
  `amount` int NOT NULL COMMENT '支付金额',
  `paid` int NOT NULL COMMENT '已支付金额',
  `createTimestamp` varchar(32) NOT NULL COMMENT '交易时间',
  `updateTimestamp` varchar(32) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`tokenID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='token';
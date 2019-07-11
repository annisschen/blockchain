package blockchain.dao;

import blockchain.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TokenDao {
    public void addToken(@Param(value = "token")Token token);//新增token记录
    public void updateTokenStatus(@Param(value = "tokenID")String tokenID, @Param(value = "tokenStatus")String tokenStatus, @Param(value = "updateTimestamp") String updateTimestamp);//更新token状态
    public void updateToken(@Param(value = "token")Token token);//不会更新创建时间
    public Token queryTokenByTokenID(@Param(value = "tokenID")String tokenID);//通过Token号查询token
    public List<Token> queryAllTokens();//查询所有token
    public List<Token> queryTokenByFromOrg(@Param(value = "initOrg")String initOrg);//查询fromOrg发起的所有token
    public List<Token> queryTokenByRecOrg(@Param(value = "recOrg")String recOrg);//查询fromOrg接受的所有token
    public int queryTotalAmount(@Param(value = "orgName")String orgName);//查询fromOrg的总借款
    public int queryTotalPaid(@Param(value = "orgName")String orgName);//查询fromOrg的总还款
}

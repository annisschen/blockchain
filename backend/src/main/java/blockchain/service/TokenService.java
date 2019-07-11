package blockchain.service;

import blockchain.entity.Token;

import java.util.List;

public interface TokenService {

//    添加
    public boolean addToken(Token token);
//    查询
    public Token queryTokenByTokenID(String tokenID);

    public List<Token> queryTokenByInitOrg(String fromOrg);

    public List<Token> queryTokenByRecOrg(String recOrg);

    public List<Token> queryAllTokens();

    //查询负债
    public int queryDebt(String orgName);
//    更新
    public boolean updateToken(Token token);

    public boolean updateTokenStatus(String tokenID,String tokenStatus);
}

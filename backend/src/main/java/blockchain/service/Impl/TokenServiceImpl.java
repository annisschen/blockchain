package blockchain.service.Impl;

import blockchain.dao.OrganizationDao;
import blockchain.dao.TokenDao;
import blockchain.entity.Organization;
import blockchain.entity.Token;
import blockchain.service.TokenService;
import blockchain.utils.Utils;
import blockchain.solidity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private Wallet wallet;
    @Override
    public boolean addToken(Token token)
    {
        //判断是否存在机构
        Organization fromOrg = organizationDao.queryOrgnizationByOrgName(token.getInitOrg());
        Organization recOrg = organizationDao.queryOrgnizationByOrgName(token.getRecOrg());
        Organization bank = organizationDao.queryOrgnizationByOrgID(token.getBankID());
        if(fromOrg==null)
        {
            System.out.println("发起机构不存在");
            return false;
        }
        if(recOrg==null)
        {
            System.out.println("接受机构不存在");
            return false;
        }
        if(bank==null)
        {
            System.out.println("银行不存在");
            return false;
        }
        //信用数值是否合法
        if(token.getAmount()<0)
        {
            System.out.println("信用小于0");
            return false;
        }
//        是否已经存在了
        Token istoken = tokenDao.queryTokenByTokenID(token.getTokenID());
        if(istoken!=null)
        {
            System.out.println("token记录已经存在");
            return false;
        }
//        //token的状态是否合法：amount==paid,Status = C, amount>paid, Status = U
//        if(Math.abs(token.getAmount()-token.getPaid())<1e-6)
//        {
//            if(!("C".equals(token.getTokenStatus())))
//            {
//                System.out.println("token状态错误，应为C");
//                return false;
//            }
//        }
//        else if(token.getPaid()< token.getAmount())
//        {
//            if(!("U".equals(token.getTokenStatus())))
//            {
//                System.out.println("token状态错误，应为U");
//                return false;
//            }
//        }
//        else
//        {
//            System.out.println("还太多钱了！");
//            return false;
//        }
        tokenDao.addToken(token);
        //区块链操作****************
        try {
            wallet.createToken(token.getTokenID(), token.getInitOrg(), token.getBankID(), BigInteger.valueOf(token.getAmount()), token.getTransTimestamp()).send();
        }
        catch (Exception e){ e.printStackTrace(); }
        return true;
    }

    @Override
    public Token queryTokenByTokenID(String tokenID)
    {
        Token token = tokenDao.queryTokenByTokenID(tokenID);
        int amount = 0;
        try{
            amount = wallet.queryTokenBal(tokenID).send().intValue();
        }
        catch (Exception e){ e.printStackTrace(); }
        token.setAmount(amount);
        return tokenDao.queryTokenByTokenID(tokenID);
    }

    @Override
    public List<Token> queryTokenByInitOrg(String fromOrg)
    {
        return tokenDao.queryTokenByFromOrg(fromOrg);
    }

    @Override
    public  List<Token> queryTokenByRecOrg(String recOrg)
    {
        return tokenDao.queryTokenByRecOrg(recOrg);
    }

    @Override
    public  List<Token> queryAllTokens()
    {
        return tokenDao.queryAllTokens();
    }

    @Override
    public  boolean updateToken(Token token)
    {
        //判断每个位置是否合法
        //判断是否存在机构
        Organization initOrg = organizationDao.queryOrgnizationByOrgName(token.getInitOrg());
        Organization recOrg = organizationDao.queryOrgnizationByOrgName(token.getRecOrg());
        Organization bank = organizationDao.queryOrgnizationByOrgID(token.getBankID());
        if(initOrg==null)
        {
            System.out.println("发起机构不存在");
            return false;
        }
        if(recOrg==null)
        {
            System.out.println("接受机构不存在");
            return false;
        }
        if(bank==null)
        {
            System.out.println("银行不存在");
            return false;
        }
        //信用数值是否合法
        if(token.getAmount()<0) {
            System.out.println("信用小于0");
            return false;
        }
//        是否已经存在了
        Token istoken = tokenDao.queryTokenByTokenID(token.getTokenID());
        if(istoken==null)
        {
            System.out.println("token记录不存在");
            return false;
        }
        //token的状态是否合法：amount==paid,Status = C, amount>paid, Status = U
//        if(Math.abs(token.getAmount()-token.getPaid())<1e-6)
//        {
//            if(!("C".equals(token.getTokenStatus())))
//            {
//                System.out.println("token状态错误，应为C");
//                return false;
//            }
//        }
//        else if(token.getPaid()< token.getAmount())
//        {
//            if(!("U".equals(token.getTokenStatus())))
//            {
//                System.out.println("token状态错误，应为U");
//                return false;
//            }
//        }
//        else
//        {
//            System.out.println("还太多钱了！");
//            return false;
//        }
        tokenDao.updateToken(token);
        //区块链操作****************
        try {
            wallet.updateToken(token.getTokenID(), BigInteger.valueOf(token.getAmount()), token.getUpdateTimestamp()).send();
        }
        catch (Exception e){ e.printStackTrace(); }
        return true;
    }

    @Override
    public boolean updateTokenStatus(String tokenID,String tokenStatus)
    {
        Token token = tokenDao.queryTokenByTokenID(tokenID);
        if(token==null)
        {
            System.out.println("token不存在");
            return false;
        }
        long now = System.currentTimeMillis();
        String time = Utils.sdf(now);
        tokenDao.updateTokenStatus(tokenID,tokenStatus,time);
        //区块链操作****************
        return true;
    }

    @Override
    public int queryDebt(String orgName)
    {
        List<Token> tokens = tokenDao.queryTokenByFromOrg(orgName);
        if(tokens==null)
        {
            return 0;
        }
        int amount = tokenDao.queryTotalAmount(orgName);
        int paid = tokenDao.queryTotalPaid(orgName);
        return amount-paid;
    }
}

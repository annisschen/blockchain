package blockchain;

import blockchain.constant.GasConstants;
import blockchain.solidity.*;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.fisco.bcos.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;

public class web3jdkTest extends BaseTest{
    @Autowired
    Web3j web3j;
    @Autowired
    Credentials credentials;
    @Autowired
    User user;
    @Autowired
    Wallet wallet;

    @Test
    public void SolidityTest() throws Exception{
        System.out.println(web3j.getBlockNumber());
        user.createUser("bank","c","20190710").send();
        wallet.createAct("100").send();
        wallet.createToken("123","c","bank",BigInteger.valueOf(1000),"201907").send();
        int amount = wallet.queryTokenBal("123").send().intValue();
        System.out.println(amount);
    }

    @Test
    public void deployAndCallContract()
    {
        try
        {
            HelloWorld helloWorld =
                    HelloWorld.deploy(web3j,
                            credentials,
                            new StaticGasProvider(
                                    GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                            .send();
            if(helloWorld!=null)
            {
                System.out.println("addresss: "+helloWorld.getContractAddress());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Test
    public  void getBlockNumber() throws IOException
    {
        BigInteger blockNumber = web3j.getBlockNumber().send().getBlockNumber();
        System.out.println(blockNumber);
    }
}

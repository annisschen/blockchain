package blockchain.autoConfig;

import blockchain.constant.GasConstants;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import blockchain.solidity.*;

@Configuration
public class ContractConfig {
    @Autowired
    Web3j web3j;
    @Autowired
    Credentials credentials;

    @Bean
    public Wallet getWallet() throws Exception{
        Wallet wallet = Wallet.deploy(web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE,GasConstants.GAS_LIMIT)).send();
        return wallet;
    }

    @Bean
    public User getUser() throws Exception {
        User user = User.deploy(web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT)).send();
        return user;
    }

    @Bean
    public EnterpriseContract getContract() throws Exception {
        EnterpriseContract enterpriseContract = EnterpriseContract.deploy(web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT)).send();
        return enterpriseContract;
    }
}

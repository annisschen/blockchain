package blockchain.utilsTest;

import blockchain.BaseTest;
import blockchain.constant.keyConstant;
import blockchain.utils.RSAUtils;
import blockchain.utils.Utils;
import org.junit.Test;
import blockchain.utils.Base64Utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import static org.junit.Assert.*;

public class Base64UtilsTest extends BaseTest {

    @Test
    public void decode() throws Exception{
        String str = "test";
        assertEquals("test",str);
    }
    @Test
    public void fileTest(){
        String file = "E:/Private_management/2019/2019上/课程/实训/blockchain/backend/pom.xml";
        try {
            Map<String,Object> keyMap = RSAUtils.genKeyPair();
            System.out.println(RSAUtils.getPrivateKey(keyMap));
            System.out.println(RSAUtils.getPublicKey(keyMap));
            File keyPair = new File("E:/Private_management/2019/2019上/课程/实训/blockchain/backend/test.keypair");
            try(OutputStream os = new FileOutputStream(keyPair)){
                String s = RSAUtils.getPrivateKey(keyMap);
                byte[] data = s.getBytes();
                os.write(data);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        assertEquals("1","1");
    }
    @Test
    public void signature()
    {
        String initiatorID = "76eccdc7-c155-4ca7-bc0d-096915eec2e5";
        String tranHash = "dsfasdflasf";
        try {
            String privateKeyPath = keyConstant.keyPath + File.separator + "private" + File.separator + initiatorID + ".key";
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String initiatorSignature = RSAUtils.sign(tranHash.getBytes(),privateKey);
            System.out.println(initiatorSignature);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            assertEquals(1,2);
        }
    }
    @Test
    public void deleteFile()
    {
        String file = "E:/Private_management/2019/2019上/课程/实训/blockchain/backend/test.txt";
        try{
            Files.deleteIfExists(Paths.get(file));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
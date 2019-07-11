package blockchain.utils;

import blockchain.BaseTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest extends BaseTest {

    @Test
    public void sdf() {
        long now = System.currentTimeMillis();
        String str = Utils.sdf(now);
        assertEquals(str,"test");
    }
    @Test
    public void test()
    {
        System.out.println(System.getProperty("user.dir"));
    }
}
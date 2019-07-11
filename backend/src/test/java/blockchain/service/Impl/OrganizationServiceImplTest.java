package blockchain.service.Impl;

import blockchain.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class OrganizationServiceImplTest extends BaseTest {
    @Autowired
    private OrganizationServiceImpl organizationServiceImpl;
    @Test
    public void test()
    {
        String res = organizationServiceImpl.queryOrgIDByOrgName("冇台集团");
        assertEquals("76eccdc7-c155-4ca7-bc0d-096915eec2e5",res);
    }


}
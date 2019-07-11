package blockchain.service.Impl;

import blockchain.dao.OrganizationDao;
import blockchain.entity.Organization;
import blockchain.service.UserSessionService;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSessionServiceImpl implements UserSessionService {
    @Autowired
    private OrganizationDao UserMapper;

    @Override
    public Organization find_session(String name){
        return UserMapper.queryOrgnizationByOrgName(name);
    }
}

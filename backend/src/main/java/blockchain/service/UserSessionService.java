package blockchain.service;

import blockchain.entity.Organization;

public interface UserSessionService<Organization> {
    Organization find_session(String name);
}

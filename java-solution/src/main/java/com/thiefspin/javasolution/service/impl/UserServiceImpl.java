package com.thiefspin.javasolution.service.impl;

import com.thiefspin.javasolution.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    /**
     * We don't have a real user service so just return true.
     *
     * @param msisdn
     * @return
     */

    @Override
    public boolean exists(String msisdn) {
        return true;
    }
}

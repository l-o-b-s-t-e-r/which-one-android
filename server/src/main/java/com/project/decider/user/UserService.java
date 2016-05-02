package com.project.decider.user;

import com.project.decider.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lobster on 30.04.16.
 */

@Service("userService")
@Transactional
public class UserService{

    @Autowired
    private Dao dao;

    public User createPlayer(User user) {
       return dao.save(user);
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

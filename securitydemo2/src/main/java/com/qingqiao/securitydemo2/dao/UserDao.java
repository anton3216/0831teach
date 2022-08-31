package com.qingqiao.securitydemo2.dao;

import com.qingqiao.securitydemo2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long>{

    User findUserByUsername(String username);
}

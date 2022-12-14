package com.qingqiao.securitydemo2;

import com.qingqiao.securitydemo2.dao.UserDao;
import com.qingqiao.securitydemo2.entity.Role;
import com.qingqiao.securitydemo2.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SpringBootTest
class Securitydemo2ApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    void contextLoads() {

        User u1 = new User();
        u1.setUsername("dahuang");
        u1.setPassword("123");
        u1.setAccountNonExpired(true);
        u1.setAccountNonLocked(true);
        u1.setCredentialsNonExpired(true);
        u1.setEnabled(true);
        List<Role> rs1 = new ArrayList<>();

        Role r1 = new Role();
        r1.setName("ROLE_admin");
        r1.setNameZh("管理员");
        rs1.add(r1);
        u1.setRoles(rs1);
        userDao.save(u1);

        User u2 = new User();
        u2.setUsername("qingqiao");
        u2.setPassword("123");
        u2.setAccountNonExpired(true);
        u2.setAccountNonLocked(true);
        u2.setCredentialsNonExpired(true);
        u2.setEnabled(true);
        List<Role> rs2 = new ArrayList<>();
        Role r2 = new Role();
        r2.setName("ROLE_user");
        r2.setNameZh("普通用户");
        rs2.add(r2);
        u2.setRoles(rs2);
        userDao.save(u2);
    }
    @Test
    void test1() throws UnsupportedEncodingException {
        // dahuang:321321321321321:kjshdfkjshkfjsdkfskdf
        String str = new String(Base64.getDecoder().decode("ZGFodWFuZzoxNjYzMTE4ODMwMTY5OmJkNmI3MWM1ZTUxYTA5NmMyNTBkMmYxNDUyZGQ5YjJm".getBytes("utf-8")));
        System.out.println(str);
    }

}

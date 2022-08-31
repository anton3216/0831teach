package com.qingqiao.securitydemo2.config;

import com.qingqiao.securitydemo2.dao.UserDao;
import com.qingqiao.securitydemo2.provider.MyAuthenticationProvider;
import com.qingqiao.securitydemo2.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    DataSource dataSource;

    /*
    * 自定义的认证生产者,主要提供了验证码的验证功能
    * 继承了DaoAuthenticationProvider类
    * 在其中也调用了父类的additionalAuthenticationChecks方法,用于账号和密码的验证
    * */
    @Bean
    MyAuthenticationProvider myAuthenticationProvider(){
        MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider();
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(userService);
        return myAuthenticationProvider;
    }

    /*
        将我们自定义的认证生产者注册到ProviderManager
     */
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager providerManager = new ProviderManager(myAuthenticationProvider());
        return providerManager;
    }

    @Bean
    JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return roleHierarchy;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        // 官方推荐密码加密方式 可以设置
        // 通过设置 strength 的值(4-31,默认10) 值
        // 改变密码的迭代次数
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()// 所有授权请求
                .antMatchers("/admin/**").hasRole("admin") // 设置权限
                //.antMatchers("/user/**").hasAnyRole("admin","user") // 设置多个权限
                .antMatchers("/user/**").hasRole("user") // 设置权限
                .antMatchers("/administrator").fullyAuthenticated()// 设置administrator必须要输入用户名和密码
                .antMatchers("/rememberme").rememberMe()// 设置rememberme必须是自动登录的才能访问
                .antMatchers("/vc.jpg").permitAll()// 放行验证码图片
                .anyRequest().authenticated() // 任何请求.都需要认证
                .and()
                .formLogin()// 表单登录
                .loginPage("/login.html")// 配置自定义登录页面
                .loginProcessingUrl("/doLogin")// 配置登录接口
                .permitAll() // 放行
//                .defaultSuccessUrl("")
//                .successForwardUrl("")
//                .successHandler()
                .and()
                .rememberMe()// 自动登录
                .rememberMeParameter("rem")// 改remember-me为rem
                .key("123456")// 设置签名盐
                .tokenRepository(jdbcTokenRepository())// 持久化令牌实现类
                .and()
                .csrf().disable(); // 关闭防御csrf攻击
    }
}

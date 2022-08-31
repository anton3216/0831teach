package com.qingqiao.securitydemo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 角色继承
    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return roleHierarchy;
    }

    // 内存中设置 用户名 密码 角色
    @Bean
    protected UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("dahuang").password("123").roles("admin").build());
        manager.createUser(User.withUsername("qingqiso").password("123").roles("user").build());
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        // 官方推荐密码加密方式 可以设置
        // 通过设置 strength 的值(4-31,默认10) 值
        // 改变密码的迭代次数
        return new BCryptPasswordEncoder();
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
                .anyRequest().authenticated() // 任何请求.都需要认证
                .and()
                .formLogin()// 表单登录
                .loginPage("/login.html")// 配置自定义登录页面
                .loginProcessingUrl("/doLogin")// 配置登录接口
                .usernameParameter("name") // 配置用户名参数名称
                .passwordParameter("pwd") // 配置密码参数名称
                .defaultSuccessUrl("/index",true) // 登陆成功后跳转地址
                .failureUrl("/error111.html") // 登录失败后跳转地址
                .permitAll() // 放行
                .and()
                .csrf().disable(); // 关闭防御csrf攻击
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("dahuang")
                .password("$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq")
                .roles("admin")
                .and()
                .withUser("qingqiao")
                .password("$2a$10$eUHbAOMq4bpxTvOVz33LIehLe3fu6NwqC9tdOcxJXEhyZ4simqXTC")
                .roles("user");
    }
}

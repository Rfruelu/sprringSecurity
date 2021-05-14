package com.lujia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * <h3>Title: ${title}</h3>
 * <p>Description: </p>
 * 资源服务配置
 * @author : lujia
 * @date : 2021-05-12
 **/
@Configuration
@EnableResourceServer
public class OAuthSourceConfig extends ResourceServerConfigurerAdapter {


    /**
     * token 验证方式
     * @return
     */
    @Bean
    public ResourceServerTokenServices resourceTokenServices(){
        RemoteTokenServices remoteTokenServices=new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9001/oauth/check_token");
        remoteTokenServices.setClientId("bobo_one");
        remoteTokenServices.setClientSecret("123");
        return  remoteTokenServices;
    }
    /**
     * 指定当前资源的id和存储方案
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("product_api").// 资源id
                tokenServices(resourceTokenServices()).  //认证服务地址
                stateless(true);// 无状态的
    }

    /**
     * spring security peizhi
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //指定不同请求方式访问资源所需要的权限，一般查询是read，其余是write。
                .antMatchers("/**").access("#oauth2.hasScope('read')")
                .anyRequest().permitAll()//其他请求都放过
                // 放开跨域，不小session管理
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().addHeaderWriter((request, response) -> {
            response.addHeader("Access-Control-Allow-Origin", "*");//允许跨域
            if (request.getMethod().equals("OPTIONS")) {//如果是跨域的预检请求，则原封不动向下传达请求头信息
                response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            }
        });
    }

}

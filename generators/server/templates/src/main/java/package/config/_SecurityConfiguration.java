package <%=packageName%>.config;

<%_ if (authenticationType == 'session' || authenticationType == 'jwt') { _%>
import <%=packageName%>.security.*;
<%_ } _%>
<%_ if (authenticationType == 'jwt') { _%>
import <%=packageName%>.security.jwt.*;
<%_ } _%>
<%_ if (authenticationType == 'session') { _%>
import <%=packageName%>.config.JHipsterProperties;
<%_ } _%>

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;<% if (authenticationType == 'oauth2') { %>
import org.springframework.security.authentication.AuthenticationManager;<% } %>
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;<% if (authenticationType == 'jwt' || authenticationType == 'oauth2') { %>
import org.springframework.security.config.http.SessionCreationPolicy;<% } %><% if (clusteredHttpSession == 'hazelcast') { %>
import org.springframework.security.core.session.SessionRegistry;<% } %>
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
<%_ if (authenticationType == 'session') { _%>
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
<%_ } _%>

<%_ if(enableLdapSupport) { _%>
import <%=packageName%>.security.SecurityProperties;
import <%=packageName%>.security.SecurityProperties.Ldap;
import <%=packageName%>.security.SecurityProperties.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import <%=packageName%>.web.rest.errors.CustomParameterizedException;
<%_ } _%>


import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)<% if(enableLdapSupport) { %>
@EnableConfigurationProperties(SecurityProperties.class)<% } %>
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {<% if (authenticationType == 'session') { %>

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;<% } if (authenticationType == 'session' || authenticationType == 'jwt') { %>

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;<% } %>

    @Inject
    private UserDetailsService userDetailsService;<% if (authenticationType == 'session') { %>

    @Inject
    private RememberMeServices rememberMeServices;<% } %><% if (clusteredHttpSession == 'hazelcast') { %>

    @Inject
    private SessionRegistry sessionRegistry;<% } %><% if (authenticationType == 'jwt') { %>

    @Inject
    private TokenProvider tokenProvider;<% } %><% if (enableLdapSupport) { %>

    @Inject
  	private SecurityProperties securityProperties;<% } %>


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) {

      <%_ if(!enableLdapSupport) { _%>
        try {
            auth
                .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
      <%_ } else { _%>
        try {
    			if (securityProperties.getMode().equals(Mode.BD)) {
    				LOG.debug("Authentication Mode: BD");
    				auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    			} else if (securityProperties.getMode().equals(Mode.LDAP)){
    				LOG.debug("Authentication Mode: LDAP");
    				Ldap ldapProperties = securityProperties.getLdap();
    				auth.ldapAuthentication().groupRoleAttribute("cn").groupSearchBase(ldapProperties.getGroupSearchBase())
    						.groupSearchFilter(ldapProperties.getGroupSearchFilter()).userSearchFilter(ldapProperties.getUserSearchFilter()).contextSource()
    						.url(ldapProperties.getUrl());
    			}else{
    				throw new CustomParameterizedException("Unsupported Authentication Mode");
    			}

    		} catch (Exception e) {
    			throw new BeanInitializationException("Security configuration failed", e);
    		}
      <%_ } _%>
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")<% if (authenticationType == 'oauth2') { %>
            .antMatchers("/api/register")
            .antMatchers("/api/activate")
            .antMatchers("/api/account/reset_password/init")
            .antMatchers("/api/account/reset_password/finish")<% } %>
            .antMatchers("/test/**")<% if (devDatabaseType != 'h2Disk' && devDatabaseType != 'h2Memory') { %>;<% } else { %>
            .antMatchers("/h2-console/**");<% } %>
    }<% if (authenticationType == 'session' || authenticationType == 'jwt') { %>

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http<% if (clusteredHttpSession == 'hazelcast') { %>
            .sessionManagement()
            .maximumSessions(32) // maximum number of concurrent sessions for one user
            .sessionRegistry(sessionRegistry)
            .and().and()<% } %><% if (authenticationType == 'session') { %>
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())<% if (websocket == 'spring-websocket' && authenticationType != 'session') { %>
            .ignoringAntMatchers("/websocket/**")<% } %>
        .and()<% } %>
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)<% if (authenticationType == 'session') { %>
        .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .rememberMeParameter("remember-me")
            .key(jHipsterProperties.getSecurity().getRememberMe().getKey())
        .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .successHandler(ajaxAuthenticationSuccessHandler)
            .failureHandler(ajaxAuthenticationFailureHandler)
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler)<% if (clusteredHttpSession == 'hazelcast') { %>
            .deleteCookies("hazelcast.sessionId")<% } %>
            .permitAll()<% } %>
        .and()<% if (authenticationType == 'jwt') { %>
            .csrf()
            .disable()<% } %>
            .headers()
            .frameOptions()
            .disable()
        .and()<% if (authenticationType == 'jwt') { %>
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()<% } %>
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()<% if (websocket == 'spring-websocket') { %>
            .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/websocket/**").permitAll()<% } %>
            <%_ if (serviceDiscoveryType == 'consul') { _%>
            .antMatchers("/management/health").permitAll()
            <%_ } _%>
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)<%if (authenticationType != 'jwt') { %>;<% } else { %>
        .and()
            .apply(securityConfigurerAdapter());<% } %>

    }<% } %><% if (authenticationType == 'oauth2') { %>

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().realmName("<%= baseName %>")
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .requestMatchers().antMatchers("/oauth/authorize")
            .and()
                .authorizeRequests()
                .antMatchers("/oauth/authorize").authenticated();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }<% } %><% if (authenticationType == 'jwt') { %>

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }<% } %>

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}

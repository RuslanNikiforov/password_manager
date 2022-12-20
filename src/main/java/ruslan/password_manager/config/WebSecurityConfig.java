package ruslan.password_manager.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import ruslan.password_manager.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private final static Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers( "/*.css", "/resources/**", "/signUp" , "/").
                permitAll().antMatchers("/passwords**").hasAuthority("USER").
                antMatchers("/admin**").hasAuthority("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/login").
                permitAll().defaultSuccessUrl("/successful_Login").failureUrl("/failure-login").
                and().logout().logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID");
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public static StandardPBEStringEncryptor stringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("secret");
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }


    public static String generateSecretKey() {
        return KeyGenerators.string().generateKey();
    }

    public static StringBuilder generateAuthToken() {
        StringBuilder keyStorage = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            keyStorage.append(generateSecretKey());
        }
        return keyStorage;
    }

    @Bean
    public static String getAuthToken() {
        if(generateAuthToken().length() != 64) {
            LOG.debug("not a correct key");
        }
        return generateAuthToken().toString();
    }


    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers("/css/**");
    }
}

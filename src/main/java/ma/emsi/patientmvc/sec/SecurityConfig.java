package ma.emsi.patientmvc.sec;

import ma.emsi.patientmvc.sec.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource  dataSource;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        PasswordEncoder passwordEncoder = passwordEncoder();
        /*
        String encPassword=passwordEncoder().encode("1234");
        System.out.println(encPassword);
        auth.inMemoryAuthentication().withUser("user1").password(encPassword).roles("USER");
        auth.inMemoryAuthentication().withUser("user2").password(encPassword).roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password(encPassword).roles("USER", "ADMIN");
        */

        /*
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials, active from users where username=?")
                .authoritiesByUsernameQuery("select username as principal, role as role from users_role where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder);
*/
         auth.userDetailsService(userDetailService);



    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] staticResources  =  {
                "/css/**",
                "/images/**",
                "/fonts/**",
        };
        http.formLogin();
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/delete/**","/edit/**","/formPatients/**","/save/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/webjars/**").permitAll();
        http.authorizeRequests().antMatchers(staticResources).permitAll().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

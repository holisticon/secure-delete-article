package de.holisticon.reference

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("itest")
@Configuration
@EnableWebSecurity
open class ApplicationSecurity : WebSecurityConfigurerAdapter() {

  override fun configure(web: WebSecurity) {
    web.ignoring().antMatchers("/**")
  }

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http
        .authorizeRequests()
        .antMatchers(
            "/**")
        .permitAll()
        .anyRequest().authenticated()
  }

}

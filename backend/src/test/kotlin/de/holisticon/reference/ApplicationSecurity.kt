package de.holisticon.reference

import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.mockito.Mockito.mock
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope
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

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  open fun keycloakRestTemplate(): KeycloakRestTemplate? {
    return KeycloakRestTemplate(mock(KeycloakClientRequestFactory::class.java))
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

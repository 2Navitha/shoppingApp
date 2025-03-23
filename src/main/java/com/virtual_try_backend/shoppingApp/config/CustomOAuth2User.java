package com.virtual_try_backend.shoppingApp.config;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(authorities, attributes, "email");
    }

    public String getFullName() {
        return getAttribute("name");
    }
}

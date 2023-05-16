package cl.rgonzalez.memoria.security;

import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.repo.RSRepoUser;

@Component
public class RSAuthenticatedUser {

    private final RSRepoUser userRepository;
    private final AuthenticationContext authenticationContext;

    public RSAuthenticatedUser(AuthenticationContext authenticationContext, RSRepoUser userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    public Optional<RSUser> get() {
        Optional<UserDetails> opt = authenticationContext.getAuthenticatedUser(UserDetails.class);
        if (opt.isEmpty()) {
            return Optional.empty();
        } else {
            UserDetails ud = opt.get();
            return userRepository.findByUsername(ud.getUsername());
        }
    }

    public void logout() {
        authenticationContext.logout();
    }

}

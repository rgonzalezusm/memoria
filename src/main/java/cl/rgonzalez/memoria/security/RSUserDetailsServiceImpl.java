package cl.rgonzalez.memoria.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.repo.RSRepoUser;

@Service
public class RSUserDetailsServiceImpl implements UserDetailsService {

    private final RSRepoUser userRepository;

    public RSUserDetailsServiceImpl(RSRepoUser userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RSEntityUser> opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            RSEntityUser user = opt.get();
            return new User(user.getUsername(), user.getHashedPassword(), getAuthorities(user));
        }
    }

    private static List<GrantedAuthority> getAuthorities(RSEntityUser user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

    }

}

package cl.rgonzalez.memoria.core.service;

import java.util.List;
import java.util.Optional;

import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.repo.RSRepoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RSSrvUser {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RSRepoUser repoUser;

    public RSSrvUser(RSRepoUser repository) {
        this.repoUser = repository;
    }

    public Optional<RSEntityUser> get(Long id) {
        return repoUser.findById(id);
    }

    public RSEntityUser create(RSEntityUser user) {
        user.setHashedPassword(passwordEncoder.encode(user.getUsername()));
        return repoUser.save(user);
    }

    public RSEntityUser save(RSEntityUser user) {
        return repoUser.save(user);
    }

    public void delete(RSEntityUser user) {
        repoUser.delete(user);
    }

    public Page<RSEntityUser> list(Pageable pageable) {
        return repoUser.findAll(pageable);
    }

    public Page<RSEntityUser> list(Pageable pageable, Specification<RSEntityUser> filter) {
        return repoUser.findAll(filter, pageable);
    }

    public int count() {
        return (int) repoUser.count();
    }

    public List<RSEntityUser> findAll() {
        return repoUser.findAll();
    }

    public Optional<RSEntityUser> findByUsername(String username) {
        return repoUser.findByUsername(username);
    }
}

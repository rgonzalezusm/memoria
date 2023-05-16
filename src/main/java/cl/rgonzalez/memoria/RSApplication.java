package cl.rgonzalez.memoria;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import cl.rgonzalez.memoria.core.RSRole;
import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSOptions;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.repo.RSRepoCategory;
import cl.rgonzalez.memoria.core.repo.RSRepoOptions;
import cl.rgonzalez.memoria.core.repo.RSRepoUser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@Push
@PWA(name = "Reserva de Salas", shortName = "Reserva")
@SpringBootApplication
@Theme(value = "memoria")
@Slf4j
public class RSApplication implements AppShellConfigurator {

    @Autowired
    RSRepoUser repoUser;
    @Autowired
    RSRepoCategory repoCat;
    @Autowired
    RSRepoOptions repoOptions;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(RSApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup01() {
        if (repoUser.count() != 0L) {
            return;
        }

        log.info("Primer inicio del sistema");

        RSUser user = new RSUser();
        user.setName("admin");
        user.setUsername("admin");
        user.setHashedPassword(passwordEncoder.encode("admin"));
        user.setRoles(Arrays.stream(new RSRole[]{RSRole.ADMIN, RSRole.USER}).collect(Collectors.toSet()));
        repoUser.save(user);

        user = new RSUser();
        user.setName("user");
        user.setUsername("user");
        user.setHashedPassword(passwordEncoder.encode("user"));
        user.setRoles(Arrays.stream(new RSRole[]{RSRole.USER}).collect(Collectors.toSet()));
        repoUser.save(user);

        List<RSCategory> cats = Arrays.asList(
                RSCategory.build("Abarrotes"),
                RSCategory.build("Bebidas"),
                RSCategory.build("Licores"),
                RSCategory.build("Dulces"),
                RSCategory.build("Cigarros"),
                RSCategory.build("General")
        );
        repoCat.saveAll(cats);

        RSOptions opt = new RSOptions();
        opt.setZone("America/Santiago");
        repoOptions.save(opt);
    }

//    @Bean
//    SqlDataSourceScriptDatabaseInitializer dsScriptDbInitializer(DataSource ds, SqlInitializationProperties pr, UserRepository repoUser) {
//        // This bean ensures the database is only initialized when empty
//        return new SqlDataSourceScriptDatabaseInitializer(ds, pr) {
//            @Override
//            public boolean initializeDatabase() {
//                if (repoUser.count() == 0L) {
//                    return super.initializeDatabase();
//                }
//                return false;
//            }
//        };
//    }
}

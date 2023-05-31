package cl.rgonzalez.memoria;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.repo.RSRepoRoom;
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
import cl.rgonzalez.memoria.core.entity.RSEntityOptions;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.repo.RSRepoOptions;
import cl.rgonzalez.memoria.core.repo.RSRepoUser;

import java.util.ArrayList;
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
    RSRepoOptions repoOptions;
    @Autowired
    RSRepoRoom repoRoom;
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

        RSEntityUser user = new RSEntityUser();
        user.setName("admin");
        user.setUsername("admin");
        user.setHashedPassword(passwordEncoder.encode("admin"));
        user.setRoles(Arrays.stream(new RSRole[]{RSRole.ADMIN, RSRole.USER}).collect(Collectors.toSet()));
        repoUser.save(user);

        user = new RSEntityUser();
        user.setName("user");
        user.setUsername("user");
        user.setHashedPassword(passwordEncoder.encode("user"));
        user.setRoles(Arrays.stream(new RSRole[]{RSRole.USER}).collect(Collectors.toSet()));
        repoUser.save(user);

        List<RSEntityRoom> rooms = new ArrayList<>();
        rooms.add(RSEntityRoom.build(1, "Sala 1", 30, "HP Prodesk 400 g3 SFF - CPU i5@3,2 GHz - GPU nVidia GeForce GT730 2GB (gráfica) con Proyector"));
        rooms.add(RSEntityRoom.build(2, "Sala 2", 30, "Lenovo Thinkcentre M73 - CPU i5@3,2 GHz - GPU nVidia Quadro K620 2 GB (gráfica)"));
        rooms.add(RSEntityRoom.build(3, "Sala 3", 42, "HP Prodesk 400 G2 Mini - CPU i5@2,5 GHz con Data"));
        rooms.add(RSEntityRoom.build(4, "Sala 4", 42, "HP Prodesk 400 G2 Mini - CPU i5@2,5 GHz con Data"));
        rooms.add(RSEntityRoom.build(5, "Sala 5", 42, "HP Prodesk 400 G1 SFF - CPU i5@3,3 GHz - GPU MSI R5450 1GB (gráfica)"));
        rooms.add(RSEntityRoom.build(6, "Sala 6", 24, "Lenovo Thinkcentre Edge 72z - CPU i5@3,1 GHz."));
        rooms.add(RSEntityRoom.build(7, "Sala 7", 31, "Lenovo Thinkcentre M73 - CPU i5@3,2 GHz - GPU nVidia Quadro K620 2 GB (gráfica) con Data"));
        rooms.add(RSEntityRoom.build(8, "Sala 8", 49, "Lenovo Thinkcentre M73 - CPU i5@3,2 GHz - GPU nVidia Quadro K620 2 GB (gráfica) con Data"));
        rooms.add(RSEntityRoom.build(9, "Sala 9", 48, "Lenovo Thinkcentre M73 - CPU i5@3,2 GHz - GPU nVidia Quadro K620 2 GB (gráfica) con Data"));
        rooms.add(RSEntityRoom.build(10, "Sala 10", 48, "Lenovo Thinkcentre M73 - CPU i5@3,2 GHz - GPU nVidia Quadro K620 2 GB (gráfica) con Data"));
        rooms.add(RSEntityRoom.build(11, "Sala 11", 30, "HP Prodesk 400 G2 Mini - CPU i5@2,5 GHz con Data."));
        repoRoom.saveAll(rooms);

        RSEntityOptions opt = new RSEntityOptions();
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

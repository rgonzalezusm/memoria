package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.exceptions.RSException;
import cl.rgonzalez.memoria.core.entity.RSEntityOptions;
import cl.rgonzalez.memoria.core.repo.RSRepoOptions;
import cl.rgonzalez.memoria.exceptions.RSServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RSSrvOptions {

    @Autowired
    RSRepoOptions repoOptions;

    public String getZone() {
        List<RSEntityOptions> opts = repoOptions.findAll();
        if (opts.size() == 1) {
            RSEntityOptions opt = opts.get(0);
            return opt.getZone();
        } else {
            throw new RSServerException("Mas de una opcion encontradas");
        }
    }

    public void setZone(String zone) throws RSException {
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        if (!availableZoneIds.contains(zone)) {
            throw new RSException("Zona horaria no valida: " + zone);
        }

        List<RSEntityOptions> opts = repoOptions.findAll();
        if (opts.size() == 1) {
            RSEntityOptions opt = opts.get(0);
            opt.setZone(zone);
            repoOptions.save(opt);
        } else {
            throw new RSServerException("Se encontraron mas de una opcion");
        }
    }

}

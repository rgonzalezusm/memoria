package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.RSPayStyle;
import cl.rgonzalez.memoria.core.entity.RSSell;
import cl.rgonzalez.memoria.core.entity.RSSellUnit;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.repo.RSRepoSell;
import cl.rgonzalez.memoria.core.repo.RSRepoSellUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RSSrvSell {

    @Autowired
    RSRepoSell repoSell;
    @Autowired
    RSRepoSellUnit repoUnits;

    public List<RSSell> findAll() {
        return repoSell.findAll();
    }

    public RSSell save(RSSell product) {
        return repoSell.save(product);
    }

    public List<RSSell> saveAll(List<RSSell> products) {
        return repoSell.saveAll(products);
    }

    public void delete(RSSell sell) {
        List<RSSellUnit> units = repoUnits.findAllBySell(sell);
        repoUnits.deleteAll(units);
        repoSell.delete(sell);
    }

    public void createSell(List<RSSellUnit> units, RSPayStyle payStyle, double total, RSUser user) {
        List<RSSellUnit> eunits = repoUnits.saveAll(units);

        RSSell sell = new RSSell();
        sell.setDateTime(LocalDateTime.now());
        sell.setPayStyle(payStyle);
        sell.setTotal((int) total);
        sell.setUnits(eunits);
        sell.setUser(user);
        repoSell.save(sell);
    }

    public List<RSSell> findAllByDate(LocalDate now) {
        return repoSell.findAllBeetween(now.atStartOfDay(), now.plusDays(1).atStartOfDay());
    }
}

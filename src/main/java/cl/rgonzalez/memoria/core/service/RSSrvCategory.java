package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.repo.RSRepoCategory;
import cl.rgonzalez.memoria.core.repo.RSRepoProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RSSrvCategory {

    @Autowired
    RSRepoCategory repoCategory;
    @Autowired
    RSRepoProduct repoProduct;

    public List<RSCategory> findAll() {
        return repoCategory.findAll();
    }

    public RSCategory save(RSCategory cat) {
        return repoCategory.save(cat);
    }

    public List<RSCategory> saveAll(List<RSCategory> cats) {
        return repoCategory.saveAll(cats);
    }

    public void delete(RSCategory cat) {
        Set<RSProduct> prs = repoProduct.findAllByCategory(cat);
        for (RSProduct pr : prs) {
            pr.setCategory(null);
        }
        repoProduct.saveAll(prs);
        repoCategory.delete(cat);
    }

}

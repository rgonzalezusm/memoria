package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.repo.RSRepoProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RSSrvProduct {

    @Autowired
    RSRepoProduct repo;

    public Optional<RSProduct> findById(Long id) {
        return repo.findById(id);
    }

    public List<RSProduct> findAll() {
        return repo.findAll();
    }

    public RSProduct save(RSProduct product) {
        return repo.save(product);
    }

    public List<RSProduct> saveAll(Iterable<RSProduct> products) {
        return repo.saveAll(products);
    }

    public void delete(RSProduct product) {
        repo.delete(product);
    }

    public Optional<RSProduct> findByCode(String code) {
        return repo.findByCode(code);
    }

    public void updateCategories(Set<RSProduct> products, RSCategory cat) {
        List<RSProduct> prs = repo.findAllById(products.stream().map(e -> e.getId()).collect(Collectors.toList()));
        prs.stream().forEach(p -> p.setCategory(cat));
        saveAll(prs);
    }

    public void editAllByIdsCategories(Long[] ids, RSCategory cat) {
        List<RSProduct> prs = repo.findAllById(Arrays.asList(ids));
        prs.stream().forEach(p -> p.setCategory(cat));
        saveAll(prs);
    }

    public void deleteAllByIds(Long[] ids) {
        repo.deleteAllById(Arrays.asList(ids));
    }
}

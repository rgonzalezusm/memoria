package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.repo.RSRepoReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RSSrvSala {

    @Autowired
    RSRepoReservation repoSala;

}

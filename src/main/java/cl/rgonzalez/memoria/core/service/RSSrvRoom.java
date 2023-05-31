package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.repo.RSRepoRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RSSrvRoom {

    @Autowired
    RSRepoRoom repoRoom;

    public List<RSEntityRoom> findAll() {
        return repoRoom.findAll();
    }

    public RSEntityRoom save(RSEntityRoom room) {
        return repoRoom.save(room);
    }

    public void delete(RSEntityRoom room) {
        this.repoRoom.delete(room);
    }


}

package cl.rgonzalez.memoria.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import cl.rgonzalez.memoria.core.entity.RSSell;
import cl.rgonzalez.memoria.core.service.RSSrvSell;

import java.time.LocalDateTime;

@SpringBootTest
public class SB_Sell01 {

    @Autowired
    RSSrvSell srvSell;

    @Test
    public void test() throws Exception {
        System.out.println("test");

        srvSell.save(RSSell.build(LocalDateTime.now()));
        srvSell.findAll().stream().forEach(System.out::println);
    }
}

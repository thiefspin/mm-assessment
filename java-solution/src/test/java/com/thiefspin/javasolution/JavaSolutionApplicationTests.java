package com.thiefspin.javasolution;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.thiefspin.javasolution.models.UssdRequest;
import com.thiefspin.javasolution.models.UssdResponse;
import com.thiefspin.javasolution.service.SessionService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@RunWith(JUnit4.class)
@DataJpaTest
//@AutoConfigureEmbeddedDatabase
class JavaSolutionApplicationTests {

    @Autowired
    private SessionService service;

    EmbeddedPostgres pg = null;

    @BeforeEach
    public void before() {
        try {
            pg = EmbeddedPostgres.builder().setPort(2551).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void after() throws IOException {
        pg.close();
    }

    @Test
    void userFlow() {
        String sessionId = UUID.randomUUID().toString();
        String msisdn = UUID.randomUUID().toString();
        UssdRequest req = new UssdRequest(sessionId, msisdn, null);
        UssdResponse res = service.handleRequest(req).get();
        String expectedMessage = "Welcome to Mama Money! Where would you like to send money today? #placeholder";
        Assert.assertEquals(res.getMessage(), expectedMessage);
    }

}

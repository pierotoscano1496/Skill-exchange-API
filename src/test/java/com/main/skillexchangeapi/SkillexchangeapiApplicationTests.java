package com.main.skillexchangeapi;

import com.main.skillexchangeapi.apirest.abstractions.DatasourceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SkillexchangeapiApplicationTests {
    @Autowired
    DatasourceConfig datasourceConfig;

    @Test
    void contextLoads() {
        datasourceConfig.setup();
    }

}

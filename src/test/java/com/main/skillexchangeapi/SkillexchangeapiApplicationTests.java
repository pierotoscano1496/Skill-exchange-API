package com.main.skillexchangeapi;

import com.main.skillexchangeapi.apirest.abstractions.DatasourceConfig;
import com.main.skillexchangeapi.app.utils.UuidManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class SkillexchangeapiApplicationTests {
    @Autowired
    DatasourceConfig datasourceConfig;

    @Test
    void contextLoads() {
        datasourceConfig.setup();
    }

    @Test
    void checkConversionUUIDToBin() {
        UUID uuid = UUID.fromString("2319073f-2284-11ee-8052-489ebd32b066");
        byte[] bytes = UuidManager.UuidToBytes(uuid);
        System.out.println(bytes);
    }

}

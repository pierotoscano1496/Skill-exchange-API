package com.main.skillexchangeapi;

import com.main.skillexchangeapi.app.utils.UuidManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestUuidManager {
    @Test
    void checkConversionUUIDToBin() {
        UUID uuid = UUID.fromString("2319073f-2284-11ee-8052-489ebd32b066");
        byte[] bytes = UuidManager.UuidToBytes(uuid);
        System.out.println(bytes);
    }

    @Test
    void checkBytesToUUID() {
        byte[] bytes = {11, 23, 33};
        UUID uuid = UuidManager.bytesToUuid(bytes);
        System.out.println(uuid);
    }

    @Test
    void chechIfRandomUuidHas16Lengh() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = UuidManager.UuidToBytes(uuid);
        assertTrue(bytes.length == 16);
    }

}

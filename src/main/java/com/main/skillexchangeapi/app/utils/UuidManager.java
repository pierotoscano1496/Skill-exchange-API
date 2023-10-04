package com.main.skillexchangeapi.app.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidManager {
    public static byte[] UuidToBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        return byteBuffer.array();
    }

    public static byte[] generateRandomBinaryUuid() {
        return UuidManager.UuidToBytes(UUID.randomUUID());
    }
}

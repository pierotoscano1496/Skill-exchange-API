package com.main.skillexchangeapi.domain.abstractions.services;

public interface ITokenBlackList {
    void addToBlacklist(String token);

    boolean isBlacklisted(String token);
}

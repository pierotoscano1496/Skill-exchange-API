package com.main.skillexchangeapi.app.security.services;

import com.main.skillexchangeapi.domain.abstractions.services.ITokenBlackList;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InMemoryTokenBlackList implements ITokenBlackList {
    private Set<String> blackList = new HashSet<>();

    @Override
    public void addToBlacklist(String token) {
        blackList.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blackList.contains(token);
    }
}

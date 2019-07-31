package ru.shaplov.storages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class MemoryStorage implements Storage {

    private static final Logger LOG = LogManager.getLogger(MemoryStorage.class);

    private final ConcurrentMap<Integer, User> users = new ConcurrentHashMap<>();

    private final AtomicInteger idCounter = new AtomicInteger();

    @Override
    public User add(User user) {
        user.setId(idCounter.incrementAndGet());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }
}

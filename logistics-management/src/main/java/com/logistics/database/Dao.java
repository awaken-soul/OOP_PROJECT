package com.logistics.database;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);
}

package ru.politech.theater.model.repositories;

import java.util.List;

public interface Repository<T> {

    void add(T item);

    void update(T item);

    void remove(T item);

    T get(int id);

    List<T> query();
}

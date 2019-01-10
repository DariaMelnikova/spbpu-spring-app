package ru.politech.theater.model.mappers;

import java.util.List;

abstract class AbstractMapper<T> {

    final String CONNECTION_URL = "jdbc:sqlite:theaterdb.db";

    public abstract void add(T item);

    public abstract T get(int id);

    public abstract List<T> getAll();

}

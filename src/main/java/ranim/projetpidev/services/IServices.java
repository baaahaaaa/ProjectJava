package ranim.projetpidev.services;

import java.util.List;

public interface IServices<T> {
    void add(T t);
    void delete(T t);
    void update(T t);
    T getById(int id);
    List<T> getAll();
}
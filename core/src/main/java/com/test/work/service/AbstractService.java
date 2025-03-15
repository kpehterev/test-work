package com.test.work.service;

import com.test.work.repository.CrudRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractService<R extends CrudRepository<E>, E, ID extends Long> {

    protected abstract R getRepository();

    public E save(E entity) {
        return getRepository().save(entity);
    }

    public List<E> saveAll(Collection<E> entity) {
        return getRepository().saveAll(entity);
    }

    public  List<E> findAll(){ return getRepository().findAll(); };

    public  Optional<E> findById(ID id){ return getRepository().findById(id); };

    public Optional<E> findByIdLocked(ID id) { return getRepository().findByIdLocked(id);}

}
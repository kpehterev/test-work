package com.test.work.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


@NoRepositoryBean
public interface GenericRepository<T, ID> extends Repository<T, ID> {
    @NonNull T save(@NonNull T model);

    @NonNull T saveAndFlush(@NonNull T model);

    List<T> saveAll(Iterable<T> entities);

    @NonNull Optional<T> findById(@NonNull ID id);

    @NonNull List<T> findAllById(@NonNull Iterable<ID> ids);

    @NonNull List<T> findAll();

    @NonNull List<T> findAll(@NonNull Pageable pageable);

    long count();

    void deleteById(@NonNull ID id);

    @NonNull Boolean existsById(@NonNull ID id);

    void deleteAll();

    void deleteAll(Iterable<? extends T> entities);

    T getReferenceById(ID id);

    @Query("FROM #{#entityName} e WHERE e.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<T> findByIdLocked(ID id);
}
package com.test.work.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepository<T> extends GenericRepository<T, Long> {}
package com.test;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompangDao extends CrudRepository<CompangEntity, Long>{



}

package com.example.database_microservice.repository;

import com.example.database_microservice.model.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ListRepository extends JpaRepository<Lists, Long> {

}



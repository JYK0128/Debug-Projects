package com.example.demo.Test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "person", path = "people")
public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {

    @RestResource(rel="IgnoreCase", path = "IgnoreCase")
    List<Person> findAllByFirstNameIgnoreCase(String first);
    // when export methods overridable, change to optional parameter for query url.
    Optional<Person> findById(Optional<Long> id);


    //    @Override
//    @RestResource(exported = false) //not 'export', make custom method wrapped origin
//    Optional<Person> findById(Long aLong);
}


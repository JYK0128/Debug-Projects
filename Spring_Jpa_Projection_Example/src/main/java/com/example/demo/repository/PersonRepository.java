package com.example.demo.repository;

import com.example.demo.domain.Person;
import com.example.demo.dto.PersonDto;
import com.example.demo.dto.PersonView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;
import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.List;

@RepositoryRestResource(path="person")
public interface PersonRepository extends JpaRepository<Person, Long> {
//    == Projection in Derived Query ==
    Person findByFirstNameAndLastName(String firstName, String lastName);
    PersonDto findByFirstName(String firstName);
    PersonView findByLastName(String lastName);

//    == Dynamic Projection ==
//    Parameter 'Type' is not working in url suggested by RepositoryRestResource
//    Only Use with Controller
    <T> T findById(Long id, Class<T> type);
    <T> List<T> findAllBy(Class<T> type);

//***************************************************************************
//    == Projection in JPQL ==
//    1. Entity(Working)
    @Query("select p from Person p where p.lastName = ?1")
    Person jpqlQuery(String lastName);
//    2. DTO Class(Working)
    @Query("select new com.example.demo.dto.PersonDto(p.lastName) from Person p where p.firstName = ?1")
    PersonDto jpqlDtoQuery(String firstName);
//    3. VIEW Interface(Working)
    @Query("select p from Person p where p.lastName = ?1")
    PersonView jpqlViewQuery(String lastName);

//    == Collection of Projection in JPQL ==
//    1. Entity(Working)
    @Query("select p from Person p")
    List<Person> jpqlListQuery();
//    2. DTO Class(Working only Java, Not Url suggested by RepositoryRestResource)
    @Query("select new com.example.demo.dto.PersonDto(p.lastName) from Person p")
    List<PersonDto> jpqlListDtoQuery();
//    3. VIEW Interface(Working only Java, Not Url suggested by RepositoryRestResource)
    @Query("select p from Person p")
    List<PersonView> jpqlListViewQuery();

//***************************************************************************
//    == Projection in Native Query ==  (Not working) //could not prepare statement
//    1. Entity(Working)
    @Query(value = "select * from Person p where p.lastName = ?1", nativeQuery = true)
    Person nativeQuery(@Param("lastName") String lastName);
//    2. DTO Class(Not Working - No converter found capable of converting from type)
    @Query(value = "select * from Person p where p.firstName = ?1", nativeQuery = true)
    PersonDto nativeDtoQuery(String firstName);
//    3. VIEW Interface(Working)
    @Query(value = "select * from Person p where p.lastName = ?1", nativeQuery = true)
    PersonView nativeViewQuery(String lastName);

//    == Collection of Projection in Native Query ==
//    1. Entity(working)
    @Query(value = "select * from Person p", nativeQuery = true)
    List<Person> nativeListQuery();
//    2. DTO Class(Not working - No converter found capable of converting from type)
    @Query(value = "select * from Person p", nativeQuery = true)
    List<PersonDto> nativeListDtoQuery();
//    3. VIEW Interface(Working)
    @Query(value = "select * from Person p", nativeQuery = true)
    List<PersonView> nativeListViewQuery();
}
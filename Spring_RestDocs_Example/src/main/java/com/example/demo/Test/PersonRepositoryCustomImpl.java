package com.example.demo.Test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;


public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Person findCustomById(Long id) {
        //queryDSL Entity
        QPerson person = QPerson.person;
        //Instant of JPAQuery, JPAQueryFactory can use "select" param
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        //like below
        Person p = query.select(person).from(person).where(person.id.eq(id)).fetchOne();
        return p;
    }
}
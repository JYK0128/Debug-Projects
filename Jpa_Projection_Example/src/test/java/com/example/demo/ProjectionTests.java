package com.example.demo;

import com.example.demo.domain.Person;
import com.example.demo.dto.PersonDto;
import com.example.demo.dto.PersonView;
import com.example.demo.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@DataJpaTest
public class ProjectionTests {
    @Autowired
    PersonRepository personRepository;

    @Test
    void Dynamic_Query_Find_Single(){
        PersonDto test1 = personRepository.findById((long) 1, PersonDto.class);
        PersonView test2 = personRepository.findById((long) 1, PersonView.class);

        System.out.println(test1.toString());
        System.out.println(test2.getFirstName());
    }

    @Test
    void Dynamic_Query_Find_All(){
        List<PersonDto> test1 = personRepository.findAllBy(PersonDto.class);
        List<PersonView> test2 = personRepository.findAllBy(PersonView.class);

        System.out.println(test1);
        System.out.println(test2);
    }

    @Test
    void List_JPQL_Dto_Query(){
        List<PersonDto> test = personRepository.jpqlListDtoQuery();
        System.out.println("testttttttttttt" + test);
        test.stream().forEach(t -> System.out.println(t));
    }

    @Test
    void List_JPQL_View_Query(){
        List<PersonView> test = personRepository.jpqlListViewQuery();
        System.out.println("testttttttttttt" + test);
        test.forEach(t -> System.out.println(t.getFirstName()));
    }

    @Test
    void Native_Query(){
        List<Person> test = personRepository.nativeListQuery();
        System.out.println("testttttttttttt" + test);
    }

    @Test
    void Native_List_Dto_Query() {
        List<Object[]> test = personRepository.nativeListDtoQuery();
        List<PersonDto> a = (List<PersonDto>) (Object) test;
        System.out.println("testttttttttttt" + a);

        List<PersonDto> customer = test.stream()
                .filter(PersonDto.class::isInstance)
                .map(PersonDto.class::cast)
                .collect(toList());

        System.out.println(customer);
    }

    @Test
    void Native_List_View_Query(){
        List<PersonView> test = personRepository.nativeListViewQuery();
        System.out.println("testttttttttttt" + test);
        test.stream().forEach(t -> System.out.println(t.getFirstName()));
    }
}

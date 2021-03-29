package ebi.ac.uk.person.crud.api;

import ebi.ac.uk.person.entity.Color;
import ebi.ac.uk.person.entity.Person;

import java.util.List;

/**
 * Person operations like CRUD operations on Person.
 */
public interface PersonCrudService {
    Person findUniquePerson(String firstName, String lastName, Integer age, Color favouriteColour);

    List<Person> allPeople();

    void addPerson(Person person);

    boolean updatePerson(Person person);

    boolean deletePerson(Person person);

    Person find(Long id);

    List<Person> findAll();
}

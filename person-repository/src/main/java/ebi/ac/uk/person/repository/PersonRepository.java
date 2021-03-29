package ebi.ac.uk.person.repository;


import ebi.ac.uk.person.entity.Color;
import ebi.ac.uk.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Person Repository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findById(Long id);

    List<Person> findByFirstName(String firstName);

    List<Person> findByLastName(String lastName);

    Person findByFirstNameAndLastNameAndAge(String firstName, String lastName, Integer age);

    Person findByFirstNameAndLastNameAndAgeAndFavouriteColour(String firstName, String lastName, Integer age, Color favouriteColour);

}
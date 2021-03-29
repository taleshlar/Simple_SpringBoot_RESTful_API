package ebi.ac.uk.person.crud.service;

import ebi.ac.uk.person.crud.api.PersonCrudService;
import ebi.ac.uk.person.entity.Color;
import ebi.ac.uk.person.entity.Person;
import ebi.ac.uk.person.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
public class PersonService implements PersonCrudService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    private Optional<Person> findOptionalById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        return (person.isPresent()) ? person : Optional.empty();
    }

    private Person findById(Long id) {
        Optional<Person> person = findOptionalById(id);
        return (person.isPresent()) ? person.get() : null;
    }


    private Optional<Person> findOptionalUniquePerson(String firstName, String lastName, Integer age, Color favouriteColour) {
        Person person = personRepository.findByFirstNameAndLastNameAndAgeAndFavouriteColour(firstName, lastName, age, favouriteColour);
        return (person != null) ? Optional.of(person) : Optional.empty();
    }

    @Override
    public Person findUniquePerson(String firstName, String lastName, Integer age, Color favouriteColour) {
        Optional<Person> person = findOptionalUniquePerson(firstName, lastName, age, favouriteColour);
        return (person.isPresent()) ? person.get() : null;

    }


    public List<Person> allPeople() {
        List<Person> people = personRepository.findAll();
        return people;
    }

    public void addPerson(Person person) {
        if (person == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "");
        Person existPerson = findUniquePerson(person.getFirstName(), person.getLastName(),
                person.getAge(), person.getFavouriteColour());
        if (existPerson == null) {
            personRepository.save(person);
        } else {
            LOG.debug("Person with id {} is duplicated", existPerson.getId());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Person is duplicated");
        }
    }


    public boolean updatePerson(Person person) {
        if (person == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "");
        boolean saveResult;
        Person existPerson = findById(person.getId());
        if (existPerson != null) {
            personRepository.save(person);
            saveResult = true;
        } else {
            LOG.debug("Person with id {} is not found", person.getId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Person is not found");
        }
        return saveResult;
    }

    public boolean deletePerson(Person person) {
        if (person == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "");

        boolean saveResult = false;
        Person existPerson = findById(person.getId());
        if (existPerson != null) {
            personRepository.delete(person);
            saveResult = true;
        } else {
            LOG.debug("Person with id {} is not found", person.getId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Person is not found");
        }
        return saveResult;
    }

    public Person find(Long id) {
        if (id == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "");
        return personRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Person is not found"));
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }
}
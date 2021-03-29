package ebi.ac.uk.person.controller;

import ebi.ac.uk.auth.api.UserAuthenticationService;
import ebi.ac.uk.person.annotation.UserBilling;
import ebi.ac.uk.person.crud.service.PersonService;
import ebi.ac.uk.person.entity.Person;
import ebi.ac.uk.person.dto.PersonDTO;
import io.swagger.annotations.*;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * The PersonRestController is permitting the user to perform operations only when logged in
 */
@RestController
@RequestMapping("/api/people")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Api(value = "Secured Person Service")
@Validated
public class SecuredPersonController {

    private static final Logger LOG = LoggerFactory.getLogger(SecuredPersonController.class);

    private PersonService personService;

    private UserAuthenticationService authentication;

    private final ModelMapper modelMapper;


    public SecuredPersonController(PersonService personService, @Qualifier("tokenAuthenticationService") UserAuthenticationService authentication, ModelMapper modelMapper) {
        this.personService = personService;
        this.authentication = authentication;
        this.modelMapper = modelMapper;
    }



    /**
     * <p>Retrieve all the people</p>
     *
     * @return list of people using JSON format
     */
    @ApiOperation(
            nickname = "allpeople",
            value = "allpeople",
            notes = "Using this Endpoint you can get all stored people")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved people list"),
            @ApiResponse(code = 401, message = "You are not authorized to call this endpoint"),
            @ApiResponse(code = 403, message = "Accessing some resource is forbidden")})
    @GetMapping("/allpeople")
    @UserBilling
    List<PersonDTO> all() {
        List<Person> persons = personService.findAll();
        List<PersonDTO> personDTOS = persons.stream().map(user -> modelMapper.map(user, PersonDTO.class))
                .collect(Collectors.toList());
        LOG.info("allpeople is called!");
        return personDTOS;
    }


    /**
     * <p>Find a person by id </p>
     *
     * @param id : unique identifier of the person
     * @return the person using JSON format or 404 not found
     */
    @ApiOperation(
            nickname = "person",
            value = "person",
            notes = "Using this Endpoint you can get a person with its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a person"),
            @ApiResponse(code = 401, message = "You are not authorized to call this endpoint"),
            @ApiResponse(code = 403, message = "Accessing some resource is forbidden")})
    @GetMapping("/person/{id}")
    PersonDTO one(@PathVariable Long id) {
        LOG.info("Finding person with id {}", id);
        Person person = personService.find(id);
        LOG.info("Person with id {} is found", id);
        return new PersonDTO(person.getId(), person.getFirstName(), person.getLastName(), person.getAge(), person.getFavouriteColour());
    }

    /**
     * <p>Add people to the db (POST) </p>
     *
     * @param people: list of people using json format
     * @return 200 and JSON message
     */
    @ApiOperation(
            nickname = "addpeople",
            value = "addpeople",
            notes = "Using this Endpoint you can add a list of persons")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully stored people list"),
            @ApiResponse(code = 401, message = "You are not authorized to call this endpoint"),
            @ApiResponse(code = 403, message = "Accessing some resource is forbidden")})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //We can use PreAuthorize to see if the user has the needed role to perform this method or not
    //@PreAuthorize("#oauth2.hasScope('add-people')")
    public @ResponseBody
    ResponseEntity<String> add
    (
            @ApiParam(
                    required = true,
                    name = "people",
                    value = "List of Persons to create"
            )
            @Valid @RequestBody Map<String, ArrayList<PersonDTO>> people) {
        HttpHeaders responseHeaders = setResponseHttpHeaders();

        if (people.get("person") != null) {
            for (PersonDTO personDTO : people.get("person")) {
                Person newPerson = new Person();
                modelMapper.map(personDTO, newPerson);
                personService.addPerson(newPerson);
                LOG.info("{} is added to the people list", newPerson.FullName());
            }
        }

        return new ResponseEntity<>(returnJSONMessage(), responseHeaders, HttpStatus.OK);
    }

    /**
     * <p>Update people using PUT </p>
     *
     * @param people: list of people using json format
     * @return 200 and JSON message
     */
    @ApiOperation(
            nickname = "updatepeople",
            value = "updatepeople",
            notes = "Using this Endpoint you edit people list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully edited people list"),
            @ApiResponse(code = 401, message = "You are not authorized to call this endpoint"),
            @ApiResponse(code = 403, message = "Accessing some resource is forbidden")})
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    //We can use PreAuthorize to see if the user has the needed role to perform this method or not
    //@PreAuthorize("#oauth2.hasScope('update-people')")
    public @ResponseBody
    ResponseEntity<String> update
    (
            @ApiParam(
                    required = true,
                    name = "person",
                    value = "List of Persons to update"
            )
            @Valid @RequestBody Map<String, ArrayList<PersonDTO>> people) {
        HttpHeaders responseHeaders = setResponseHttpHeaders();

        if (people.get("person") != null) {
            for (PersonDTO personDTO : people.get("person")) {
                Person person = new Person(personDTO.getId(), personDTO.getFirstName(), personDTO.getLastName(),
                        personDTO.getAge(), personDTO.getFavouriteColour());
                if (personService.updatePerson(person)) {
                    // you can count or return the list of updated people
                    System.out.println("Updated person".concat(person.getId().toString()));
                }
            }
        }
        LOG.info("updatepeople is called!");
        return new ResponseEntity<>(returnJSONMessage(), responseHeaders, HttpStatus.OK);
    }

    /**
     * <p>Delete people using DELETE request </p>
     *
     * @param people: list of people using json format
     * @return 200 and JSON message
     */
    @ApiOperation(
            nickname = "deletepeople",
            value = "deletepeople",
            notes = "Using this Endpoint you delete people list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted people list"),
            @ApiResponse(code = 401, message = "You are not authorized to call this endpoint"),
            @ApiResponse(code = 403, message = "Accessing some resource is forbidden")})
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    //We can use PreAuthorize to see if the user has the needed role to perform this method or not
    //@PreAuthorize("#oauth2.hasScope('delete_people')")
    public @ResponseBody
    ResponseEntity<String> delete
    (
            @ApiParam(
                    required = true,
                    name = "person",
                    value = "List of Persons to delete"
            )
            @Valid @RequestBody Map<String, ArrayList<PersonDTO>> people) {
        HttpHeaders responseHeaders = setResponseHttpHeaders();

        if (people.get("person") != null) {
            for (PersonDTO personDTO : people.get("person")) {
                Person deletePerson = new Person(personDTO.getId(), personDTO.getFirstName(), personDTO.getLastName(),
                        personDTO.getAge(), personDTO.getFavouriteColour());
                if (personService.deletePerson(deletePerson)) {
                    // you can store and return ids deleted.
                    System.out.println("Deleted person");
                }
            }
        }
        LOG.info("deletepeople is called!");
        return new ResponseEntity<>(returnJSONMessage(), responseHeaders, HttpStatus.OK);
    }


    // DRY: this function is called in many place of this class.
    private HttpHeaders setResponseHttpHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return responseHeaders;
    }

    // DRY: this function is called in many place of this class.
    // Return a JSON string "message":"ok"
    private String returnJSONMessage() {
        String result = new StringBuilder("{\"message\":\"ok").append("\"}").toString();
        return result;
    }

}

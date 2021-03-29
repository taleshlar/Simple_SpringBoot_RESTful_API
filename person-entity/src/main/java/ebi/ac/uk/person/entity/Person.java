package ebi.ac.uk.person.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Person Entity
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PEOPLE")
@SequenceGenerator(name = "people", sequenceName = "person_seq", allocationSize = 1)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("favourite_colour")
    private Color favouriteColour;

    // Constructor with params
    public Person(String firstName,
                  String lastName,
                  Integer age,
                  Color favouriteColour) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.favouriteColour = favouriteColour;
    }

    @JsonIgnore
    public String FullName() {
        return getFirstName() + " " + getLastName();
    }
}

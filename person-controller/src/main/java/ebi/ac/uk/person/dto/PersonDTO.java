package ebi.ac.uk.person.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ebi.ac.uk.person.entity.Color;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO that represents the Person.
 */
@ApiModel(description = "DTO that represents the person.")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO implements Serializable {

    private static final long serialVersionUID = -140598950020070176L;
    @ApiModelProperty(value = "Unique identifier of this person.", required = true, notes = "* It's always a number.")
    private Long id;

    @ApiModelProperty("First name of the person.")
    @JsonProperty("first_name")
    private String firstName;

    @ApiModelProperty("Last name of the person.")
    @JsonProperty("last_name")
    private String lastName;

    @ApiModelProperty("Age of the person.")
    @JsonProperty("age")
    private Integer age;

    @ApiModelProperty("Favourite Colour of the person.")
    @JsonProperty("favourite_colour")
    private Color favouriteColour;

}
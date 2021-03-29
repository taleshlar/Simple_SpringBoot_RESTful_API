package ebi.ac.uk.user.controller;

import ebi.ac.uk.auth.api.UserAuthenticationService;
import ebi.ac.uk.user.crud.api.UserCrudService;
import ebi.ac.uk.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

/**
 * The PublicUsersController allows a user to login into the application
 */
@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor
@Api(value = "Public Users Service")
public class PublicUsersController {

    private static final Logger LOG = LoggerFactory.getLogger(PublicUsersController.class);

    @NonNull
    UserAuthenticationService authentication;
    @NonNull
    UserCrudService users;


    /**
     * Register a new user and return an authentication token
     *
     * @param username Username
     * @param password Password.
     * @return token
     */
    @PostMapping("/register")
    @ApiOperation(value = "Register a user with provided username and password.", notes = "")
    @ApiResponses(@ApiResponse(code = 201, message = "User registered with success."))
    String register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {

        LOG.debug("Request to register a user with {} username", username);

        users.save(User.builder()
                .id(username)
                .username(username)
                .password(password)
                .build()
        );
        return login(username, password);
    }

    /**
     * login an existing user and return an authentication token (if any user found with matching password)
     *
     * @param username Username.
     * @param password Password.
     * @return token
     */
    @PostMapping("/login")
    @ApiOperation(value = "Login a user.", notes = "* User must be registered with register service before.\n* Otherwise access will be denied.")
    @ApiResponses(@ApiResponse(code = 201, message = "Logged in."))
    String login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        LOG.debug("Request to login a user with username {}", username);
        return authentication
                .login(username, password)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User is not found"));
    }
}

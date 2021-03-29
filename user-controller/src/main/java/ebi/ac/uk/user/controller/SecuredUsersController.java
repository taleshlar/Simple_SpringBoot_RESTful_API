package ebi.ac.uk.user.controller;

import ebi.ac.uk.auth.api.UserAuthenticationService;
import ebi.ac.uk.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

/**
 *
 */
@RestController
@RequestMapping("/api/user")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Api(value = "Secured User Service")
public class SecuredUsersController {

    private static final Logger LOG = LoggerFactory.getLogger(SecuredUsersController.class);

    private UserAuthenticationService authentication;

    @Autowired
    SecuredUsersController(@Qualifier("tokenAuthenticationService") UserAuthenticationService authentication) {
        this.authentication = authentication;
    }

    /**
     * Get the current user bean
     *
     * @param user
     * @return User
     */
    @GetMapping("/current")
    @ApiOperation(
            nickname = "current",
            value = "current",
            notes = "Using this Endpoint you can get current logged in user")
    User getCurrent(@AuthenticationPrincipal final User user) {
        return user;
    }

    /*
     * Logout from the application
     */
    @GetMapping("/logout")
    @ApiOperation(
            nickname = "logout",
            value = "logout",
            notes = "Using this Endpoint you can logout current user")
    boolean logout(@AuthenticationPrincipal final User user) {
        LOG.info("{} is logout", user.getUsername());
        authentication.logout(user);
        return true;
    }
}

package ebi.ac.uk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test class for the application.
 *
 * @see Application
 */
@RunWith(SpringRunner.class)
public class ApplicationTest {

    private static final String[] ARGS = {};

    @Test
    public void contextLoadsTest() {
        Application.main(ARGS);
    }

}


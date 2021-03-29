package ebi.ac.uk.service;

import ebi.ac.uk.Application;
import ebi.ac.uk.person.crud.service.PersonService;
import ebi.ac.uk.person.entity.Color;
import ebi.ac.uk.person.entity.Person;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Test class for the PersonServiceTest.
 *
 * @see PersonServiceTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceTest.class);
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Autowired
    private PersonService service;
    @Autowired
    private PersonService serviceMock;

    @Before
    public void setup() {

    }

    @Test
    public void deletePersonNullTest() {
        expectedException.expect(ResponseStatusException.class);
        service.deletePerson(null);
        Assert.fail("Should'n accept person null");
    }

    @Test
    public void updatePersonNullTest() {
        expectedException.expect(ResponseStatusException.class);
        service.updatePerson(null);
        Assert.fail("Should'n accept person null");
    }

    @Test
    public void addPersonNullTest() {
        expectedException.expect(ResponseStatusException.class);
        service.addPerson(null);
        Assert.fail("Should'n accept person null");
    }

    @Test
    public void findPersonNullTest() {
        expectedException.expect(ResponseStatusException.class);
        service.find(null);
        Assert.fail("Should'n accept person null");
    }

    @Test
    public void personDuplicationTest() {
        expectedException.expect(ResponseStatusException.class);
        Person firstPerson = new Person("Jack", "London", 50, Color.MAGENTA);
        service.addPerson(firstPerson);
        service.addPerson(firstPerson);
        Assert.fail("Should'n accept duplicate person");
    }

    @Test
    public void personNotFoundInUpdateTest() {
        expectedException.expect(ResponseStatusException.class);
        Person firstPerson = new Person(100L, "Jack", "London", 50, Color.MAGENTA);
        service.updatePerson(firstPerson);
        Assert.fail("Should'n accept not available person");
    }

    @Test
    public void personNotFoundInDeleteTest() {
        expectedException.expect(ResponseStatusException.class);
        Person firstPerson = new Person(100L, "Jack", "London", 50, Color.MAGENTA);
        service.deletePerson(firstPerson);
        Assert.fail("Should'n accept not available person");
    }

    public String generateRandomString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    private void createLoadTest() throws Exception {
        final int executionTimes = 10;
        for (int executionIndex = 0; executionIndex < executionTimes; executionIndex++) {
            final ExecutorService executorService = Executors
                    .newFixedThreadPool(8);
                executorService.execute(() -> {
                    try {
                        Person firstPerson = new Person(generateRandomString(), generateRandomString(), 50, Color.MAGENTA);
                        service.addPerson(firstPerson);
                    } catch (final AssertionError | Exception exception) {
                    }
                });

            executorService.shutdown();
            Assert.assertTrue("Timeout!!!",executorService.awaitTermination(400, TimeUnit.MILLISECONDS));
        }
    }

    @Test
    public void loadTest() throws Exception {
        final ExecutorService executorService = Executors
                .newFixedThreadPool(8);

        final int executionTimes = 10;
        for (int index = 0; index < executionTimes; index++) {
            executorService.execute(() -> {
                try {
                    createLoadTest();
                } catch (final Exception exception) {
                    LOG.error("Error at create load test", exception);
                }
            });
        }
        executorService.shutdown();
        Assert.assertTrue("Timeout!",executorService.awaitTermination(1, TimeUnit.SECONDS));
    }
}
package imartinez.com.spacematerial.peopleinspace;

import imartinez.com.spacematerial.net.RetrofitFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Integration test that checks the API is alive and responds with valid data.
 * // TODO: 2/11/16 This integration test does not make sense. Remove.
 */
public class PeopleInSpaceApiIntegrationTest {

    PeopleInSpaceNetworkController.RetrofitImpl networkController;

    @Before
    public void setUp() throws Exception {
        // Create a real network controller that goes to network
        networkController = new PeopleInSpaceNetworkController.RetrofitImpl(new RetrofitFactory(null));
    }

    @Test
    public void fetchPeopleInSpaceDoesContainValidValues() throws Exception {
        List<PersonInSpace> peopleInSpace = networkController.fetchPeopleInSpace();
        assertTrue(peopleInSpace.size() > 0);
    }

}
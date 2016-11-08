package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.net.RetrofitFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Integration test that checks the API is alive and responds with valid data.
 * // TODO: 2/11/16 This integration test does not make sense. Remove.
 */
public class IssLocationApiIntegrationTest {

    private IssLocationNetworkController.RetrofitImpl networkController;

    @Before
    public void setUp() throws Exception {
        // Create a real network controller that goes to network
        networkController = new IssLocationNetworkController.RetrofitImpl(new RetrofitFactory(null));
    }

    @Test
    public void fetchIssLocationDoesContainValidValues() throws Exception {
        IssLocation issLocation = networkController.fetchIssLocation();
        assertTrue(issLocation.timestamp() > 0);
        assertTrue(issLocation.latitude() != 0);
        assertTrue(issLocation.longitude() != 0);
    }

}
package imartinez.com.spacematerial.isslocation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Integration test that checks the API is alive and responds with valid data.
 */
public class IssLocationRetrofitNetworkControllerTest {

    private IssLocationRetrofitNetworkController networkController;

    @Before
    public void setUp() throws Exception {
        networkController = new IssLocationRetrofitNetworkController();
    }

    @Test
    public void fetchIssLocationDoesContainValidValues() throws Exception {
        IssLocation issLocation = networkController.fetchIssLocation();
        assertTrue(issLocation.timestamp() > 0);
        assertTrue(issLocation.latitude() != 0);
        assertTrue(issLocation.longitude() != 0);
    }

}
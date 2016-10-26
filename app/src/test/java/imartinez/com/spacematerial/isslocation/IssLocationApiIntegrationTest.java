package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.net.RetrofitFactory;
import okhttp3.Cache;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertTrue;

/**
 * Integration test that checks the API is alive and responds with valid data.
 */
public class IssLocationApiIntegrationTest {

    private IssLocationNetworkController.RetrofitImpl networkController;

    @Mock
    Cache cache;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        // Create a real network controller that goes to network
        networkController = new IssLocationNetworkController.RetrofitImpl(new RetrofitFactory(cache));
    }

    @Test
    public void fetchIssLocationDoesContainValidValues() throws Exception {
        IssLocation issLocation = networkController.fetchIssLocation();
        assertTrue(issLocation.timestamp() > 0);
        assertTrue(issLocation.latitude() != 0);
        assertTrue(issLocation.longitude() != 0);
    }

}
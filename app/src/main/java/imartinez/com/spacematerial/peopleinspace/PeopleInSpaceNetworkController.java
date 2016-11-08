package imartinez.com.spacematerial.peopleinspace;

import imartinez.com.spacematerial.net.RetrofitFactory;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpaceApi.PeopleInSpaceApiResponse;
import javax.inject.Inject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface PeopleInSpaceNetworkController {

    List<PersonInSpace> getCachedPeopleInSpace() throws IOException;
    List<PersonInSpace> fetchPeopleInSpace() throws IOException;

    class RetrofitImpl implements PeopleInSpaceNetworkController {

        private final PeopleInSpaceApi networkService;
        private final PeopleInSpaceApi cacheService;

        @Inject
        RetrofitImpl(RetrofitFactory retrofitFactory) {
            networkService = retrofitFactory.create(PeopleInSpaceApi.PEOPLE_IN_SPACE_API_BASE_URL,
                    PeopleInSpaceApi.class);
            cacheService = retrofitFactory.createCacheOnly(PeopleInSpaceApi.PEOPLE_IN_SPACE_API_BASE_URL,
                    PeopleInSpaceApi.class);
        }

        @Override
        public List<PersonInSpace> getCachedPeopleInSpace() throws IOException {
            PeopleInSpaceApiResponse serviceResponse = cacheService.fetchPeopleInSpace().execute().body();

            if (serviceResponse == null) {
                throw new IOException("No cached IssLocation available");
            }

            return extractPersonInSpaceList(serviceResponse);
        }

        @Override
        public List<PersonInSpace> fetchPeopleInSpace() throws IOException {
            PeopleInSpaceApiResponse serviceResponse = networkService.fetchPeopleInSpace().execute().body();
            return extractPersonInSpaceList(serviceResponse);
        }

        private List<PersonInSpace> extractPersonInSpaceList(PeopleInSpaceApiResponse serviceResponse) {
            ArrayList<PersonInSpace> peopleInSpace = new ArrayList<>();
            for (PeopleInSpaceApiResponse.Person person : serviceResponse.people()) {
                peopleInSpace.add(PersonInSpace.builder()
                        .setName(person.name())
                        .setBioPhotoImageUrl(person.biophoto())
                        .setCountryFlagImageUrl(person.countryflag())
                        .setLaunchDate(person.launchdate())
                        .setCareerDays(person.careerdays())
                        .setTitle(person.title())
                        .setLocation(person.location())
                        .setBio(person.bio())
                        .setBioLinkUrl(person.biolink())
                        .build());
            }

            return Collections.unmodifiableList(peopleInSpace);
        }
    }
}

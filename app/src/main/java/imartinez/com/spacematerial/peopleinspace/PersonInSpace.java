package imartinez.com.spacematerial.peopleinspace;

import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.Date;

@AutoValue
abstract class PersonInSpace implements Serializable {

    abstract String name();
    abstract String bioPhotoImageUrl();
    abstract String countryFlagImageUrl();
    abstract Date launchDate();
    abstract int careerDays();
    abstract String title();
    abstract String location();
    abstract String bio();
    abstract String bioLinkUrl();

    static Builder builder() {
        return new AutoValue_PersonInSpace.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract PersonInSpace.Builder setName(String name);
        abstract PersonInSpace.Builder setBioPhotoImageUrl(String bioPhotoImageUrl);
        abstract PersonInSpace.Builder setCountryFlagImageUrl(String countryFlagImageUrl);
        abstract PersonInSpace.Builder setLaunchDate(Date launchDate);
        abstract PersonInSpace.Builder setCareerDays(int careerDays);
        abstract PersonInSpace.Builder setTitle(String title);
        abstract PersonInSpace.Builder setLocation(String location);
        abstract PersonInSpace.Builder setBio(String bio);
        abstract PersonInSpace.Builder setBioLinkUrl(String biolinkUrl);
        abstract PersonInSpace build();
    }

}

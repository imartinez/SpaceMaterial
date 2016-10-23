package imartinez.com.spacematerial.isslocation;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class IssLocation {

    abstract long timestamp();
    abstract double latitude();
    abstract double longitude();

    static Builder builder() {
        return new AutoValue_IssLocation.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setTimestamp(long value);
        abstract Builder setLatitude(double value);
        abstract Builder setLongitude(double value);
        abstract IssLocation build();
    }

}

package imartinez.com.spacematerial.isslocation;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * TypeAdapterFactory for all auto-value-gson classes used in the network layer.
 */
@GsonTypeAdapterFactory
abstract class IssLocationServiceAdapterFactory implements TypeAdapterFactory {

    public static TypeAdapterFactory create() {
        return new AutoValueGson_IssLocationServiceAdapterFactory();
    }

}

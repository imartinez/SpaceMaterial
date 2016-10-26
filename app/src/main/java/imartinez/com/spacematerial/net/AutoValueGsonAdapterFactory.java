package imartinez.com.spacematerial.net;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * TypeAdapterFactory for all auto-value-gson classes used in the network layer.
 */
@GsonTypeAdapterFactory
abstract class AutoValueGsonAdapterFactory implements TypeAdapterFactory {

    public static TypeAdapterFactory create() {
        return new AutoValueGson_AutoValueGsonAdapterFactory();
    }

}

package core.config;

import com.google.gson.GsonBuilder;

/**
 * Created by Admin on 19.04.2015.
 *
 */
public interface IGsonAdapterAdder {
    GsonBuilder addTypeAdapters(GsonBuilder builder);
}

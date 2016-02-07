package autaxi.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import autaxi.core.config.sources.IConfigSource;

public class ConfigFactory {

    public static <T> T getFromSource(Class<T> type, IConfigSource source) {
        String sourceStr = source.getConfigStr();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapter(Port.class, new JSONPortAdapter<Port>())
                .registerTypeAdapter(type, new ConfigDeserializer<Class<T>>())
                .create();
        return gson.fromJson(sourceStr, type);
    }
}

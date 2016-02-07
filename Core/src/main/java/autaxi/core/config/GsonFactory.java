package autaxi.core.config;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 19.04.2015.
 *
 */
public class GsonFactory {

    private static Map<String, IGsonAdapterAdder> adapters = new HashMap<>();

    public static void registerTypeAdapterAdder(String name, IGsonAdapterAdder adder) {
        adapters.putIfAbsent(name, adder);
    }

    public static Gson getGson() {
        GsonBuilder gson = new GsonBuilder();
        for (IGsonAdapterAdder adder : adapters.values()) {
            adder.addTypeAdapters(gson);
        }
        return gson
                .setPrettyPrinting()
                .create();
    }

    public static Gson getGsonDT() {
        return Converters.registerDateTime(new GsonBuilder()).create();
    }
}

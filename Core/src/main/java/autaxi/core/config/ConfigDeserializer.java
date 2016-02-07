package autaxi.core.config;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfigDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String className = ((Class) typeOfT).getSimpleName();
        if (jsonObject.has(className)) {
            JsonElement elem = jsonObject.get(className);
            if (elem != null && !elem.isJsonNull()) {
                JsonObject valuesString = elem.getAsJsonObject();
                return GsonFactory.getGson().fromJson(valuesString.toString(), typeOfT);
            }
        }
        return null;
    }
}
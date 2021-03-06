package co.timecrypt.api.v2.database.postgresql;

import co.timecrypt.api.v2.database.TimecryptDataStore;
import co.timecrypt.api.v2.servlets.TimecryptApiServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * This class should handle creation of {@link TimecryptDataStore}(s). If you need a more complex implementation, override that here.
 */
public class PostgresProvider {

    private static Map<String, TimecryptDataStore> storeCache = new HashMap<>();

    /**
     * This gets called whenever a servlet is initialized. Do some complex pool logic or caching here if you need to.
     *
     * @param servlet Which servlet was just initialized
     */
    public static void onServletInitialized(TimecryptApiServlet servlet) {
        TimecryptDataStore dataStore = new PostgresController();
        try {
            dataStore.init(servlet);
        } catch (IllegalStateException e) {
            servlet.log("Failed to initialize " + String.valueOf(dataStore), e);
        }
        storeCache.put(servlet.getClass().getSimpleName(), dataStore);
    }

    /**
     * This method should create and return the data store for the given servlet. If you have any complex caching logic, use it now.
     *
     * @param servlet Which servlet needs the data store
     * @return The data store instance. If you return {@code null}, everything will crash
     */
    public static TimecryptDataStore getDataStore(TimecryptApiServlet servlet) {
        return storeCache.get(servlet.getClass().getSimpleName());
    }

    /**
     * This gets called whenever a servlet becomes destroyed. Do some pool cleanup logic or un-caching here if you need to.
     *
     * @param servlet Which servlet was just destroyed
     */
    public static void onServletDestroyed(TimecryptApiServlet servlet) {
        TimecryptDataStore dataStore = storeCache.get(servlet.getClass().getSimpleName());
        dataStore.destroy();
        storeCache.remove(servlet.getClass().getSimpleName());
    }

}

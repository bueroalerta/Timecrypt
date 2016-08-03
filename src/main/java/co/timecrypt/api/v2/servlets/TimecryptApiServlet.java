
package co.timecrypt.api.v2.servlets;

import co.timecrypt.api.v2.database.DataStoreProvider;
import co.timecrypt.api.v2.database.TimecryptDatabase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The base class for all Timecrypt servlets. This handles data store initialization, destruction and other stuff.
 */
public abstract class TimecryptApiServlet extends HttpServlet {

    private TimecryptDatabase dataStore;

    @Override
    public void init() throws ServletException {
        super.init();
        DataStoreProvider.onServletInitialized(this);
        dataStore = DataStoreProvider.getDataStore(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        DataStoreProvider.onServletDestroyed(this);
        dataStore = null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        if (isPostAllowed()) {
            handleRequest(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        if (isGetAllowed()) {
            handleRequest(req, resp);
        }
    }

    /**
     * @return This servlet's data store
     */
    protected TimecryptDatabase getDataStore() {
        return dataStore;
    }

    /**
     * This handles the request for both {@link #doPost(HttpServletRequest, HttpServletResponse)} and
     * {@link #doGet(HttpServletRequest, HttpServletResponse)}. If you don't want to handle any of these, override {@link #isPostAllowed()}
     * or {@link #isGetAllowed()}, respectively.
     * 
     * @param request Which request to handle
     * @param response Which response to use for the reaction
     */
    abstract protected void handleRequest(HttpServletRequest request, HttpServletResponse response);

    /**
     * @return {@code True} if {@link #doPost(HttpServletRequest, HttpServletResponse)} is allowed to be handled by
     *         {@link #handleRequest(HttpServletRequest, HttpServletResponse)}, {@code false} if not
     */
    protected boolean isPostAllowed() {
        return true;
    }

    /**
     * @return {@code True} if {@link #doGet(HttpServletRequest, HttpServletResponse)} is allowed to be handled by
     *         {@link #handleRequest(HttpServletRequest, HttpServletResponse)}, {@code false} if not
     */
    protected boolean isGetAllowed() {
        return true;
    }

}

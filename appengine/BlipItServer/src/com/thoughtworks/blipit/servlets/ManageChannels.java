package com.thoughtworks.blipit.servlets;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.DataStoreHelper;
import com.thoughtworks.contract.common.Category;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ManageChannels extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ManageChannels.class.getName());

    // Handles channel saving & deleting
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (isValidAction(action)) {
            PersistenceManager persistenceManager = null;
            try {
                persistenceManager = DataStoreHelper.getPersistenceManager();
                if (isDeleteAction(action)) {
                    deleteChannel(persistenceManager, req);
                } else if (isSaveAction(action)) {
                    saveChannel(persistenceManager, req);
                }
            } finally {
                if (persistenceManager != null) persistenceManager.close();
            }
        }
        resp.sendRedirect("/ManageBlipIt.jsp");
    }

    private void saveChannel(PersistenceManager persistenceManager, HttpServletRequest request) {
        String channelName = request.getParameter("channel.name");
        String channelCategory = request.getParameter("channel.category");
        Channel channel = new Channel(channelName, Utils.convert(Category.valueOf(channelCategory)));
        persistenceManager.makePersistent(channel);
    }

    private void deleteChannel(PersistenceManager persistenceManager, HttpServletRequest request) {
        Enumeration parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                if (parameterName.matches("key[0-9]+")) {
                    String channelKeyStr = request.getParameter(parameterName);
                    Key key = KeyFactory.stringToKey(channelKeyStr);
                    Channel channel = persistenceManager.getObjectById(Channel.class, key);
                    persistenceManager.deletePersistent(channel);
                }
            }
        }
    }

    private boolean isValidAction(String action) {
        return isDeleteAction(action) || isSaveAction(action);
    }

    private boolean isSaveAction(String action) {
        return "save".equalsIgnoreCase(action);
    }

    private boolean isDeleteAction(String action) {
        return "delete".equalsIgnoreCase(action);
    }
}

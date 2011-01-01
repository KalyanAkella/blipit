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

import static com.thoughtworks.blipit.Utils.isValidManageAction;

public class ManageChannels extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ManageChannels.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (isValidManageAction(action)) {
            PersistenceManager persistenceManager = null;
            try {
                persistenceManager = DataStoreHelper.getPersistenceManager();
                if (Utils.isDeleteAction(action)) {
                    deleteChannel(persistenceManager, req);
                } else if (Utils.isSaveAction(action)) {
                    saveChannel(persistenceManager, req);
                }
            } finally {
                if (persistenceManager != null) persistenceManager.close();
            }
        }
        resp.sendRedirect("/ManageBlipIt.jsp?category=channels");
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

}

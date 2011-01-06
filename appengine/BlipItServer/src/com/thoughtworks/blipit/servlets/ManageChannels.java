/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit.servlets;

import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.CategoryEnum;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.DataStoreHelper;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static com.thoughtworks.blipit.Utils.isValidManageAction;

public class ManageChannels extends HttpServlet {

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
        Channel channel = new Channel(channelName, Utils.convert(CategoryEnum.valueOf(channelCategory)));
        persistenceManager.makePersistent(channel);
    }

    private void deleteChannel(PersistenceManager persistenceManager, HttpServletRequest request) {
        Enumeration parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                if (parameterName.matches("key[0-9]+")) {
                    String channelKeyStr = request.getParameter(parameterName);
                    Key key = Utils.constructKey(Channel.class, channelKeyStr);
                    Channel channel = persistenceManager.getObjectById(Channel.class, key);
                    persistenceManager.deletePersistent(channel);
                }
            }
        }
    }

}

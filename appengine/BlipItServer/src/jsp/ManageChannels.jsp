<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.thoughtworks.blipit.domain.Channel" %>
<%@ page import="com.thoughtworks.blipit.Utils" %>
<%@ page import="com.thoughtworks.blipit.persistence.DataStoreHelper" %>

<h3>Manage Channels</h3>
<form id="list-entities-form" action="/manageChannels" method="post">
    <input type="hidden" name="category" value="channels"/>
    <input type="hidden" name="action" value="delete"/>
    <table class="entities" id="entities_table">
        <thead>
        <tr>
            <th><input id="allkeys" name="allkeys" type="checkbox" onclick="checkAllEntities();"/></th>
            <th id="entities_table_header_key">Key</th>
            <th id="entities_table_header_name">Name</th>
            <th id="entities_table_header_category">Category</th>
        </tr>
        <%
                PersistenceManager persistenceManager = DataStoreHelper.getPersistenceManager();
                Query query = persistenceManager.newQuery(Channel.class);
                List<Channel> channels = (List<Channel>) query.execute();
                int index = 0;
                if (Utils.isNotEmpty(channels)) {
                    for (Channel channel : channels) {
                        index ++;
                        String evenOdd = (index & 1) == 1 ? "odd" : "even";
        %>
                <tr class="<%= evenOdd%>">
                    <td>
                        <input id="key<%= index%>" type="checkbox" name="key<%= index%>" value="<%= channel.getKey().toString()%>" onclick="updateDeleteButtonAndCheckbox();"/>
                    </td>
                    <td><%= channel.getKey().toString()%></td>
                    <td><%= channel.getName()%></td>
                    <td><%= channel.getCategory().getCategory()%></td>
                </tr>
        <%
                    }
                }
                query.closeAll();
                persistenceManager.close();
        %>
    </table>
    <div class="entities g-section g-tpl-50-50">
        <div class="g-unit g-first">
            <div id="entities-control">
            <input id="delete_button" type="submit" value="Delete" onclick="return confirm('Are you sure you wish to delete these channel(s)?')"/>
            </div>
        </div>
    </div>
</form>
<br>
<h5>Add a new channel:</h5>
<form id="add-channels-form" action="/manageChannels" method="post">
    <input type="hidden" name="category" value="channels"/>
    <input type="hidden" name="action" value="save"/>
    <div>
        <fieldset title="Add a new channel">
            <label for="channel.name">Name:</label>
            <input id="channel.name" type="text" name="channel.name"/>
            <br>
            <label for="channel.category">Category:</label>
            <select id="channel.category" name="channel.category">
                <option value="PANIC" selected="selected">Panic</option>
                <option value="AD">Ad</option>
            </select>
            <br>
            <input type="submit" value="Add"/>
        </fieldset>
    </div>
</form>

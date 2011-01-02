<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.thoughtworks.blipit.domain.Channel" %>
<%@ page import="com.thoughtworks.blipit.domain.Blip" %>
<%@ page import="com.thoughtworks.blipit.Utils" %>
<%@ page import="com.thoughtworks.blipit.persistence.DataStoreHelper" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>

<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false">
</script>
<script type="text/javascript">
  var map;
  function initialize() {
    var latlng = new google.maps.LatLng(12.966667,77.566667);
    var myOptions = {
      zoom: 8,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      mapTypeControl: false,
      navigationControl: true,
      navigationControlOptions: {
         style: google.maps.NavigationControlStyle.SMALL
      }
    };
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    var marker = new google.maps.Marker({
      position: latlng,
      map: map,
      draggable: true
    });
    var lat_input = document.getElementById("blip.loc.lat");
    var long_input = document.getElementById("blip.loc.long");
    var infowindow = new google.maps.InfoWindow(
      { content: "Drag me to change lat:long pair",
        size: new google.maps.Size(50,50)
      }
    );
    google.maps.event.addListener(marker, 'click', function() {
	infowindow.open(map, marker);
    });
    google.maps.event.addListener(marker, 'drag', function(event) {
	lat_input.value = event.latLng.lat();
	long_input.value = event.latLng.lng();
    });
  }
  google.maps.event.addDomListener(window, 'load', initialize);
</script>

<h3>Manage Blips</h3>
<form id="list-entities-form" action="/manageBlips" method="post">
    <input type="hidden" name="category" value="blips"/>
    <input type="hidden" name="action" value="delete"/>
    <table class="entities" id="entities_table">
        <thead>
        <tr>
            <th><input id="allkeys" name="allkeys" type="checkbox" onclick="checkAllEntities();"/></th>
            <th id="entities_table_header_key">Key</th>
            <th id="entities_table_header_name">Title</th>
            <th id="entities_table_header_desc">Description</th>
            <th id="entities_table_header_geopoint">Location</th>
            <th id="entities_table_header_creatorId">CreatorId</th>
            <th id="entities_table_header_channels">Channel(s)</th>
        </tr>
        <%
                PersistenceManager persistenceManager = DataStoreHelper.getPersistenceManager();
                Query query = persistenceManager.newQuery(Blip.class);
                List<Blip> blips = (List<Blip>) query.execute();
                int index = 0;
                if (Utils.isNotEmpty(blips)) {
                    for (Blip blip : blips) {
                        index ++;
                        String evenOdd = (index & 1) == 1 ? "odd" : "even";
                        StringBuilder buffer = new StringBuilder();
                        for (Key channelKey : blip.getChannelKeys()) {
                            Channel channel = persistenceManager.getObjectById(Channel.class, channelKey);
                            buffer.append(channel.getName()).append("|");
                        }
                        if (buffer.length() > 0) buffer.deleteCharAt(buffer.length() - 1);
                        String channelStr = buffer.toString();
        %>
                <tr class="<%= evenOdd%>">
                    <td>
                        <input id="key<%= index%>" type="checkbox" name="key<%= index%>" value="<%= blip.getKeyAsString()%>" onclick="updateDeleteButtonAndCheckbox();"/>
                    </td>
                    <td><%= blip.getKeyAsString()%></td>
                    <td><%= blip.getTitle()%></td>
                    <td><%= blip.getDescription()%></td>
                    <td><%= blip.getGeoPoint()%></td>
                    <td><%= blip.getCreatorId()%></td>
                    <td><%= channelStr%></td>
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
            <input id="delete_button" type="submit" value="Delete" onclick="return confirm('Are you sure you wish to delete these blip(s)?')"/>
            </div>
        </div>
    </div>
</form>
<br>
<h5>Add a new blip:</h5>
<form id="add-blips-form" action="/manageBlips" method="post">
    <input type="hidden" name="category" value="blips"/>
    <input type="hidden" name="action" value="save"/>
    <div>
        <label for="blip.title">Title:</label>
        <input type="text" id="blip.title" name="blip.title"/>
    </div>
    <div>
        <label for="blip.desc">Description:</label>
        <input type="text" id="blip.desc" name="blip.desc"/>
    </div>
    <div>
        <label for="blip.channels">Channels:</label>
        <select id="blip.channels" name="blip.channels" size="2" multiple="true">
        <%
            persistenceManager = DataStoreHelper.getPersistenceManager();
            query = persistenceManager.newQuery(Channel.class);
            List<Channel> channels = (List<Channel>) query.execute();
            if (Utils.isNotEmpty(channels)) {
                for (Channel channel : channels) {
        %>
            <option value="<%= channel.getKeyAsString()%>"><%= channel.getName()%></option>
        <%
                }
            }
            query.closeAll();
            persistenceManager.close();
        %>
        </select>
    </div>
    <div>
        <label for="blip.loc.lat">Latitude (in degrees):</label>
        <input type="text" id="blip.loc.lat" name="blip.loc.lat"/>
    </div>
    <div>
        <label for="blip.loc.long">Longitude (in degrees):</label>
        <input type="text" id="blip.loc.long" name="blip.loc.long"/>
    </div>
    <div id="map_canvas" style="border: 1px dotted darkblue; width: 600px; height: 500px;"></div>
    <div><input type="submit" value="Add"/></div>
</form>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
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
    var lat_input = document.getElementById("alert.loc.lat");
    var long_input = document.getElementById("alert.loc.long");
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
</head>
<body>
<form action="/blipit/publish" method="post">
    <div>
        <label for="alert.title">Alert Title:</label>
        <input type="text" id="alert.title" name="alert.title"/>
    </div>
    <div>
        <label for="alert.desc">Alert Desc:</label>
        <input type="text" id="alert.desc" name="alert.desc"/>
    </div>
    <div>
        <label for="alert.channels">Channels (comma separated):</label>
        <input type="text" id="alert.channels" name="alert.channels"/>
    </div>
    <div>
        <label for="alert.loc.lat">Latitude (in degrees):</label>
        <input type="text" id="alert.loc.lat" name="alert.loc.lat"/>
    </div>
    <div>
        <label for="alert.loc.long">Longitude (in degrees):</label>
        <input type="text" id="alert.loc.long" name="alert.loc.long"/>
    </div>
    <div id="map_canvas" style="border: 1px dotted darkblue; width: 600px; height: 500px;"></div>
    <div><input type="submit" value="Insert Alert"/></div>
</form>
</body>
</html>

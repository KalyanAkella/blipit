<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
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
        <label for="alert.loc.lat">Latitude (in degrees):</label>
        <input type="text" id="alert.loc.lat" name="alert.loc.lat"/>
    </div>
    <div>
        <label for="alert.loc.long">Longitude (in degrees):</label>
        <input type="text" id="alert.loc.long" name="alert.loc.long"/>
    </div>
    <div><input type="submit" value="Insert Alert"/></div>
</form>
</body>
</html>
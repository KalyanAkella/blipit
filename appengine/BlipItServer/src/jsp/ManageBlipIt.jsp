<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<style type="text/css">
    /* Copyright 2008 Google, Inc. All Rights Reserved */
    html,body,div,h1,h2,h3,h4,h5,h6,p,img,dl,dt,dd,ol,ul,li,table,caption,tbody,tfoot,thead,tr,th,td,form,fieldset,embed,object,applet{margin:0;padding:0;border:0}body{font-size:62.5%;font-family:Arial,sans-serif;color:#000;background:#fff}a{color:#00c}a:active{color:#f00}a:visited{color:#551a8b}table{border-collapse:collapse;border-width:0;empty-cells:show}ul{padding:0
    0 1em 1em}ol{padding:0 0 1em 1.3em}li{line-height:1.5em;padding:0 0 .5em 0}p{padding:0 0 1em
    0}h1,h2,h3,h4,h5{padding:0 0 1em
    0}h1,h2{font-size:1.3em}h3{font-size:1.1em}h4,h5,table{font-size:1em}sup,sub{font-size:.7em}input,select,textarea,option{font-family:inherit;font-size:inherit}.g-doc,.g-doc-1024,.g-doc-800{font-size:130%}.g-doc{width:100%;text-align:left}.g-section:after{content:".";display:block;height:0;clear:both;visibility:hidden}.g-unit
    .g-section:after{clear:none}.g-unit .g-section{width:100%;overflow:hidden}.g-section,.g-unit{zoom:1}.g-split
    .g-unit{text-align:right}.g-split .g-first{text-align:left}.g-tpl-25-75 .g-unit,.g-unit .g-tpl-25-75 .g-unit,.g-unit
    .g-unit .g-tpl-25-75 .g-unit,.g-unit .g-unit .g-unit .g-tpl-25-75
    .g-unit{width:74.999%;float:right;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-25-75 .g-first,.g-unit
    .g-unit .g-tpl-25-75 .g-first,.g-unit .g-tpl-25-75 .g-first,.g-tpl-25-75
    .g-first{width:24.999%;float:left;display:inline;margin:0}.g-tpl-25-75-alt .g-unit,.g-unit .g-tpl-25-75-alt
    .g-unit,.g-unit .g-unit .g-tpl-25-75-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-25-75-alt
    .g-unit{width:24.999%;float:left;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-25-75-alt .g-first,.g-unit
    .g-unit .g-tpl-25-75-alt .g-first,.g-unit .g-tpl-25-75-alt .g-first,.g-tpl-25-75-alt
    .g-first{width:74.999%;float:right;display:inline;margin:0}.g-tpl-75-25 .g-unit,.g-unit .g-tpl-75-25 .g-unit,.g-unit
    .g-unit .g-tpl-75-25 .g-unit,.g-unit .g-unit .g-unit .g-tpl-75-25
    .g-unit{width:24.999%;float:right;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-75-25 .g-first,.g-unit
    .g-unit .g-tpl-75-25 .g-first,.g-unit .g-tpl-75-25 .g-first,.g-tpl-75-25
    .g-first{width:74.999%;float:left;display:inline;margin:0}.g-tpl-75-25-alt .g-unit,.g-unit .g-tpl-75-25-alt
    .g-unit,.g-unit .g-unit .g-tpl-75-25-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-75-25-alt
    .g-unit{width:74.999%;float:left;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-75-25-alt .g-first,.g-unit
    .g-unit .g-tpl-75-25-alt .g-first,.g-unit .g-tpl-75-25-alt .g-first,.g-tpl-75-25-alt
    .g-first{width:24.999%;float:right;display:inline;margin:0}.g-tpl-33-67 .g-unit,.g-unit .g-tpl-33-67 .g-unit,.g-unit
    .g-unit .g-tpl-33-67 .g-unit,.g-unit .g-unit .g-unit .g-tpl-33-67
    .g-unit{width:66.999%;float:right;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-33-67 .g-first,.g-unit
    .g-unit .g-tpl-33-67 .g-first,.g-unit .g-tpl-33-67 .g-first,.g-tpl-33-67
    .g-first{width:32.999%;float:left;display:inline;margin:0}.g-tpl-33-67-alt .g-unit,.g-unit .g-tpl-33-67-alt
    .g-unit,.g-unit .g-unit .g-tpl-33-67-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-33-67-alt
    .g-unit{width:32.999%;float:left;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-33-67-alt .g-first,.g-unit
    .g-unit .g-tpl-33-67-alt .g-first,.g-unit .g-tpl-33-67-alt .g-first,.g-tpl-33-67-alt
    .g-first{width:66.999%;float:right;display:inline;margin:0}.g-tpl-67-33 .g-unit,.g-unit .g-tpl-67-33 .g-unit,.g-unit
    .g-unit .g-tpl-67-33 .g-unit,.g-unit .g-unit .g-unit .g-tpl-67-33
    .g-unit{width:32.999%;float:right;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-67-33 .g-first,.g-unit
    .g-unit .g-tpl-67-33 .g-first,.g-unit .g-tpl-67-33 .g-first,.g-tpl-67-33
    .g-first{width:66.999%;float:left;display:inline;margin:0}.g-tpl-67-33-alt .g-unit,.g-unit .g-tpl-67-33-alt
    .g-unit,.g-unit .g-unit .g-tpl-67-33-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-67-33-alt
    .g-unit{width:66.999%;float:left;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-67-33-alt .g-first,.g-unit
    .g-unit .g-tpl-67-33-alt .g-first,.g-unit .g-tpl-67-33-alt .g-first,.g-tpl-67-33-alt
    .g-first{width:32.999%;float:right;display:inline;margin:0}.g-tpl-50-50 .g-unit,.g-unit .g-tpl-50-50 .g-unit,.g-unit
    .g-unit .g-tpl-50-50 .g-unit,.g-unit .g-unit .g-unit .g-tpl-50-50
    .g-unit{width:49.999%;float:right;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-50-50 .g-first,.g-unit
    .g-unit .g-tpl-50-50 .g-first,.g-unit .g-tpl-50-50 .g-first,.g-tpl-50-50
    .g-first{width:49.999%;float:left;display:inline;margin:0}.g-tpl-50-50-alt .g-unit,.g-unit .g-tpl-50-50-alt
    .g-unit,.g-unit .g-unit .g-tpl-50-50-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-50-50-alt
    .g-unit{width:49.999%;float:left;display:inline;margin:0}.g-unit .g-unit .g-unit .g-tpl-50-50-alt .g-first,.g-unit
    .g-unit .g-tpl-50-50-alt .g-first,.g-unit .g-tpl-50-50-alt .g-first,.g-tpl-50-50-alt
    .g-first{width:49.999%;float:right;display:inline;margin:0}.g-tpl-nest .g-unit,.g-unit .g-tpl-nest .g-unit,.g-unit
    .g-unit .g-tpl-nest .g-unit,.g-unit .g-unit .g-unit .g-tpl-nest
    .g-unit{float:left;width:auto;display:inline;margin:0}.g-tpl-nest-alt .g-unit,.g-unit .g-tpl-nest-alt
    .g-unit,.g-unit .g-unit .g-tpl-nest-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-nest-alt
    .g-unit{float:right;width:auto;display:inline;margin:0}.g-doc-1024{width:73.074em;*width:71.313em;min-width:950px;margin:0
    auto;text-align:left}.g-doc-800{width:57.69em;*width:56.3em;min-width:750px;margin:0 auto;text-align:left}.g-tpl-160
    .g-unit,.g-unit .g-tpl-160 .g-unit,.g-unit .g-unit .g-tpl-160 .g-unit,.g-unit .g-unit .g-unit .g-tpl-160
    .g-unit{display:block;margin:0 0 0 161px;width:auto;float:none}.g-unit .g-unit .g-unit .g-tpl-160 .g-first,.g-unit
    .g-unit .g-tpl-160 .g-first,.g-unit .g-tpl-160 .g-first,.g-tpl-160
    .g-first{display:block;margin:0;width:161px;float:left}.g-tpl-160-alt .g-unit,.g-unit .g-tpl-160-alt .g-unit,.g-unit
    .g-unit .g-tpl-160-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-160-alt .g-unit{display:block;margin:0 161px 0
    0;width:auto;float:none}.g-unit .g-unit .g-unit .g-tpl-160-alt .g-first,.g-unit .g-unit .g-tpl-160-alt
    .g-first,.g-unit .g-tpl-160-alt .g-first,.g-tpl-160-alt
    .g-first{display:block;margin:0;width:161px;float:right}.g-tpl-180 .g-unit,.g-unit .g-tpl-180 .g-unit,.g-unit
    .g-unit .g-tpl-180 .g-unit,.g-unit .g-unit .g-unit .g-tpl-180 .g-unit{display:block;margin:0 0 0
    181px;width:auto;float:none}.g-unit .g-unit .g-unit .g-tpl-180 .g-first,.g-unit .g-unit .g-tpl-180 .g-first,.g-unit
    .g-tpl-180 .g-first,.g-tpl-180 .g-first{display:block;margin:0;width:181px;float:left}.g-tpl-180-alt .g-unit,.g-unit
    .g-tpl-180-alt .g-unit,.g-unit .g-unit .g-tpl-180-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-180-alt
    .g-unit{display:block;margin:0 181px 0 0;width:auto;float:none}.g-unit .g-unit .g-unit .g-tpl-180-alt
    .g-first,.g-unit .g-unit .g-tpl-180-alt .g-first,.g-unit .g-tpl-180-alt .g-first,.g-tpl-180-alt
    .g-first{display:block;margin:0;width:181px;float:right}.g-tpl-300 .g-unit,.g-unit .g-tpl-300 .g-unit,.g-unit
    .g-unit .g-tpl-300 .g-unit,.g-unit .g-unit .g-unit .g-tpl-300 .g-unit{display:block;margin:0 0 0
    301px;width:auto;float:none}.g-unit .g-unit .g-unit .g-tpl-300 .g-first,.g-unit .g-unit .g-tpl-300 .g-first,.g-unit
    .g-tpl-300 .g-first,.g-tpl-300 .g-first{display:block;margin:0;width:301px;float:left}.g-tpl-300-alt .g-unit,.g-unit
    .g-tpl-300-alt .g-unit,.g-unit .g-unit .g-tpl-300-alt .g-unit,.g-unit .g-unit .g-unit .g-tpl-300-alt
    .g-unit{display:block;margin:0 301px 0 0;width:auto;float:none}.g-unit .g-unit .g-unit .g-tpl-300-alt
    .g-first,.g-unit .g-unit .g-tpl-300-alt .g-first,.g-unit .g-tpl-300-alt .g-first,.g-tpl-300-alt
    .g-first{display:block;margin:0;width:301px;float:right}
</style>
<style type="text/css">
    /* Goog.css Overrides */
    h1 {
    font-size: 1.5em;
    }

    .g-doc {
    width: auto;
    margin: 0 10px;
    }

    /* Header Selectors */
    #ae-logo {
    margin-bottom: 0;
    }
    #ae-appbar-lrg {
    margin: 0 0 1.25em 0;
    padding: .2em .6em;
    background-color: #e5ecf9;
    border-top: 1px solid #36c;
    }
    #ae-appbar-lrg h1 {
    margin: 0;
    padding: 0;
    }

    /* Footer Selectors */
    #ft p {
    text-align: center;
    margin-top: 2.5em;
    padding-top: .5em;
    border-top: 2px solid #c3d9ff;
    }

    /* bd selectors */
    #bd h3 {
    font-weight: bold;
    font-size: 1.4em;
    }
    #bd p {
    padding: 0 0 1em 0;
    }
    #ae-content {
    padding-left: 1em;
    border-left: 3px solid #e5ecf9;
    min-height: 200px;
    }

    /* Tables */
    .ae-table-plain {
    border-collapse: collapse;
    width: 100%;
    }
    .ae-table {
    border: 1px solid #c5d7ef;
    border-collapse: collapse;
    width: 100%;
    }

    #bd h2.ae-table-title {
    background: #e5ecf9;
    margin: 0;
    color: #000;
    font-size: 1em;
    padding: 3px 0 3px 5px;
    border-left: 1px solid #c5d7ef;
    border-right: 1px solid #c5d7ef;
    border-top: 1px solid #c5d7ef;
    }
    .ae-table-caption,
    .ae-table caption {
    border: 1px solid #c5d7ef;
    background: #e5ecf9;
    /**
    * Fixes the caption margin ff display bug.
    * see www.aurora-il.org/table_test.htm
    * this is a slight variation to specifically target FF since Safari
    * was shifting the caption over in an ugly fashion with margin-left: -1px
    */
    -moz-margin-start: -1px;
    }
    .ae-table caption {
    padding: 3px 5px;
    text-align: left;
    }
    .ae-table th,
    .ae-table td {
    background-color: #fff;
    padding: .35em 1em .25em .35em;
    margin: 0;
    }
    .ae-table thead th {
    font-weight: bold;
    text-align: left;
    background: #c5d7ef;
    vertical-align: bottom;
    }
    .ae-table tfoot tr td {
    border-top: 1px solid #c5d7ef;
    background-color: #e5ecf9;
    }
    .ae-table td {
    border-top: 1px solid #c5d7ef;
    border-bottom: 1px solid #c5d7ef;
    }
    .ae-even td,
    .ae-even th,
    .ae-even-top td,
    .ae-even-tween td,
    .ae-even-bottom td,
    ol.ae-even {
    background-color: #e9e9e9;
    border-top: 1px solid #c5d7ef;
    border-bottom: 1px solid #c5d7ef;
    }
    .ae-even-top td {
    border-bottom: 0;
    }
    .ae-even-bottom td {
    border-top: 0;
    }
    .ae-even-tween td {
    border: 0;
    }
    .ae-table .ae-tween td {
    border: 0;
    }
    .ae-table .ae-tween-top td {
    border-bottom: 0;
    }
    .ae-table .ae-tween-bottom td {
    border-top: 0;
    }
    .ae-table #ae-live td {
    background-color: #ffeac0;
    }
    .ae-table-fixed {
    table-layout: fixed;
    }
    .ae-table-fixed td,
    .ae-table-nowrap {
    overflow: hidden;
    white-space: nowrap;
    }
    .ae-new-usr td {
    border-top: 1px solid #ccccce;
    background-color: #ffe;
    }
    .ae-error-td td {
    border: 2px solid #f00;
    background-color: #fee;
    }
    .ae-table .ae-pager {
    background-color: #c5d7ef;
    }

    .ae-errorbox {
    border: 1px solid #f00;
    background-color: #fee;
    margin-bottom: 1em;
    padding: 1em;
    display: inline-block;
    }

    .ae-message {
    border: 1px solid #e5ecf9;
    background-color: #f6f9ff;
    margin-bottom: 1em;
    padding: 1em;
    display: inline-block;
    }
</style>
<style type="text/css">
    #ae-nav ul {
    list-style-type: none;
    margin: 0;
    padding: 1em 0;
    }
    #ae-nav ul li {
    padding-left: .5em;
    }

    #ae-nav .ae-nav-selected {
    color: #000;
    display: block;
    font-weight: bold;
    background-color: #e5ecf9;
    border-top-left-radius: 4px;
    -moz-border-radius-topleft: 4px;
    -webkit-border-top-left-radius: 4px;
    border-bottom-left-radius: 4px;
    -moz-border-radius-bottomleft: 4px;
    -webkit-border-bottom-left-radius: 4px;
    }

    a.ae-nav-selected {
    color: #000;
    text-decoration:none;
    }

    /* aka disabled items */
    #ae-nav ul li span.ae-nav-disabled {
    color: #666;
    }

    /* Sub-navigation rules */
    #ae-nav ul ul {
    margin: 0;
    padding: 0 0 0 .5em;
    }
    #ae-nav ul ul li {
    padding-left: .5em;
    }
    #ae-nav ul li a,
    #ae-nav ul li span,
    #ae-nav ul ul li a {
    padding-left: .5em;
    }

    /* ae-nav Link Selectors */
    #ae-nav li a:link,
    #ae-nav li a:visited {
    color: #00c;
    }
    #ae-nav li a:link.ae-nav-selected,
    #ae-nav li a:visited.ae-nav-selected {
    color: #000;
    text-decoration: none;
    }

    /* Group of boxed help links */
    .ae-nav-group {
    padding: .5em;
    margin: 0 .75em 0 0;
    background-color: #fffbe8;
    border: 1px solid #fff1a9;
    }
    .ae-nav-group h4 {
    font-weight: bold;
    padding: auto auto .5em .5em;
    padding-left: .4em;
    margin-bottom: .5em;
    padding-bottom: 0;
    }
    .ae-nav-group ul {
    margin: 0 0 .5em 0;
    padding: 0 0 0 1.3em;
    list-style-type: none;
    }
    .ae-nav-group ul li {
    padding-bottom: .5em;
    }

    /* ae-nav-group link Selectors */
    .ae-nav-group li a:link,
    .ae-nav-group li a:visited {
    color: #00c;
    }
    .ae-nav-group li a:hover {
    color: #00c;
    }
</style>

<title>BlipIt Management Console</title>
<style type="text/css">
    #datastore_search {
    margin-bottom: 1em;
    }

    #hint {
    background-color: #F6F9FF;
    border: 1px solid #E5ECF9;
    margin-bottom: 1em;
    padding: 0.5em 1em;
    }

    #message {
    color: red;
    position: relative;
    bottom: 6px;
    }

    #pagetotal {
    float: right;
    }

    #pagetotal .count {
    font-weight: bold;
    }

    table.entities {
    border: 1px solid #c5d7ef;
    border-collapse: collapse;
    width: 100%;
    margin-bottom: 0;
    }

    table.entities th, table.entities td {
    padding: .25em 1.5em .5em .5em;
    }

    table.entities th {
    font-weight: bold;
    text-align: left;
    background: #e5ecf9;
    white-space: nowrap;
    }

    table.entities th a, table.entities th a:visited {
    color: black;
    text-decoration: none;
    }

    table.entities td {
    background-color: #fff;
    text-align: left;
    vertical-align: top;
    cursor: pointer;
    }

    table.entities tr.even td {
    background-color: #f9f9f9;
    }

    div.entities {
    background-color: #c5d7ef;
    margin-top: 0;
    }

    #entities-pager, #entities-control {
    padding: .3em 1em .4em 1em;
    }

    #entities-pager {
    text-align: right;
    }
</style>
<style type="text/css">
    .ae-page-number {
    margin: 0 0.5em;
    }

    .ae-page-selected {
    font-weight: bold;
    }
</style>
<script type="text/javascript">
  //<![CDATA[

  function load() {
    checkAllEntities();
  }

  function checkAllEntities() {
    var allCheckBox = document.getElementById("allkeys");
    var check = allCheckBox.checked;
    var entitiesForm = document.getElementById("list-entities-form")
    var keyPattern = /key[0-9]+/i;
    for (var i = 0; i < entitiesForm.length; i++) {
        if (keyPattern.test(entitiesForm.elements[i].name)) {
            var box = entitiesForm.elements[i];
            if (box) box.checked = check;
        }
    }
    updateDeleteButtonAndCheckbox();
  }

  function updateDeleteButtonAndCheckbox() {
    var button = document.getElementById("delete_button");
    var uncheck = false;
    var disable = true;
    var entitiesForm = document.getElementById("list-entities-form")
    var keyPattern = /key[0-9]+/i;
    for (var i = 0; i < entitiesForm.length; i++) {
        if (keyPattern.test(entitiesForm.elements[i].name)) {
            var box = entitiesForm.elements[i];
            if (box) {
                if (box.checked) {
                    disable = false;
                } else {
                    uncheck = true;
                }
            }
        }
    }
    button.disabled = disable;
    if (uncheck) {
      document.getElementById("allkeys").checked = false;
    }
  }

  //]]>
</script>
</head>
<body onload="load()">
<div class="g-doc">

    <div id="hd" class="g-section">

        <div id="ae-appbar-lrg" class="g-section">
            <h1>BlipIt Management Console</h1>

        </div>

    </div>

    <div id="bd" class="g-section">

        <div class="g-section g-tpl-160">

            <div id="ae-lhs-nav" class="g-unit g-first">

                <div id="ae-nav" class="g-c">

                    <ul id="menu">
                        <li><a href="ManageBlipIt.jsp?category=channels" id="manage_channels_link">Manage Channels</a></li>
                        <li><a href="ManageBlipIt.jsp?category=blips" id="manage_blips_link">Manage Blips</a></li>
                    </ul>

                </div>

            </div>
            <div id="ae-content" class="g-unit">
            <% String actionType = request.getParameter("category");
            if ("blips".equals(actionType)) { %>
            <jsp:include page="ManageBlips.jsp" />
            <% } else { %>
            <jsp:include page="ManageChannels.jsp" />
            <% } %>
            </div>
            <div id="ft">
                <p>
                    &copy;2010 The BlipIt Project
                </p>

            </div>
        </div>
    </div>
</div>
</body>
</html>

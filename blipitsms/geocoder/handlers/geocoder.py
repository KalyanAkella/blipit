#!/usr/bin/env python
# vim: ai ts=4 sts=4 et sw=4


from rapidsms.contrib.handlers.handlers.keyword import KeywordHandler
import urllib2, urllib
import json

class GeoCoderHandler(KeywordHandler):
    """
    Handle any message prefixed ``echo``, responding with the remainder
    of the text. Useful for remotely testing internationalization.
    """

    keyword = "panic|fire|PANIC|FIRE"

    def help(self):
        self.respond("To echo some text, send: PANIC <ANYTHING>")

    def handle(self, text):
        url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true&address=" + urllib.quote_plus(text)
        result = urllib2.urlopen(url)
        json_data = json.loads(result.read())
        if json_data['status'] == u'OK':
            latitude = json_data['results'][0]['geometry']['location']['lat']
            longitude = json_data['results'][0]['geometry']['location']['lng']
            self.respond("lat is : %f and longitude is : %f" % (latitude,longitude))
            return
            
        self.respond("Sorry we do not understand your address!")
        

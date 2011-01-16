from rapidsms.contrib.handlers.handlers.keyword import KeywordHandler
import urllib2, urllib
import json
from blipitsms.geocoder.blip import Channel, GeoPoint, Blip, BlipItPost

class GeoCoderHandler(KeywordHandler):
    keyword = "panic|fire|PANIC|FIRE|"

    def help(self):
        self.respond("To echo some text, send: PANIC <ANYTHING>")

    def handle(self, text):
        pass
        # url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true&address=" + urllib.quote_plus(text)
        #         result = urllib2.urlopen(url)
        #         json_data = json.loads(result.read())
        #         if json_data['status'] == u'OK':
        #             latitude = json_data['results'][0]['geometry']['location']['lat']
        #             longitude = json_data['results'][0]['geometry']['location']['lng']
        #             self.respond("lat is : %f and longitude is : %f" % (latitude,longitude))
        #             creatorId = self.msg.connection.identity
        #             c1 = Channel(3001)
        #             c2 = Channel(4001)
        #             g1 = GeoPoint(latitude, longitude)
        #             b = Blip([c1,c2], "PANIC sms", g1, "PANIC sms", creatorId)
        #             blipitPost = BlipItPost(b)
        #             blipitPost.post()
        #             return
            
        # self.respond("Sorry we do not understand your address!")
        

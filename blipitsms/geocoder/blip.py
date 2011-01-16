class Channel(dict):
	
	def __init__(self, id):
		self['id'] = id
		
class GeoPoint(dict):
	
	def __init__(self, latitude, longitude):
		self['latitude'] = latitude
		self['longitude'] = longitude

import json
	
class Blip(dict):
	
	def __init__(self, channel_list, description, geopoint, title, creatorId):
		self['channelKeys'] = channel_list
		self['description'] = description
		self['geoPoint'] = geopoint
		self['title'] = title
		self['creatorId'] = creatorId

        def get_json(self):
                return json.dumps(self)

import urllib2
from django.conf import settings
		
class BlipItPost:
	
	url = settings.BLIPIT_SVC_PANIC_URL
	
	def __init__(self, blip):
		self.blip = blip
		
	def post(self):
                request = urllib2.Request(self.url, self.blip.get_json())
                response = urllib2.urlopen(request)
		return

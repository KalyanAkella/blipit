class Channel(dict):
	def __init__(self, id):
		self['id'] = id
		
class GeoPoint(dict):
	def __init__(self, latitude, longitude):
		self['latitude'] = latitude
		self['longitude'] = longitude
	
class Blip(dict):
	def __init__(self, channel_list, description, geopoint, title):
		self['channelKeys'] = channel_list
		self['description'] = description
		self['geoPoint'] = geopoint
		self['title'] = title

import urllib2, json
		
class BlipItPost:
	
	self.url = "http://blipitsvc.appspot.com/blipit/panic/blip"
	
	def __init_(self, blip):
		self.blip = blip
		
	def post(self):
		json_data = json.dumps(self.blip)
		request = urllib2.Request(self.url, json_data)
		response = urllib2.urlopen(request)
		return
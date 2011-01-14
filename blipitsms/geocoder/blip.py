class Channel(dict):
	
	def __init__(self, id):
		self['id'] = id
		
class GeoPoint(dict):
	
	def __init__(self, latitude, longitude):
		self['latitude'] = latitude
		self['longitude'] = longitude
	
class Blip(dict):
	
	def __init__(self, channel_list, description, geopoint, title, creatorId):
		self['channelKeys'] = channel_list
		self['description'] = description
		self['geoPoint'] = geopoint
		self['title'] = title
		self['creatorId'] = creatorId

import urllib2, json
		
class BlipItPost:
	
	url = "http://blipitsvc.appspot.com/blipit/panic/blip"
	
	def __init__(self, blip):
		self.blip = blip
		
	def post(self):
		json_data = json.dumps(self.blip)
		print json_data
		request = urllib2.Request(self.url, json_data)
		response = urllib2.urlopen(request)
		return
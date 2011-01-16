from celery.decorators import task
import urllib2, urllib, json
from models import Channel
from django.conf import settings

from blip import Blip, BlipItPost, GeoPoint

@task
def scan_channels(urlopen = urllib2.urlopen(settings.BLIPIT_SVC_CHANNELS_URL)):
    json_data = json.loads(urlopen.read())
    for channel in json_data:
        channel_id = channel['key']['id']
        channel_name = channel['name']
        if len(Channel.objects.filter(channel_name = channel_name)) is 0:
            Channel(channel_id = channel_id, channel_name = channel_name).save()
            
    return Channel.objects


@task
def send(phone_number, address, channel_list):
    url = settings.GEOCODER_SVC_URL + urllib.quote_plus(address)
    url_request = urllib2.urlopen(url)
    response = url_request.read()
    geocoder_result = json.loads(response)
    if geocoder_result['status'] == u'OK':
        latitude = geocoder_result['results'][0]['geometry']['location']['lat']
        longitude = geocoder_result['results'][0]['geometry']['location']['lng']
        blip = Blip(channel_list, "PANIC SMS", GeoPoint(latitude, longitude), "PANIC SMS", phone_number)
        blipItPost = BlipItPost(blip)
        blipItPost.post()
        
        return True

    return False
                  


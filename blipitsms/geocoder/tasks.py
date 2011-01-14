from celery.decorators import task
import urllib2, json
from models import Channel

@task
def scan_channels():
    url = "http://blipitsvc.appspot.com/blipit/panic/channel"
    req = urllib2.urlopen(url)
    json_data = json.loads(req.read())
    for channel in json_data:
        channel_id = channel['key']['id']
        channel_name = channel['name']
        if len(Channel.objects.filter(channel_name = channel_name)) is 0:
            Channel(channel_id = channel_id, channel_name = channel_name).save()
        
        
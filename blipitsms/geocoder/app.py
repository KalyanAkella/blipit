from rapidsms.apps.base import AppBase
from models import Channel
from geocoder import tasks
from geocoder import  blip

class App(AppBase):

    def parse(self, msg):
        msg_txt = (msg.text).strip().split(" ",1)
        print msg_txt
        channels = Channel.objects.filter(channel_name = msg_txt[0])
        print channels
        channel_list = []
        for channel in channels:
            channel_list.append(blip.Channel(channel.channel_id))
        
        print channel_list
        if len(channel_list) is not 0:
            tasks.send.delay(msg.connection.identity, msg_txt[1], channel_list, msg)
            msg.respond("We are processing your request")
            return
        
        msg.respond("Sorry, we do not have a channel called %s" % (msg_txt[0],))


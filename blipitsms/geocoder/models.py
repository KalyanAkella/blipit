from django.db import models

class Channel(models.Model):
    channel_id = models.IntegerField()
    channel_name = models.CharField(max_length = 20)
    
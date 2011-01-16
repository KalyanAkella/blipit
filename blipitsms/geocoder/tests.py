import unittest
from mock import Mock

import tasks
from blip import Channel

class GeoCoderTest(unittest.TestCase):
    test_json_data = '[{"key":{"kind":"Channel","id":3001},"name":"panic1","category":{"category":"PANIC"}},\
    {"key":{"kind":"Channel","id":4001},"name":"panic2","category":{"category":"PANIC"}},\
    {"key":{"kind":"Channel","id":5001},"name":"panic3","category":{"category":"PANIC"}},\
    {"key":{"kind":"Channel","id":6001},"name":"panic4","category":{"category":"PANIC"}},\
    {"key":{"kind":"Channel","id":7001},"name":"panic5","category":{"category":"PANIC"}},\
    {"key":{"kind":"Channel","id":52001},"name":"fire","category":{"category":"PANIC"}},\
    {"key":{"kind":"Channel","id":53001},"name":"accident","category":{"category":"PANIC"}}]'
    
    def test_scan_channels(self):
        urlopen = Mock()
        urlopen.read = Mock(return_value = self.test_json_data)
        channel_objs = tasks.scan_channels(urlopen)
        self.assertEqual(len(channel_objs.all()), 7)
        
    def test_send_correct_address(self):
        addr = "1600 Amphitheatre Parkway,Mountain+View,CA"
        channel_list = [Channel(12), Channel(13)]
        result = tasks.send(123, addr, channel_list)
        self.assertEqual(result,True)

    def test_send_wrong_address(self):
        addr = "1600 KoramanagalIndi"
        channel_list = [Channel(12), Channel(13)]
        result = tasks.send(123, addr, channel_list)
        self.assertEqual(result,False)

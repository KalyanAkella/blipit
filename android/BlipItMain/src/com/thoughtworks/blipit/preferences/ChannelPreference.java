/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thoughtworks.blipit.utils.BlipItUtils;
import com.thoughtworks.contract.common.Channel;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.blipit.utils.BlipItUtils.CHANNEL_PREF_KEY;

public class ChannelPreference extends DialogPreference {
    private ListView listView;
    private List<Channel> availableChannels;
    private List<String> availableChannelNames;

    public ChannelPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        listView = new ListView(getContext());
        initAvailableChannels();
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, availableChannelNames));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        checkItemsFromPreferences();
        builder.setView(listView);
    }

    private void initAvailableChannels() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String allChannelsStr = sharedPreferences.getString(BlipItUtils.ALL_CHANNELS_KEY, null);
        availableChannels = BlipItUtils.toChannelList(allChannelsStr);
        availableChannelNames = new ArrayList<String>();
        for (Channel availableChannel : availableChannels) {
            availableChannelNames.add(availableChannel.getName());
        }
    }

    private void checkItemsFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String preferredChannelStr = sharedPreferences.getString(CHANNEL_PREF_KEY, null);
        if (preferredChannelStr != null) {
            List<Channel> prefChannelList = BlipItUtils.toChannelList(preferredChannelStr);
            for (Channel prefChannel : prefChannelList) {
                int channelIndex = availableChannelNames.indexOf(prefChannel.getName());
                if (channelIndex >= 0) {
                    listView.setItemChecked(channelIndex, true);
                }
            }
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            List<Channel> channelList = getSelectedChannels();
            SharedPreferences sharedPreferences = getSharedPreferences();
            sharedPreferences.edit().putString(CHANNEL_PREF_KEY, BlipItUtils.getChannelsAsString(channelList)).commit();
        }
    }

    private List<Channel> getSelectedChannels() {
        List<Channel> channelList = new ArrayList<Channel>();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < availableChannelNames.size(); i++) {
            String channel = availableChannelNames.get(i);
            if (checkedItemPositions.get(i, false)) {
                for (Channel availableChannel : availableChannels) {
                    if (availableChannel.getName().equals(channel)) {
                        channelList.add(availableChannel);
                        break;
                    }
                }
            }
        }
        return channelList;
    }
}

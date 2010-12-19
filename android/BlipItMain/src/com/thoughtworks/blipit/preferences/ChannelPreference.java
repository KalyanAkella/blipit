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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thoughtworks.blipit.utils.BlipItUtils.CHANNEL_SPLITTER;
import static com.thoughtworks.blipit.utils.BlipItUtils.CHANNEL_PREF_KEY;

public class ChannelPreference extends DialogPreference {
    private ListView listView;
    private List<String> channels;

    public ChannelPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        listView = new ListView(getContext());
        channels = getChannels();
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, channels));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        checkItemsFromPreferences();
        builder.setView(listView);
    }

    // TODO: Make a web service call to retrieve the various channels available
    private List<String> getChannels() {
        return Arrays.asList("Food", "Retail", "Transport", "Gaming", "Movies", "Fire", "Accident");
    }

    private void checkItemsFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String preferredChannelStr = sharedPreferences.getString(CHANNEL_PREF_KEY, null);
        if (preferredChannelStr != null) {
            String[] channels = preferredChannelStr.split(CHANNEL_SPLITTER);
            for (String channel : channels) {
                int channelIndex = this.channels.indexOf(channel);
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
            List<String> channelListStr = getSelectedChannels();
            SharedPreferences sharedPreferences = getSharedPreferences();
            sharedPreferences.edit().putString(CHANNEL_PREF_KEY, BlipItUtils.getChannelsAsString(channelListStr)).commit();
        }
    }

    private List<String> getSelectedChannels() {
        List<String> channelListStr = new ArrayList<String>();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < channels.size(); i++) {
            String channel = channels.get(i);
            if (checkedItemPositions.get(i, false)) channelListStr.add(channel);
        }
        return channelListStr;
    }
}

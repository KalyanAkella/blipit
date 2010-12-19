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
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.util.Log;

import static com.thoughtworks.blipit.utils.BlipItUtils.APP_TAG;
import static com.thoughtworks.blipit.utils.BlipItUtils.RADIUS_PREF_KEY;
import static com.thoughtworks.blipit.utils.BlipItUtils.getRadius;

public class RadiusPreference extends EditTextPreference {
    public RadiusPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // TODO: Currently does nothing. But this may not be the right thing to do here !
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        Log.w(APP_TAG, "onSetInitialValue called on RadiusPreference");
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        float radius = getRadius(getSharedPreferences(), RADIUS_PREF_KEY);
        getEditText().setText(String.valueOf(radius));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String radiusStr = this.getEditText().getText().toString();
            float radius;
            try {
                radius = Float.valueOf(radiusStr);
            } catch (NumberFormatException e) {
                radius = 2f;
            }
            getSharedPreferences().edit().putFloat(RADIUS_PREF_KEY, radius).commit();
        }
    }
}

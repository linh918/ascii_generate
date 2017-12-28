/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.common.purchase;

import android.content.Context;

import com.duy.common.BuildConfig;
import com.duy.common.utils.DLog;


/**
 * Created by Duy on 14-Jul-17.
 */

public class Premium {
    public static final String BASE64_KEY = BuildConfig.BASE64_KEY;

    //SKU for my product: the premium upgrade
    public static final String SKU_PREMIUM = BuildConfig.SKU_PREMIUM;
    private static final String TAG = "Premium";

    /**
     * Faster
     */
    private static boolean IS_PREMIUM = false;

    /**
     * Purchase user
     *
     * @param context - Android context
     */
    public static boolean isPremiumUser(Context context) {
        return IS_PREMIUM || FileUtil.licenseCached(context);
    }

    /**
     * Purchase user
     */
    public static void setPremiumUser(Context context, boolean isPremium) {
        DLog.d(TAG, "setPremiumUser() called with: context = [" + context + "], isPremium = [" + isPremium + "]");
        IS_PREMIUM = isPremium;
        if (isPremium) {
            FileUtil.saveLicence(context);
        } else {
            FileUtil.clearLicence(context);
        }
    }


    /**
     * @param context - the android context
     * @return true if free user
     */
    public static boolean isFreeUser(Context context) {
        return !isPremiumUser(context);
    }


}

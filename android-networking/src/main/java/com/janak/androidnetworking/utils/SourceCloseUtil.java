/*
 *    Copyright (C) 2020 Janak Khimsuriya
 *    Copyright (C) 2020 Android Open Source Project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.janak.androidnetworking.utils;

import com.janak.androidnetworking.common.ANRequest;
import com.janak.androidnetworking.common.ResponseType;

import okhttp3.Response;

/**
 * Created by Janak Khimsuriya on 01/11/2020.
 */
@SuppressWarnings("ALL")
public final class SourceCloseUtil {

    private SourceCloseUtil() {
    }

    public static void close(Response response, ANRequest request) {
        if (request.getResponseAs() != ResponseType.OK_HTTP_RESPONSE &&
                response != null && response.body() != null &&
                response.body().source() != null) {
            try {
                response.body().source().close();
            } catch (Exception ignore) {

            }
        }
    }
}

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

package com.janak.androidnetworking.internal;

import com.janak.androidnetworking.common.ANConstants;
import com.janak.androidnetworking.common.ANRequest;
import com.janak.androidnetworking.common.ANResponse;
import com.janak.androidnetworking.common.ResponseType;
import com.janak.androidnetworking.error.ANError;
import com.janak.androidnetworking.utils.SourceCloseUtil;
import com.janak.androidnetworking.utils.Utils;

import okhttp3.Response;

import static com.janak.androidnetworking.common.RequestType.DOWNLOAD;
import static com.janak.androidnetworking.common.RequestType.MULTIPART;
import static com.janak.androidnetworking.common.RequestType.SIMPLE;

/**
 * Created by Janak Khimsuriya on 01/11/2020.
 */
@SuppressWarnings("ALL")
public final class SynchronousCall {

    private SynchronousCall() {

    }

    public static <T> ANResponse<T> execute(ANRequest request) {
        switch (request.getRequestType()) {
            case SIMPLE:
                return executeSimpleRequest(request);
            case DOWNLOAD:
                return executeDownloadRequest(request);
            case MULTIPART:
                return executeUploadRequest(request);
        }
        return new ANResponse<>(new ANError());
    }

    private static <T> ANResponse<T> executeSimpleRequest(ANRequest request) {
        Response okHttpResponse = null;
        try {
            okHttpResponse = InternalNetworking.performSimpleRequest(request);
            if (okHttpResponse == null) {
                return new ANResponse<>(Utils.getErrorForConnection(new ANError()));
            }

            if (request.getResponseAs() == ResponseType.OK_HTTP_RESPONSE) {
                ANResponse response = new ANResponse(okHttpResponse);
                response.setOkHttpResponse(okHttpResponse);
                return response;
            }
            if (okHttpResponse.code() >= 400) {
                ANResponse response = new ANResponse<>(Utils.getErrorForServerResponse(new ANError(okHttpResponse),
                        request, okHttpResponse.code()));
                response.setOkHttpResponse(okHttpResponse);
                return response;
            }
            ANResponse response = request.parseResponse(okHttpResponse);
            response.setOkHttpResponse(okHttpResponse);
            return response;
        } catch (ANError se) {
            return new ANResponse<>(Utils.getErrorForConnection(new ANError(se)));
        } catch (Exception e) {
            return new ANResponse<>(Utils.getErrorForConnection(new ANError(e)));
        } finally {
            SourceCloseUtil.close(okHttpResponse, request);
        }
    }

    private static <T> ANResponse<T> executeDownloadRequest(ANRequest request) {
        Response okHttpResponse;
        try {
            okHttpResponse = InternalNetworking.performDownloadRequest(request);
            if (okHttpResponse == null) {
                return new ANResponse<>(Utils.getErrorForConnection(new ANError()));
            }
            if (okHttpResponse.code() >= 400) {
                ANResponse response = new ANResponse<>(Utils.getErrorForServerResponse(new ANError(okHttpResponse),
                        request, okHttpResponse.code()));
                response.setOkHttpResponse(okHttpResponse);
                return response;
            }
            ANResponse response = new ANResponse(ANConstants.SUCCESS);
            response.setOkHttpResponse(okHttpResponse);
            return response;
        } catch (ANError se) {
            return new ANResponse<>(Utils.getErrorForConnection(new ANError(se)));
        } catch (Exception e) {
            return new ANResponse<>(Utils.getErrorForConnection(new ANError(e)));
        }
    }

    private static <T> ANResponse<T> executeUploadRequest(ANRequest request) {
        Response okHttpResponse = null;
        try {
            okHttpResponse = InternalNetworking.performUploadRequest(request);

            if (okHttpResponse == null) {
                return new ANResponse<>(Utils.getErrorForConnection(new ANError()));
            }

            if (request.getResponseAs() == ResponseType.OK_HTTP_RESPONSE) {
                ANResponse response = new ANResponse(okHttpResponse);
                response.setOkHttpResponse(okHttpResponse);
                return response;
            }
            if (okHttpResponse.code() >= 400) {
                ANResponse response = new ANResponse<>(Utils.getErrorForServerResponse(new ANError(okHttpResponse),
                        request, okHttpResponse.code()));
                response.setOkHttpResponse(okHttpResponse);
                return response;
            }
            ANResponse response = request.parseResponse(okHttpResponse);
            response.setOkHttpResponse(okHttpResponse);
            return response;
        } catch (ANError se) {
            return new ANResponse<>(Utils.getErrorForConnection(se));
        } catch (Exception e) {
            return new ANResponse<>(Utils.getErrorForConnection(new ANError(e)));
        } finally {
            SourceCloseUtil.close(okHttpResponse, request);
        }
    }
}

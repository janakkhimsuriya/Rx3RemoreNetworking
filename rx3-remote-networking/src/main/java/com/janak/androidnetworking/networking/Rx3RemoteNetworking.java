package com.janak.androidnetworking.networking;

public class Rx3RemoteNetworking {

    /**
     * private constructor to prevent instantiation of this class
     */
    private Rx3RemoteNetworking() {
    }

    /**
     * Method to make GET request
     *
     * @param url The url on which request is to be made
     * @return The GetRequestBuilder
     */
    public static Rx3ANRequest.GetRequestBuilder get(String url) {
        return new Rx3ANRequest.GetRequestBuilder(url);
    }

    /**
     * Method to make HEAD request
     *
     * @param url The url on which request is to be made
     * @return The HeadRequestBuilder
     */
    public static Rx3ANRequest.HeadRequestBuilder head(String url) {
        return new Rx3ANRequest.HeadRequestBuilder(url);
    }

    /**
     * Method to make OPTIONS request
     *
     * @param url The url on which request is to be made
     * @return The OptionsRequestBuilder
     */
    public static Rx3ANRequest.OptionsRequestBuilder options(String url) {
        return new Rx3ANRequest.OptionsRequestBuilder(url);
    }

    /**
     * Method to make POST request
     *
     * @param url The url on which request is to be made
     * @return The PostRequestBuilder
     */
    public static Rx3ANRequest.PostRequestBuilder post(String url) {
        return new Rx3ANRequest.PostRequestBuilder(url);
    }

    /**
     * Method to make PUT request
     *
     * @param url The url on which request is to be made
     * @return The PutRequestBuilder
     */
    public static Rx3ANRequest.PutRequestBuilder put(String url) {
        return new Rx3ANRequest.PutRequestBuilder(url);
    }

    /**
     * Method to make DELETE request
     *
     * @param url The url on which request is to be made
     * @return The DeleteRequestBuilder
     */
    public static Rx3ANRequest.DeleteRequestBuilder delete(String url) {
        return new Rx3ANRequest.DeleteRequestBuilder(url);
    }

    /**
     * Method to make PATCH request
     *
     * @param url The url on which request is to be made
     * @return The PatchRequestBuilder
     */
    public static Rx3ANRequest.PatchRequestBuilder patch(String url) {
        return new Rx3ANRequest.PatchRequestBuilder(url);
    }

    /**
     * Method to make download request
     *
     * @param url      The url on which request is to be made
     * @param dirPath  The directory path on which file is to be saved
     * @param fileName The file name with which file is to be saved
     * @return The DownloadBuilder
     */
    public static Rx3ANRequest.DownloadBuilder download(String url, String dirPath, String fileName) {
        return new Rx3ANRequest.DownloadBuilder(url, dirPath, fileName);
    }

    /**
     * Method to make upload request
     *
     * @param url The url on which request is to be made
     * @return The MultiPartBuilder
     */
    public static Rx3ANRequest.MultiPartBuilder upload(String url) {
        return new Rx3ANRequest.MultiPartBuilder(url);
    }

    /**
     * Method to make Dynamic request
     *
     * @param url    The url on which request is to be made
     * @param method The HTTP METHOD for the request
     * @return The DynamicRequestBuilder
     */
    public static Rx3ANRequest.DynamicRequestBuilder request(String url, int method) {
        return new Rx3ANRequest.DynamicRequestBuilder(url, method);
    }
}
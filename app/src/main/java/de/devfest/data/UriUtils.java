package de.devfest.data;

import android.support.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public class UriUtils {

    static final String URL_PREFIX_WWW = "www.";
    static final String URL_SCHEME_HTTP = "http://";
    static final String URL_SCHEME_HTTPS = "https://";

    public static String ensureScheme(@NonNull String uri) {
        if (!uri.startsWith(URL_SCHEME_HTTP) && !uri.startsWith(URL_SCHEME_HTTPS)) {
            uri = URL_SCHEME_HTTP + uri;
        }
        return uri;
    }


    public static String stripWWW(@NonNull String authority) {
        if (authority.startsWith(URL_PREFIX_WWW)) authority = authority.replace(URL_PREFIX_WWW, "");
        return authority;
    }
}

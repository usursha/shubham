package com.hpy.sampatti_data_service.service;
import javax.net.ssl.HostnameVerifier;

import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier {

// @Override

// public boolean verify(String hostname, SSLSession session) {

// return "localhost".equals(hostname) || "127.0.0.1".equals(session);

// }

@Override

public boolean verify(String hostname, SSLSession session) {

// Add the hostname(s) or IP address(es) of your server here

// Check if the provided hostname is acceptable

return "ftweb.hitachi-payments.com".equals(hostname);//|| "172.18.78.119".equals(hostname);

}

}
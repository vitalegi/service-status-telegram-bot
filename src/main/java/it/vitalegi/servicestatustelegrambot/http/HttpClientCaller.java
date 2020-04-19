package it.vitalegi.servicestatustelegrambot.http;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpClientCaller {

	HttpResponseWrapper call(HttpPost httpRequest) throws IOException;

	HttpResponseWrapper call(HttpGet httpRequest) throws IOException;

	HttpResponseWrapper doCall(HttpRequestBase request) throws IOException;
}
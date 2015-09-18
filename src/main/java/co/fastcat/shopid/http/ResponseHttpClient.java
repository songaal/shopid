package co.fastcat.shopid.http;

import co.fastcat.shopid.controller.InvalidAuthenticationException;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.jdom2.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResponseHttpClient {
	private static Logger logger = LoggerFactory.getLogger(ResponseHttpClient.class);

	private CloseableHttpClient httpclient;
	private String urlPrefix;
	private boolean isActive;
	private String host;
	
	private static final ResponseHandler<JSONObject> jsonResponseHandler = new JSONResponseHandler();
	private static final ResponseHandler<Document> xmlResponseHandler = new XMLResponseHandler();
	private static final ResponseHandler<String> textResponseHandler = new TextResponseHandler();

    public ResponseHttpClient(String host) {
        this(host, 0);
    }

	public ResponseHttpClient(String host, int timeout) {

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(cm).setDefaultCookieStore(cookieStore);
        if(timeout > 0) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout * 1000)
                    .setConnectTimeout(timeout * 1000)
                    .build();

            clientBuilder = clientBuilder.setDefaultRequestConfig(requestConfig);
        }
		httpclient = clientBuilder.build();

        this.host = host;
		if(host != null){
			urlPrefix = "http://" + host;
		}else{
			urlPrefix = "";
		}
		isActive = true;

	}

	public String getHostString(){
		return host;
	}
	public boolean isActive() {
		return isActive;
	}

	private String getURL(String uri) {
		return urlPrefix + uri;
	}

	public GetMethod httpGet(String uri) {
		return new GetMethod(this, getURL(uri));
	}

	public PostMethod httpPost(String uri) {
		return new PostMethod(this, getURL(uri));
	}

	public void close() {
		if (httpclient != null) {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error("error close httpclient", e);
			}
			httpclient = null;
		}
		isActive = false;
	}

	public static abstract class AbstractMethod {
		protected ResponseHttpClient responseHttpClient;
		protected String url;

		public AbstractMethod(ResponseHttpClient responseHttpClient, String url) {
			this.responseHttpClient = responseHttpClient;
			this.url = url;
		}


		public abstract String getQueryString();

		public abstract AbstractMethod addParameter(String key, String value);
		
		public abstract AbstractMethod addParameters(List<NameValuePair> nvps);

		protected abstract HttpUriRequest getHttpRequest();

		public AbstractMethod addParameterString(String parameterString) {
			String[] keyValues = parameterString.split("&");
			for (String keyValue : keyValues) {
				keyValue = keyValue.trim();
				if (keyValue.length() > 0) {
					String[] list = keyValue.split("=");
					if (list.length == 2) {
						addParameter(list[0], list[1]);
					}
				}
			}

			return this;
		}

		public JSONObject requestJSON() throws ClientProtocolException, IOException, Http404Error {
			HttpUriRequest httpUriRequest = null;
			try {
				httpUriRequest = getHttpRequest();
				JSONObject obj = responseHttpClient.httpclient.execute(httpUriRequest, jsonResponseHandler);

				checkAuthorizedMessage(obj);

				return obj;
			} catch (SocketException e) {
				logger.error("httpclient socket error! >> {}", e.getMessage());
				responseHttpClient.close();
			} catch (ClientProtocolException e) {
				if (e.getCause() instanceof Http404Error) {
					throw (Http404Error) e.getCause();
				}
				logger.error("error while request > {}", httpUriRequest);
				logger.error("httpclient error! >> {}, {}", e.getMessage(), e.getCause());
				throw e;
			}
			return null;
		}

		public Document requestXML() throws ClientProtocolException, IOException, Http404Error {
			try {
				return responseHttpClient.httpclient.execute(getHttpRequest(), xmlResponseHandler);
			} catch (SocketException e) {
				logger.debug("httpclient socket error! >> {}", e.getMessage());
				responseHttpClient.close();
			} catch (ClientProtocolException e) {
				if (e.getCause() instanceof Http404Error) {
					throw (Http404Error) e.getCause();
				}
				logger.debug("httpclient error! >> {}, {}", e.getMessage(), e.getCause());
				throw e;
			}
			return null;
		}

		public String requestText() throws ClientProtocolException, IOException, Http404Error {
			try {
				return responseHttpClient.httpclient.execute(getHttpRequest(), textResponseHandler);
			} catch (SocketException e) {
				logger.debug("httpclient socket error! >> {}", e.getMessage());
				responseHttpClient.close();
			} catch (ClientProtocolException e) {
				if (e.getCause() instanceof Http404Error) {
					throw (Http404Error) e.getCause();
				}
				logger.debug("httpclient error! >> {}", e.getMessage());
				throw e;
			}
			return null;
		}

		private void checkAuthorizedMessage(Object obj) {
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;
				logger.trace("jsonobj:{}", jsonObj.optString("error"));
				if ("Not Authenticated.".equals(jsonObj.optString("error"))) {
					logger.trace("throwing exception...");
					throw new InvalidAuthenticationException();
				}
			}
		}
	}

	public static class GetMethod extends AbstractMethod {

		private String queryString;

		public GetMethod(ResponseHttpClient responseHttpClient, String url) {
			super(responseHttpClient, url);
		}

		protected HttpGet getHttpRequest() {
			if (queryString != null) {
				return new HttpGet(url + "?" + queryString);
			} else {
				return new HttpGet(url);
			}
		}

		@Override
		public GetMethod addParameter(String key, String value) {
			try {
				if (value == null) {
					value = "";
				}

				if (queryString == null) {
					queryString = "";
				} else {
					queryString += "&";
				}
				queryString += (key + "=" + URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
			}

			return this;
		}
		
		@Override
		public GetMethod addParameters(List<NameValuePair> nvps) {
			for(NameValuePair nvp : nvps) {
				addParameter(nvp.getName(), nvp.getValue());
			}
			return this;
		}

		@Override
		public String getQueryString() {
			if (queryString != null) {
				return queryString;
			} else {
				return "";
			}
		}
	}

	public static class PostMethod extends AbstractMethod {
		private List<NameValuePair> nvps;

		public PostMethod(ResponseHttpClient responseHttpClient, String url) {
			super(responseHttpClient, url);
		}

		@Override
		protected HttpPost getHttpRequest() {
			HttpPost httpost = new HttpPost(url);
			if (nvps != null) {
				httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			}
			return httpost;
		}

		@Override
		public String getQueryString() {
			if (nvps != null) {
				return URLEncodedUtils.format(nvps, Consts.UTF_8);
			} else {
				return "";
			}
		}

		@Override
		public PostMethod addParameter(String key, String value) {
			if (nvps == null) {
				nvps = new ArrayList<NameValuePair>();
			}

			nvps.add(new BasicNameValuePair(key, value));

			return this;
		}
		
		@Override
		public PostMethod addParameters(List<NameValuePair> nvps) {
			if (nvps == null) {
				nvps = new ArrayList<NameValuePair>();
			}
			
			nvps.addAll(nvps);
			
			return this;
		}

		public String getParameter(String key) {
			if (nvps != null) {
				for (NameValuePair pair : nvps) {
					if (pair.getName().equalsIgnoreCase(key)) {
						return pair.getValue();
					}
				}
			}

			return null;
		}

	}

}

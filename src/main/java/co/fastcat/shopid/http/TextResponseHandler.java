package co.fastcat.shopid.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TextResponseHandler extends BasicResponseHandler {
	private static Logger logger = LoggerFactory.getLogger(TextResponseHandler.class);

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if(status == 404){
			//NOT FOUND URL
			throw new ClientProtocolException(new Http404Error());
		} else {
			return super.handleResponse(response);
		}
	}

}

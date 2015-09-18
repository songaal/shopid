package co.fastcat.shopid.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONResponseHandler implements ResponseHandler<JSONObject> {
	private static Logger logger = LoggerFactory.getLogger(JSONResponseHandler.class);

	@Override
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// logger.debug("Raw JSON len = {}", entity.getContentLength());
				String jsonString = EntityUtils.toString(entity);
				// logger.debug("Raw JSON >>{}<<", jsonString);
				return new JSONObject(jsonString);
			} else {
				return new JSONObject();
			}
		}else if(status == 404){
			//NOT FOUND URL
			throw new ClientProtocolException(new Http404Error());
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}

}

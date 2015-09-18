package co.fastcat.shopid.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class XMLResponseHandler implements ResponseHandler<Document> {
	private static Logger logger = LoggerFactory.getLogger(XMLResponseHandler.class);

	@Override
	public Document handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream is = entity.getContent();
				try {
					SAXBuilder saxBuilder = new SAXBuilder();
					return saxBuilder.build(is);
				} catch (Exception e) {
					logger.error("xml parsing error", e);
				} finally {
					if(is != null){
						is.close();
					}
				}
			}
			return null;
		}else if(status == 404){
			//NOT FOUND URL
			throw new ClientProtocolException(new Http404Error());
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}

}

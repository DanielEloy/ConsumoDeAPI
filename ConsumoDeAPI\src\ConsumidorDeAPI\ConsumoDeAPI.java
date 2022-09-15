/*
 * Aprofundando os estudos de consumo de uma API em JAVA
 */
package ConsumidorDeAPI;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// @autor Daniel Eloy

public class ConsumoDeAPI {

	private static String URLBase = "http://api.olhovivo.sptrans.com.br/v2.1/";
	private static String token = "XXXXXXXX";	// Deverá ser colocado o token fornecido pelo OLHO vivo SPTRANS
	private static ConsumoDeAPI instancia;
	private CloseableHttpClient clienteHTTP;
	Logger logger = Logger.getLogger(ConsumoDeAPI.class.getName());
	
	private ConsumoDeAPI() {
		this.clienteHTTP = HttpClients.createDefault();
	}

	public static ConsumoDeAPI getInstancia() {
		if (instancia == null) {
			instancia = new ConsumoDeAPI();
		}
		return instancia;
	}

	public void doLogin() {
		try {
			HttpPost httpPost = new HttpPost(ConsumoDeAPI.URLBase + "Login/Autenticar?token=" + ConsumoDeAPI.token);
//			logger.info(httpPost.toString());
			
//			Criando um Handler ou manipulador de resposta customizado
//			para recuperar o conteudo da resposta e não apenar o código http da resposta
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Status de resposta inesperado: " + status);
					}
				}
			};
			String responseBody = this.clienteHTTP.execute(httpPost, responseHandler);
			logger.info("Conectado: " + responseBody);

		} catch (IOException ex) {
			Logger.getLogger(ConsumoDeAPI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

//	Método para realizar requisições para API
//	Lembrando que primeiro deve ser executado o método do login para realizar a autenticação
//	Este método recebe parte da URL que será executada, pois AURL base já é um atributo estático desta classe.
	public String doRequest(String path) {
		String responseBody = null;
		logger.info("path: "+ path);

		try {
			HttpGet httpGet = new HttpGet(ConsumoDeAPI.URLBase + path);

//			Criando um handler ou manipulador de resposta customizado
//			Pois queremos recuperar o conteúdo da resposta e não apenas o código http da resposta
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Status de resposta inesperado: " + status);
					}
				}
			};
			responseBody = this.clienteHTTP.execute(httpGet, responseHandler);
			
		} catch (IOException ex) {
			Logger.getLogger(ConsumoDeAPI.class.getName()).log(Level.SEVERE, null, ex);
		}
		return responseBody;
	}
}

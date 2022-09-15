package ConsumidorDeAPI;

import java.util.logging.Logger;

// @ autor Daniel Eloy
public class TratativaDeChamadas {
	
	public static void main(String args[]) {
		ConsumoDeAPI consumidor = ConsumoDeAPI.getInstancia();
		Logger logger = Logger.getLogger(TratativaDeChamadas.class.getName());
		
		consumidor.doLogin();
		logger.info(consumidor.doRequest("Posicao"));
	}
}

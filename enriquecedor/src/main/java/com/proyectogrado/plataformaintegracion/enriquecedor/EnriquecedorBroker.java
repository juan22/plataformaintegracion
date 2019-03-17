package com.proyectogrado.plataformaintegracion.enriquecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;


@EnableBinding(EnriquecedorProcesador.class)
public class EnriquecedorBroker {
	
	@Autowired
	IEnriquecedorLogica enriquecedorLogica;
	
	@Autowired
	Environment env;
	
	@Autowired
	EnriquecedorProcesador enriquecedorProcesador;
	
	private Logger logger = LoggerFactory.getLogger(EnriquecedorBroker.class);
	
	@StreamListener(target = "enriquecedorSubscribableChannel")
	public void receive(Message<String> message) {
		try {
			Message<String> messageResultado;
			messageResultado = enriquecedorLogica.enriquecerMensaje(message);
			enriquecedorProcesador.enriquecedorMessages().send(messageResultado);
	        logger.info("Se ejecutó ENRIQUECEDOR exitosamente");
		}catch(Exception ex) {
			logger.error("ERROR EN ENRIQUECEDOR: "+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			enriquecedorProcesador.enriquecedorMessagesErrores().send(messageResultado);
		}
	}

}

package com.proyectogrado.plataformaintegracion.transformacionxmljson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;


@EnableBinding(TransformacionProcesador.class)
public class TransformacionBroker {
		
	@Autowired
	ITransformacionLogica transformacionLogica;
	
	@Autowired
	Environment env;
	
	@Autowired
	TransformacionProcesador transformacionProcesador;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionBroker.class);
		
	@StreamListener(target = "transformacionSubscribableChannel")
	public void receive(Message<String> message) {
		try {
			Message<String> messageResultado;
			messageResultado = transformacionLogica.procesamientoTransformacion(message);
	        transformacionProcesador.transformacionMessages().send(messageResultado);
	        logger.info("Se ejecutó TRANSFORMADOR exitosamente");
		}catch(Exception ex) {
			logger.error("ERROR EN TRANSFORMADOR: "+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			transformacionProcesador.transformacionMessages().send(messageResultado);
		}
	}
	
}

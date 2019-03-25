package com.proyectogrado.plataformaintegracion.conectorSalida;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;


@EnableBinding(ConectorSink.class)
public class ConectorBroker {
	
	@Autowired
	Environment env;
	
	@Autowired
	ConectorSink conectorSink;
	
	@Autowired
	ServicioParametrosRequestSOAP servicioParametrosRequestSOAP;
	
	@Autowired
	IConectorLogica conectorLogica;
		
	private Logger logger = LoggerFactory.getLogger(ConectorBroker.class);

	@StreamListener(target = "conectorSalidaSubscribableChannel")
	public void receive(Message<String> message){
		try {
			Message<String> mensaje = conectorLogica.procesamientoConector(message);					
			conectorSink.conectorSalidaMessages().send(mensaje);
			logger.info("Se ejecutó CONECTORSALIDA exitosamente");
		}catch(Exception ex) {
			logger.error("ERROR EN CONECTORSALIDA:"+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			conectorSink.conectorSalidaErrors().send(messageResultado);
		}
	}
	
}

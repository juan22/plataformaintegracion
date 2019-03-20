package com.proyectogrado.plataformaintegracion.enriquecedor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.MensajeCanonicoUtils;

@RestController
public class EnriquecedorController {
	
	@Autowired
	IEnriquecedorLogica enriquecedorLogica;
	
	private Logger logger = LoggerFactory.getLogger(EnriquecedorController.class);
	
	@RequestMapping(value = "/ejecutar", method = RequestMethod.POST)
	public String ejecutarEnricher(@RequestBody String mensaje){
		Message<String> messageResultado;
		
		Map<String,String> headers = MensajeCanonicoUtils.obtenerListaHeadersMensajeCanonico(mensaje);
		String contenidoMensaje = MensajeCanonicoUtils.obtenerPayloadMensajeCanonico(mensaje);
		
		Message<String> message = crearMensajeSpring(headers, contenidoMensaje);
		
		try {
			messageResultado = enriquecedorLogica.enriquecerMensaje(message);
			logger.info("Se ejecutó ENRIQUECEDOR exitosamente");
			headers = obtenerHeadersMensajeSpring(messageResultado);
			headers.put("status", "200");
//			MessageHeaders headersMessageResult = messageResultado.getHeaders();
//			String basicAuth = (String) headersMessageResult.get("authorization");
//			headers.put("authorization", basicAuth);
		} catch (Exception ex) {
			logger.error("ERROR EN ENRIQUECEDOR: "+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			headers.put("status", "550");
		}
		return MensajeCanonicoUtils.crearMensajeCanonico(headers, messageResultado.getPayload());
	}
	
	private Message<String> crearMensajeSpring(Map<String,String> headers, String contenidoMensaje){
		MessageBuilder<String> builder = MessageBuilder.withPayload(contenidoMensaje);
		for(Map.Entry<String, String> header : headers.entrySet()){
			builder = builder.setHeader(header.getKey(), header.getValue());
		}
		return builder.build();
	}
	
	private Map<String,String> obtenerHeadersMensajeSpring(Message<String> mensaje){
		Map<String,String> headers = new HashMap<>();
		for(Map.Entry<String, Object> header :	mensaje.getHeaders().entrySet()) {
			if (header.getValue() instanceof String) {
				headers.put(header.getKey(), (String) header.getValue());
			}
		}
		return headers;
	}

}

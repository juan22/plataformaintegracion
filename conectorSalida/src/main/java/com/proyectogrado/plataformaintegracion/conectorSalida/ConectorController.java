package com.proyectogrado.plataformaintegracion.conectorSalida;

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
import utils.MensajeSpringUtils;

@RestController
public class ConectorController {
	
	@Autowired
	IConectorLogica conectorLogica;
	
	private Logger logger = LoggerFactory.getLogger(ConectorController.class);
	
	@RequestMapping(value = "/ejecutar", method = RequestMethod.POST)
	public String ejecutarTrans(@RequestBody String mensaje){
		Message<String> messageResultado;
		
		Map<String,String> headers = MensajeCanonicoUtils.obtenerListaHeadersMensajeCanonico(mensaje);
		String contenidoMensaje = MensajeCanonicoUtils.obtenerPayloadMensajeCanonico(mensaje);
		
		Message<String> message = MensajeSpringUtils.crearMensajeSpring(headers, contenidoMensaje);
		
		try {
			messageResultado = conectorLogica.procesamientoConector(message);
			logger.info("Se ejecutó CONECTOR2 exitosamente");
			headers.put("status", "200");
		}catch(Exception ex) {
			logger.error("ERROR EN CONECTOR2:"+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			headers.put("status", "550");
		}
		return MensajeCanonicoUtils.crearMensajeCanonico(headers, messageResultado.getPayload());
	}
	
}

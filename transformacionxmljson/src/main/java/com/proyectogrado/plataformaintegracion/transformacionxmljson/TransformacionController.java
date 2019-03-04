package com.proyectogrado.plataformaintegracion.transformacionxmljson;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.MensajeCanonicoUtils;

@RestController
public class TransformacionController {
	
	@Autowired
	ITransformacionLogica transformacionLogica;
		
	private Logger logger = LoggerFactory.getLogger(TransformacionController.class);
		
	@RequestMapping(value = "/ejecutar", method = RequestMethod.POST)
	public String ejecutarTrans(@RequestBody String contentMessage, @RequestHeader String idSol, @RequestHeader String paso){
		Message<String> messageResultado;
		Map<String,String> headers = new HashMap<>();
		Message<String> message = MessageBuilder.withPayload(contentMessage).setHeader("idSol", idSol).setHeader("paso", new Integer(paso)).build();
		try {
			messageResultado = transformacionLogica.procesamientoTransformacion(message);
			logger.info("Se ejecutó TRANSFORMADOR exitosamente");
			headers.put("status", "200");
		} catch (Exception ex) {
			logger.error("ERROR EN TRANSFORMADOR: "+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			headers.put("status", "550");
		}
		return MensajeCanonicoUtils.crearMensajeCanonico(headers, messageResultado.getPayload());
	}

}

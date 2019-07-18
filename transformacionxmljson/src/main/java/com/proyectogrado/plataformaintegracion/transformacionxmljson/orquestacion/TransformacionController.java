package com.proyectogrado.plataformaintegracion.transformacionxmljson.orquestacion;

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

import com.proyectogrado.plataformaintegracion.transformacionxmljson.interfaces.ITransformacionLogica;

import utils.MensajeCanonicoUtils;
import utils.MensajeSpringUtils;

@RestController
public class TransformacionController {
	
	@Autowired
	ITransformacionLogica transformacionLogica;
		
	private Logger logger = LoggerFactory.getLogger(TransformacionController.class);
		
	@RequestMapping(value = "/ejecutar", method = RequestMethod.POST)
	public String ejecutarTrans(@RequestBody String mensaje){
		Message<String> messageResultado;
		
		Map<String,String> headers = MensajeCanonicoUtils.obtenerListaHeadersMensajeCanonico(mensaje);
		String contenidoMensaje = MensajeCanonicoUtils.obtenerPayloadMensajeCanonico(mensaje);
		
		Message<String> message = MensajeSpringUtils.crearMensajeSpring(headers, contenidoMensaje);
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

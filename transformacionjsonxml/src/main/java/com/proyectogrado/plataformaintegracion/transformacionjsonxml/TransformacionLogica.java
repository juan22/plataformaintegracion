package com.proyectogrado.plataformaintegracion.transformacionjsonxml;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@Service
public class TransformacionLogica implements ITransformacionLogica {
	
	@Autowired
	Environment env;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionLogica.class);
	
	public Message<String> procesamientoTransformacion(Message<String> message) throws Exception {
		logger.info("Mensaje recibido en el Transformador: "+message.toString());
		int numero = (int) (Math.random() * 100);
		logger.info("VAMOS A TRANSFORMAR!! El numero aleatorio es:"+numero);
		Message<String> messageResultado;
		if (numero > 80) {
			logger.error("El transformador dio error!!");
			throw new Exception("Error por número aleatorio!!");
		}
		String result = transformarJsonToXml(message.getPayload());
		logger.info("Resultado de Transformacion: "+result);
        messageResultado = (Message<String>) MessageBuilder.withPayload(result).copyHeaders(message.getHeaders()).build();
        return messageResultado;
	}

	private String transformarJsonToXml(String texto) throws Exception{
		JSONObject json = new JSONObject(texto);
		return XML.toString(json);
    }
	
}

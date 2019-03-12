package com.proyectogrado.plataformaintegracion.enriquecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Service
public class EnriquecedorLogica implements IEnriquecedorLogica {
	
	@Autowired
	Environment env;
	
	private Logger logger = LoggerFactory.getLogger(EnriquecedorLogica.class);
	
	public Message<String> enriquecerMensaje(Message<String> message){
		logger.info("Mensaje recibido en el Enriquecedor: "+message.toString());
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idSol");
		Integer paso = (Integer) headers.get("paso");
		StringBuffer enricherUser = new StringBuffer("enricher.");
		enricherUser.append(idSol).append(".paso").append(paso).append(".usuario");
    	String enricherUsuario = env.getProperty(enricherUser.toString());
    	StringBuffer enricherPass = new StringBuffer("enricher.");
    	enricherPass.append(idSol).append(".paso").append(paso).append(".password");
    	String enricherContrasenia = env.getProperty(enricherPass.toString());
		String basicAuth = obtenerCadenaBasicAuth(enricherUsuario, enricherContrasenia);
		logger.info("Resultado de Enriquecedor: "+basicAuth);
        paso = paso + 1;
        Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders()).setHeader("paso", paso).setHeader("Authorization", basicAuth).build();
        return messageResultado;
	}

	private String obtenerCadenaBasicAuth(String enricherUsuario, String enricherContrasenia) {		
		try {
			String encoded = enricherUsuario+":"+enricherContrasenia;
			String encoding = Base64.getEncoder().encodeToString((encoded).getBytes("UTF-8"));
			return "Basic "+encoding;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}		
	}

}

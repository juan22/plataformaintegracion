package utils;

import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.support.MessageBuilder;

public interface MensajeSpringUtils {
	
	public static Message<String> crearMensajeSpring(Map<String,String> headers, String contenidoMensaje){
		MessageBuilder<String> builder = MessageBuilder.withPayload(contenidoMensaje);
		for(Map.Entry<String, String> header : headers.entrySet()){
			builder = builder.setHeader(header.getKey(), header.getValue());
		}
		return builder.build();
	}
	
	public static Map<String,String> obtenerHeadersMensajeSpring(Message<String> mensaje){
		Map<String,String> headers = new HashMap<>();
		for(Map.Entry<String, Object> header :	mensaje.getHeaders().entrySet()) {
			if (header.getValue() instanceof String) {
				headers.put(header.getKey(), (String) header.getValue());
			}
		}
		return headers;
	}

}

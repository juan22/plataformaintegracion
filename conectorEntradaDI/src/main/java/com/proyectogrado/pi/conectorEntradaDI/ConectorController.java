package com.proyectogrado.pi.conectorEntradaDI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@EnableIntegration
@Component
public class ConectorController {
	
	@Autowired
	Environment env;
	
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;
	
	private Logger logger = LoggerFactory.getLogger(ConectorController.class);
	
	private Map<String, Object> replyChannelMap = new ConcurrentHashMap<String, Object>();

    public String captureReplyChannel(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Object channel = headers.getReplyChannel();
        String correlationID = (String) headers.get("X-B3-TraceId");
        String idMensaje = (String) headers.get("idmensaje");
        logger.info("idmensaje: "+idMensaje);
        MDC.put( "idmensaje", idMensaje);
        logger.info("request: "+message.getPayload().toString());
        logger.info("idcorrelacion: "+correlationID);
        replyChannelMap.put(correlationID, channel);
        return correlationID;
    }

    public Object getReplyChannel(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Object correlationID = headers.get("idcorrelacion");        
        if (correlationID == null || ! (correlationID instanceof String))
            return null;

        String id = (String) correlationID;        
        
        logger.info("response correlationID: "+correlationID);
        logger.info("response: "+message.getPayload().toString());
        
        Object channel = replyChannelMap.remove(id);
        return channel;
    }
    
    public String setTipoComposicion(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        logger.info("headers "+headers.toString());
        String idSol = (String) headers.get("idsol");
        
        StringBuffer tipoComposicionProperty = new StringBuffer("solucion.").append(idSol).append(".tipoComposicion");
		String tipoComposicion = env.getProperty(tipoComposicionProperty.toString());
        
		logger.info("El tipo de composicion para la solucion "+idSol+" es :"+tipoComposicion);
		return tipoComposicion;
    }
    

    public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl: "http://" + serviceUrl;
	}

	public String ejecutarServicioOrquestacion(Message<?> message){
		setServiceUrl("orquestador");
		MessageHeaders headers = message.getHeaders();
        logger.info("orquesta headers "+headers.toString());
        String payload = message.getPayload().toString();
        logger.info("orquesta payload: "+payload);
        
		HttpHeaders h = new HttpHeaders();
		h.add("idsol",(String) headers.get("idsol"));
		h.add("idmensaje", (String) headers.get("idmensaje"));
		h.add("idcorrelacion", (String) headers.get("idcorrelacion"));
		HttpEntity<String> request = new HttpEntity<String>(payload, h);
		ResponseEntity<String> response = restTemplate.exchange(serviceUrl + "/orquestador/ejecutarSolucion", HttpMethod.POST, request, String.class);
		
		return response.getBody();
	}
    
    
}

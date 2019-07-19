package com.proyectogrado.plataformaintegracion.orquestador;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import utils.MensajeCanonicoUtils;

@RestController
public class OrquestadorController {
	
	@Autowired
	Environment env;
	
	@Autowired
	ClientOrquestadorService clientOrquestador;
	
	private Logger logger = LoggerFactory.getLogger(OrquestadorController.class);
		
	@RequestMapping("/orquestador/ejecutarSolucion")
    @ResponseBody
	public String ejecutarSolucion(@RequestBody String contenidoMensaje, @RequestHeader String idsol,
			@RequestHeader String idmensaje) throws Exception {
		try {
			MDC.put( "idmensaje", idmensaje);
			StringBuffer itinerarioPrpty = new StringBuffer("solucion.");
			itinerarioPrpty.append(idsol).append(".itinerario");
	        String itinerarioSolucion = env.getProperty(itinerarioPrpty.toString());
	        String[] listaItinerario = itinerarioSolucion.split(",");
	        Map<String,String> headers = new HashMap<>();
	        headers.put("idsol", idsol);
	        headers.put("idmensaje", idmensaje);
	        String mensajeCanonico = MensajeCanonicoUtils.crearMensajeCanonico(headers, contenidoMensaje);
	        for (int i = 0; i< listaItinerario.length; i++) {
	        	String nombreMicroservicio = listaItinerario[i];
	        	clientOrquestador.setServiceUrl(nombreMicroservicio);   	
	        	
	        	try {
	        		mensajeCanonico = clientOrquestador.ejecutarServicio(mensajeCanonico);
	        	}catch (Exception e) {
	        		logger.error("ERROR EN ORQUESTADOR: El componente "+nombreMicroservicio+" dió el error: "+e.getMessage());
	    			return "No se pudo ejecutar la operación. Vuelva a intentarlo más tarde.";
				}
	        	
	        	String statusResponse = MensajeCanonicoUtils.obtenerHeaderMensajeCanonico("status", mensajeCanonico);
	    		logger.info("STATUS MENSAJE="+statusResponse);
	    			    		
	    		if (!"200".equals(statusResponse)) {
	    			contenidoMensaje = MensajeCanonicoUtils.obtenerPayloadMensajeCanonico(mensajeCanonico);
	    			logger.error("ERROR EN ORQUESTADOR: El componente "+nombreMicroservicio+" respondió con código de error "+statusResponse);
	    			return contenidoMensaje;
	    		}	    		
	        }
	        contenidoMensaje = MensajeCanonicoUtils.obtenerPayloadMensajeCanonico(mensajeCanonico);
			return contenidoMensaje;
		}catch(Exception ex) {
			logger.error("ERROR EN ORQUESTADOR: "+ex.getMessage());
			return "No se pudo ejecutar la operación. Vuelva a intentarlo más tarde.";
		}finally {
			MDC.remove("idmensaje");
		}
		
	}

}

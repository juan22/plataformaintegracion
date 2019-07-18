package com.proyectogrado.plataformaintegracion.conectorSalida.implementacion;

import org.springframework.stereotype.Service;

import com.proyectogrado.plataformaintegracion.conectorSalida.interfaces.IServicioParametrosRequest;

@Service
public class ServicioParametrosRequestSOAP implements IServicioParametrosRequest {
	
	public String obtenerUnParametro(String param, String mensajeXML) throws Exception {
		if (mensajeXML.contains(param)) {
			String separador1 = "<"+param+">";
			String separador2 = "</"+param+">";
			String[] arr1 = mensajeXML.split(separador1);
			String[] arr2 = arr1[1].split(separador2);
			return arr2[0];
		}
		throw new Exception("Parámetro no encontrado en mensaje XML del request");
	}

}

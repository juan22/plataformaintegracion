package com.proyectogrado.plataformaintegracion.conectorSalida.interfaces;

import org.springframework.messaging.Message;

public interface IConectorLogica {
	
	Message<String> procesamientoConector(Message<String> message) throws Exception;

}

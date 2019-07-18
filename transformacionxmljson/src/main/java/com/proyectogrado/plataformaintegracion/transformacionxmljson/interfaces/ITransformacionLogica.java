package com.proyectogrado.plataformaintegracion.transformacionxmljson.interfaces;

import org.springframework.messaging.Message;

public interface ITransformacionLogica {
	
	Message<String> procesamientoTransformacion(Message<String> message) throws Exception;

}

package com.proyectogrado.plataformaintegracion.transformacionjsonxml.interfaces;

import org.springframework.messaging.Message;

public interface ITransformacionLogica {

	Message<String> procesamientoTransformacion(Message<String> message) throws Exception;

}
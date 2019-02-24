package com.proyectogrado.pi.transformacion;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

public interface ITransformacionLogica {
	
	Message<String> procesamientoTransformacion(Message<String> message) throws Exception;

}

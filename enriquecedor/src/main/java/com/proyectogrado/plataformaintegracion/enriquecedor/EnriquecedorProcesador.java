package com.proyectogrado.plataformaintegracion.enriquecedor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface EnriquecedorProcesador {
	
	@Input("enriquecedorSubscribableChannel")
	SubscribableChannel enriquecedorSubscribable();
	
	@Output("enriquecedorMessagesChannel")
	MessageChannel enriquecedorMessages();
	
	@Output("enriquecedorMessagesChannelErrores")
	MessageChannel enriquecedorMessagesErrores();

}

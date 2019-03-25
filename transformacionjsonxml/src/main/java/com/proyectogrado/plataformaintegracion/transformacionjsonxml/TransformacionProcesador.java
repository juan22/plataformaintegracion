package com.proyectogrado.plataformaintegracion.transformacionjsonxml;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface TransformacionProcesador {
	
	@Input("transformacionSubscribableChannel")
	SubscribableChannel transformacionSubscribable();
		
	@Output("transformacionMessagesChannel")
	MessageChannel transformacionMessages();
	
	@Output("transformacionMessagesChannelErrores")
	MessageChannel transformacionMessagesErrores();

}

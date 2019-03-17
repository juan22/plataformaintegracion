package com.proyectogrado.plataformaintegracion.conectorSalida;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ConectorSink {
	
	@Input("conectorSalidaSubscribableChannel")
	SubscribableChannel conectorSalidaSubscribable();
	
	@Output("conectorSalidaMessagesChannel")
	MessageChannel conectorSalidaMessages();
	
	@Output("conectorSalidaErrorsChannel")
	MessageChannel conectorSalidaErrors();

}

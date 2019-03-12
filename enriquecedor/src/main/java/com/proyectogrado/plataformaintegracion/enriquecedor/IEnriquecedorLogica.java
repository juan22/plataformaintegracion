package com.proyectogrado.plataformaintegracion.enriquecedor;

import org.springframework.messaging.Message;

public interface IEnriquecedorLogica {
	
	Message<String> enriquecerMensaje(Message<String> message);

}

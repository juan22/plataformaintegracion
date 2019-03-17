package com.proyectogrado.plataformaintegracion.conectorSalida;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Service
public class ConectorLogica implements IConectorLogica{
	
	@Autowired
	Environment env;
	
	@Autowired
	IServicioParametrosRequest servicioParametrosRequestSOAP;
	
	private Logger logger = LoggerFactory.getLogger(ConectorLogica.class);
	
	public Message<String> procesamientoConector(Message<String> message) throws Exception{
		int numero = (int) (Math.random() * 100);
		logger.info("EJECUTANDO CONECTORSALIDA!! El numero aleatorio es:"+numero);
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idsol");
		Integer paso = (Integer) headers.get("paso");
		if (numero > 80) {
			logger.error("El CONECTORSALIDA dio error!!");
			throw new Exception("Error por número aleatorio!!");
		}
		logger.info("Llego el siguiente mensaje al CONECTORSALIDA: "+ message.getPayload().toString());
		logger.info("Solucion ejecutada en CONECTORSALIDA: "+ idSol);
		logger.info("Paso de solucion ejecutada en CONECTORSALIDA: "+ paso);
		StringBuffer elementParam1Prpty = new StringBuffer("conectorSalida.").append(idSol).append(".requestParam1");
		String elementParam1 = env.getProperty(elementParam1Prpty.toString());
		String param1 = servicioParametrosRequestSOAP.obtenerUnParametro(elementParam1, message.getPayload());
		StringBuffer clienteWSDLPrpty = new StringBuffer("conectorSalida.").append(idSol).append(".clienteWSDL");
        String clienteWSDL = env.getProperty(clienteWSDLPrpty.toString());
		String wsdlResponse = llamadaClienteSOAP(clienteWSDL, param1);
		logger.info("Respuesta de cliente final: "+ wsdlResponse);
		Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(wsdlResponse).copyHeaders(message.getHeaders()).build();
		return messageResultado;
		
	}
	
	private String llamadaClienteSOAP(String url, String param) throws Exception {
		 try{
	          // Create SOAP Connection
	         SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
	         SOAPConnection soapConnection = soapConnectionFactory.createConnection();
	
	         //Send SOAP Message to SOAP Server
	         SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(param), url);
	         
	         // Process the SOAP Response
	         String resultadoXML = getContentXML(soapResponse);
	         
	         soapConnection.close();
	         return resultadoXML;
        } catch (Exception e){
       	 logger.error("Ocurrió el siguiente error al enviar request SOAP al cliente final: "+e.getMessage());
       	 throw new Exception("Error al comunicarse con el cliente final");
        }
	}

	private SOAPMessage createSOAPRequest(String param) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
       SOAPMessage soapMessage = messageFactory.createMessage();
       SOAPPart soapPart = soapMessage.getSOAPPart();
       
       // SOAP Envelope
       SOAPEnvelope envelope = soapPart.getEnvelope();
       envelope.addNamespaceDeclaration("emp", "http://clienteFinalSOAP/empresas");

       // SOAP Body
       SOAPBody soapBody = envelope.getBody();
       SOAPElement soapBodyElem = soapBody.addChildElement("buscarPorRazonSocialRequest", "emp");
       SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("razonSocial", "emp");
       soapBodyElem1.addTextNode(param);

       soapMessage.saveChanges();

       // Check the input
       System.out.println("Request SOAP Message = ");
       soapMessage.writeTo(System.out);
       System.out.println();
       return soapMessage;
	}
	
	private String getContentXML(SOAPMessage soapResponse) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
		String strMsg = new String(out.toByteArray());
		return strMsg;
   }

}

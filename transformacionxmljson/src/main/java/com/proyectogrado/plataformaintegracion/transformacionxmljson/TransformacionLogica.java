package com.proyectogrado.plataformaintegracion.transformacionxmljson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class TransformacionLogica implements ITransformacionLogica {
	
	@Autowired
	Environment env;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionLogica.class);
	
	public Message<String> procesamientoTransformacion(Message<String> message) throws Exception {
		logger.info("Mensaje recibido en el Transformador: "+message.toString());
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idsol");
		Integer paso = (Integer) headers.get("paso");
		int numero = (int) (Math.random() * 100);
		logger.info("VAMOS A TRANSFORMAR!! El numero aleatorio es:"+numero);
		Message<String> messageResultado;
		if (numero > 80) {
			logger.error("El transformador dio error!!");
			throw new Exception("Error por número aleatorio!!");
		}
		String result = transformarXmlToJson(message.getPayload());
		logger.info("Resultado de Transformacion: "+result);
        paso = paso + 1;
        messageResultado = (Message<String>) MessageBuilder.withPayload(result).copyHeaders(message.getHeaders()).setHeader("paso", paso).build();
        return messageResultado;
	}

//	public String transformacion(String texto, String solucion, Integer paso) {
//		try {
//			StringBuffer trnURL = new StringBuffer("transformacion.url.");
//			trnURL.append(solucion).append(".paso").append(paso);
//			StringBuffer trnContentType = new StringBuffer("transformacion.contentype.");
//			trnContentType.append(solucion).append(".paso").append(paso);
//	        String strConnection = env.getProperty(trnURL.toString());
//	        String contentType = env.getProperty(trnContentType.toString());
//	        URL url = new URL(strConnection);
//	        URLConnection uc = url.openConnection();
//	        HttpURLConnection conn = (HttpURLConnection) uc;
//	        conn.setDoOutput(true);
//	        conn.setRequestProperty("content-type", contentType);
//	        conn.setRequestMethod("POST");
//			OutputStream os = conn.getOutputStream();
//			os.write(texto.getBytes());
//			os.flush();
//			os.close();
//	        
//	        int rspCode = conn.getResponseCode();
//	        if (rspCode == HttpURLConnection.HTTP_OK) {
//	        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				String inputLine;
//				StringBuffer response = new StringBuffer();
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//				System.out.println(response.toString());
//				return response.toString();
//	        }else {
//				System.out.println("POST request not worked");
//				return null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
		
	
//	private String transformarJsonToXml(String texto) throws Exception{
//		JSONObject json = new JSONObject(texto);
//		return XML.toString(json);
//    }
	
	private String transformarXmlToJson(String texto) throws Exception{
		JSONObject xmlJSONObj = XML.toJSONObject(texto);
        return xmlJSONObj.toString(4);
    }
	
//	private String transformarXmlToJson(String texto){
//        try{
//            // Create a new XmlMapper to read XML tags
//            XmlMapper xmlMapper = new XmlMapper();
//            
//            //Reading the XML
//            JsonNode jsonNode = xmlMapper.readTree(texto.getBytes());
//            
//            //Create a new ObjectMapper
//            ObjectMapper objectMapper = new ObjectMapper();
//            
//            //Get JSON as a string
//            String value = objectMapper.writeValueAsString(jsonNode);
//            
//            System.out.println("*** Converting XML to JSON ***");
//            System.out.println(value);
//            return value;
//        } catch (JsonParseException e){
//            e.printStackTrace();
//            return null;
//        } catch (JsonMappingException e){
//            e.printStackTrace();
//            return null;
//        } catch (IOException e){
//            e.printStackTrace();
//            return null;
//        }
//    }
	
//	private String transformacionXSLT(String texto, String solucion, Integer paso) {
//        try {
//        	StringBuffer trnXslt = new StringBuffer("transformacion.xslt.");
//        	trnXslt.append(solucion).append(".paso").append(paso);
//        	String archivoXSLT = env.getProperty(trnXslt.toString());
//        	logger.info("XSLT en el Transformador: "+archivoXSLT);
//	        StringWriter sw = new StringWriter();
//	        net.sf.saxon.s9api.Processor processor = new net.sf.saxon.s9api.Processor(false);        
//	        Serializer serializer = processor.newSerializer();
//	        serializer.setOutputWriter(sw);   
//	        XsltCompiler compiler = processor.newXsltCompiler();
//	        XsltExecutable executable;
//			executable = compiler.compile(new StreamSource(new File(archivoXSLT)));
//	        XsltTransformer transformer = executable.load();
//	        	       
//			XdmNode source = processor.newDocumentBuilder().build(new StreamSource(new ByteArrayInputStream(texto.getBytes("UTF-8"))));
//			transformer.setInitialContextNode(source);
//							
//			transformer.setDestination(serializer);
//	        transformer.transform();
//	        return sw.toString();
//        } catch (SaxonApiException e) {
//			e.printStackTrace();
//			return null;
//        } catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

}

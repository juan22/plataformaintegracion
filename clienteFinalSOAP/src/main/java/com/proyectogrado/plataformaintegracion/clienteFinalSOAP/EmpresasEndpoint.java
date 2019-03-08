package com.proyectogrado.plataformaintegracion.clienteFinalSOAP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import clientefinalsoap.empresas.BuscarPorRazonSocialRequest;
import clientefinalsoap.empresas.BuscarPorRazonSocialResponse;
import clientefinalsoap.empresas.Empresa;


@Endpoint
public class EmpresasEndpoint {
	
	@Autowired
	ServicioEmpresa servicioEmpresa;
	
	@PayloadRoot(namespace = "http://clienteFinalSOAP/empresas", localPart = "buscarPorRazonSocialRequest")
	@ResponsePayload
	public BuscarPorRazonSocialResponse buscarPorRazonSocial(@RequestPayload BuscarPorRazonSocialRequest request) {
		BuscarPorRazonSocialResponse response = new BuscarPorRazonSocialResponse();
		try {
		    String razonSocial = request.getRazonSocial();
			EmpresaDto empresaDto;
			empresaDto = servicioEmpresa.buscarPorRazonSocial(razonSocial);
			Empresa empresa = new Empresa();
			empresa.setDireccion(empresaDto.getDireccion());
			empresa.setLocalidad(empresaDto.getLocalidad());
			empresa.setNombre(empresaDto.getNombre());
			empresa.setRazonSocial(empresaDto.getRazonSocial());
			empresa.setRut(empresaDto.getRut());
		    response.setEmpresa(empresa);
		    response.setCodigoRespuesta(0);
		} catch (Exception e) {
			response.setCodigoRespuesta(1);
			response.setMensajeRespuesta(e.getMessage());
		}
		return response;
	}

}

package com.proyectogrado.plataformaintegracion.clienteFinalSOAP;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ServicioEmpresa {
	
	private static List<EmpresaDto> empresas;

	public ServicioEmpresa() {
		inicializarEmpresas();
	}
	
	public EmpresaDto buscarPorRazonSocial(String razonSocial) throws Exception {
    	for (EmpresaDto e : empresas) {
    		if (e.getRazonSocial().equals(razonSocial)) {
    			return e;
    		}
    	}
        throw new Exception("No se encontró empresa con razón social " + razonSocial);
    }
    
    public EmpresaDto[] obtenerTodasEmpresas() {
    	EmpresaDto[] resultado = new EmpresaDto[empresas.size()];
        int i = 0;
        for (EmpresaDto e : empresas) {
        	resultado[i] = e;
            i++;
        }
        return resultado;
    }
    
    public void insertarEmpresa(EmpresaDto empresa) {
    	empresas.add(empresa);
    }

	private void inicializarEmpresas() {
		if (empresas == null) {
			empresas = new ArrayList<EmpresaDto>();
			empresas.add(new EmpresaDto("Arcos Dorados", "McDonald’s", "12323332", "Montevideo", "18 de Julio y Ejido"));
			empresas.add(new EmpresaDto("PepsiCo", "Pepsi", "23499484", "Montevideo", "Camino Juan Burghi 2645-Ruta1 y Cibils – Km. 10"));
			empresas.add(new EmpresaDto("Mapfre Uruguay Seguros S.A.", "Mapfre Seguros", "23900442", "Montevideo", "Juncal 1385 Piso 1"));
		}
		
	}
	
	

}

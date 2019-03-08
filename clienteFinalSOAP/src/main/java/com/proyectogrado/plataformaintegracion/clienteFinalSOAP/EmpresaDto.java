package com.proyectogrado.plataformaintegracion.clienteFinalSOAP;

public class EmpresaDto {
	
	private String razonSocial;
	private String nombre;
	private String rut;
	private String localidad;
	private String direccion;
		
	public EmpresaDto() {
		super();
	}
	
	public EmpresaDto(String razonSocial, String nombre, String rut, String localidad, String direccion) {
		super();
		this.razonSocial = razonSocial;
		this.nombre = nombre;
		this.rut = rut;
		this.localidad = localidad;
		this.direccion = direccion;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
}

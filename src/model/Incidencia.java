package model;
public class Incidencia {
	private String msg;
	
	public Incidencia(String msg){
		this.msg=msg;
		Main.incidencias.add(this);
	}
	public String getMsg() {
		return this.msg;
	}
}

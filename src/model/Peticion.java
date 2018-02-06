package model;
import java.util.ArrayList;
import java.util.Calendar;

public class Peticion {
	
	private String assignatura;
	private Calendar fechaInicio;
	private Calendar fechaFin;
	private String aula;
	private String mascaraDias;
	private ArrayList<Integer> horasInicio;
	private ArrayList<Integer> horasFin;
	private boolean estado = false;
	
	public Peticion(){
		
	}
	
	public Peticion(String assignatura, Calendar fechaInicio, Calendar fechaFin, String aula, String mascaraDias,
			ArrayList<Integer> horasInicio, ArrayList<Integer> horasFin) {
		super();
		this.assignatura = assignatura;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.aula = aula;
		this.mascaraDias = mascaraDias;
		this.horasInicio = horasInicio;
		this.horasFin = horasFin;
	}

	public String getAssignatura() {
		return assignatura;
	}
	public void setAssignatura(String assignatura) {
		this.assignatura = assignatura;
	}
	public Calendar getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Calendar fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Calendar getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Calendar fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getAula() {
		return aula;
	}
	public void setAula(String aula) {
		this.aula = aula;
	}
	public String getMascaraDias() {
		return mascaraDias;
	}
	public void setMascaraDias(String mascaraDias) {
		this.mascaraDias = mascaraDias;
	}
	public ArrayList<Integer> getHorasInicio() {
		return horasInicio;
	}
	public void setHorasInicio(ArrayList<Integer> horasInicio) {
		this.horasInicio = horasInicio;
	}
	public ArrayList<Integer> getHorasFin() {
		return horasFin;
	}
	public void setHorasFin(ArrayList<Integer> horasFin) {
		this.horasFin = horasFin;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
}

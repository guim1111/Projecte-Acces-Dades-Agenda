package model;
public class Internacional {
	private String agenda;
	private String[] dias;
	private String diasReduStr;
	private String[] diasRedu;
	private String[] mesos;
	public Internacional() {
	}
	public String getAgenda() {
		return agenda;
	}
	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}
	public String[] getDias() {
		return dias;
	}
	public void setDias(String[] dias) {
		this.dias = dias;
	}
	public String getDiasReduStr() {
		return diasReduStr;
	}
	public void setDiasReduStr(String diasReduStr) {
		this.diasReduStr = diasReduStr;
	}
	public String[] getDiasRedu() {
		return diasRedu;
	}
	public void setDiasRedu(String[] diasRedu) {
		this.diasRedu = diasRedu;
	}
	public String[] getMesos() {
		return mesos;
	}
	public void setMesos(String[] mesos) {
		this.mesos = mesos;
	}
}

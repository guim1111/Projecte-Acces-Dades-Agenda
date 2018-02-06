package model;

public class Evento {
	
	private String assignatura;
	
	public Evento(String assignatura) {
		this.assignatura = assignatura;
	}
	public String getAssignatura() {
		return assignatura;
	}
	public void setAssignatura(String assignatura) {
		this.assignatura = assignatura;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignatura == null) ? 0 : assignatura.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		if (assignatura == null) {
			if (other.assignatura != null)
				return false;
		} else if (!assignatura.equals(other.assignatura))
			return false;
		return true;
	}
}

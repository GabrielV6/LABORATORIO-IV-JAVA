package entidad;

import java.sql.Date;

public class Turno {
	
	private int idTurno;
	private Medico medico;
	private Paciente paciente;
	private Date fechaReserva;
	private String observacion;
	private EstadoTurno estadoTurno;
	private int hora;
	
	public Turno(int idTurno, Medico medico, Paciente paciente, Date fechaReserva, String observacion,
			EstadoTurno estadoTurno, int hora) {
		this.idTurno = idTurno;
		this.medico = medico;
		this.paciente = paciente;
		this.fechaReserva = fechaReserva;
		this.observacion = observacion;
		this.estadoTurno = estadoTurno;
		this.hora = hora;
	}
	
	public int getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public Date getFechaReserva() {
		return fechaReserva;
	}
	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public EstadoTurno getEstadoTurno() {
		return estadoTurno;
	}
	public void setIdEstadoTurno(EstadoTurno estadoTurno) {
		this.estadoTurno = estadoTurno;
	}
	public int getHora() {
		return hora;
	}
	public void setHora(int hora) {
		this.hora = hora;
	}
	
	@Override
	public String toString() {
		return "Turno [idTurno=" + idTurno + ", medico=" + medico.getApellido() + ", paciente=" + paciente.getApellido() + ", fechaReserva="
				+ fechaReserva + ", hora= "+hora+", observacion=" + observacion + ", EstadoTurno=" + estadoTurno.getDescripcion() + "]";
	}
	
}

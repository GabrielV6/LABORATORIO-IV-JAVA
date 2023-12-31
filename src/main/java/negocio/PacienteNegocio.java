package negocio;

import java.util.ArrayList;

import entidad.Paciente;


public interface PacienteNegocio {
	public boolean insert(Paciente paciente);
	public boolean delete(Paciente paciente);
	public boolean update(Paciente paciente);
	public ArrayList<Paciente> readAll(int estado);
	public boolean exists(Paciente paciente);
	public Paciente searchDni(int dni);	
}


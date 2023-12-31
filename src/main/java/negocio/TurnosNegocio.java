package negocio;

import java.util.ArrayList;
import java.sql.Date;
import entidad.Turno;
import entidad.Medico;

public interface TurnosNegocio {
	public boolean insert(Turno turno);
	public boolean delete(Turno turno);
	public boolean update(Turno turno);
	public ArrayList<Turno> readAll(int estado);
	public boolean exists(Turno turno);
	public ArrayList<Turno> readTurnosMedico(Medico medico);
	public ArrayList<Integer> turnosDisponiblesMedicoFecha(Medico medico, Date fecha);
}

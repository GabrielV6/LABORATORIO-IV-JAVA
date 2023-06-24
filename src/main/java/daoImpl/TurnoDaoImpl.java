package daoImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.TurnoDao;
import entidad.Especialidad;
import entidad.EstadoTurno;
import entidad.Localidad;
import entidad.Medico;
import entidad.Nacionalidad;
import entidad.Paciente;
import entidad.Provincia;
import entidad.Turno;

public class TurnoDaoImpl implements TurnoDao {
	
	private static final String READALL = "" + 
			" SELECT t.IdTurno, t.FechaReserva, t.Observacion, t.IdTurnoEstado, t.Hora, t.Estado, et.Descripcion as TurnoEstado," + 
			" p.Id as IdPac, p.Dni as DniPac, p.Nombre as NombrePac, p.Apellido as ApellidoPac, p.Sexo as SexoPac, p.IdNacionalidad as IdNacionalidadPac, np.Nacionalidad as NacionalidadPac, p.FechaNacimiento as FechaNacimientoPac, p.Direccion as DireccionPac, " + 
			" p.IdLocalidad as IdLocalidadPac, lp.Localidad as LocalidadPac, p.IdProvincia as IdProvinciaPac, pp.Provincia as ProvinciaPac, p.CorreoElectronico as CorreoElectronicoPac, p.Telefono as TelefonoPac, p.Estado as EstadoPac," + 
			" m.Id as IdMed, m.IdUsuario as IdUsuarioMed, m.Dni as DniMed, m.Nombre as NombreMed, m.Apellido as ApellidoMed, m.Sexo as SexoMed, m.IdNacionalidad as IdNacionalidadMed, nm.Nacionalidad as NacionalidadMed, " + 
			" m.FechaNacimiento as FechaNacimientoMed, m.Direccion as DireccionMed, m.IdLocalidad as IdLocalidadMed, lm.Localidad as LocalidadMed, m.IdProvincia as IdProvinciaMed, pm.Provincia as ProvinciaMed, m.CorreoElectronico as CorreoElectronicoMed, m.Telefono as TelefonoMed, m.Estado as EstadoMed, m.IdEspecialidad as IdEspecialidadMed, e.Descripcion as EspecialidadMed" + 
			" FROM clinica_medica.turnos t" + 
			" INNER JOIN clinica_medica.estadosturno et ON t.IdTurnoEstado = et.IdEstadoTurno" + 
			" INNER JOIN clinica_medica.pacientes p ON t.IdPaciente = p.Id" + 
			" INNER JOIN clinica_medica.nacionalidades np ON np.IdNacionalidad = p.IdNacionalidad " + 
			" INNER JOIN clinica_medica.provincias pp ON pp.IdProvincia = p.IdProvincia " + 
			" INNER JOIN clinica_medica.localidades lp ON lp.IdLocalidad = p.IdLocalidad" +  
			" INNER JOIN clinica_medica.medicos m ON t.IdMedico = m.Id" + 
			" INNER JOIN clinica_medica.nacionalidades nm ON nm.IdNacionalidad = m.IdNacionalidad" + 
			" INNER JOIN clinica_medica.provincias pm ON pm.IdProvincia = m.IdProvincia " + 
			" INNER JOIN clinica_medica.localidades lm ON lm.IdLocalidad = m.IdLocalidad" +
			" INNER JOIN clinica_medica.especialidades e ON e.IdEspecialidad = m.IdEspecialidad ";
	private static final String UPDATE = "UPDATE clinica_medica.turnos SET "
			+ " IdMedico = (select id from Medicos where dni = ? limit 1), IdPaciente = (select id from Pacientes where dni = ? limit 1), FechaReserva = ?, Observacion = ?, IdTurnoEstado = ?, Hora = ? "
			+ " WHERE IdTurno = ? ";
	private static final String CAMBIA_ESTADO = "UPDATE clinica_medica.turnos SET Estado = ? WHERE IdTurno = ?";
	private static final String INSERT = "INSERT INTO clinica_medica.turnos  " + 
			" (IdMedico, IdPaciente, FechaReserva, Observacion, IdTurnoEstado, Hora, Estado)"
			+ " VALUES ((select id from Medicos where dni = ? limit 1),(select id from Pacientes where dni = ? limit 1,?,?,?,?,1)";
	private static final String WHERE_SEARCH = " where t.IdTurno = ?";
	private static final String WHERE_SEARCH_DIA_HORA = " where fechaReserva = ? and hora = ? and estado = 1";
	private static final String WHERE_SEARCH_MEDICO = " where idMedico = (select id from Medicos where dni = ? limit 1) ";

	@Override
	public boolean insert(Turno turno) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try {
			statement = conexion.prepareStatement(INSERT);
			statement.setInt(1,turno.getMedico().getDni());
			statement.setInt(2, turno.getPaciente().getDni());
			statement.setDate(3, turno.getFechaReserva());
			statement.setString(4, turno.getObservacion());
			statement.setInt(5, turno.getEstadoTurno().getIdEstadoTurno());
			statement.setInt(6, turno.getHora());
			
			if (statement.executeUpdate() > 0) {
				conexion.commit();
				isInsertExitoso = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return isInsertExitoso;
	}

	@Override
	public boolean update(Turno turno, boolean esBaja) {
		if(esBaja) 
			return bajaTurno(turno);
		else
			return actualizarTurno(turno);
	}
	
	private boolean bajaTurno(Turno turno) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean estadoCambiado = false;
		try {
			statement = conexion.prepareStatement(CAMBIA_ESTADO);
			statement.setInt(1, 0);
			statement.setInt(2, turno.getIdTurno());
			if (statement.executeUpdate() > 0) {
				conexion.commit();
				estadoCambiado = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return estadoCambiado;
	}
	
	private boolean actualizarTurno(Turno turno) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean actualizado = false;
		try {
			statement = conexion.prepareStatement(UPDATE);
			statement.setInt(1, turno.getMedico().getDni());
			statement.setInt(2, turno.getPaciente().getDni());
			statement.setDate(3, turno.getFechaReserva());
			statement.setString(4, turno.getObservacion());
			statement.setInt(5, turno.getEstadoTurno().getIdEstadoTurno());
			statement.setInt(6, turno.getHora());
			statement.setInt(7, turno.getIdTurno());
			if (statement.executeUpdate() > 0) {
				conexion.commit();
				actualizado = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return actualizado;
	}

	@Override
	public ArrayList<Turno> readAll(int estado) {
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		Conexion conexion = Conexion.getConexion();
		try {
			String query = READALL;
			
			if (estado == 0)
				query += " WHERE t.estado = 0";
			else if (estado > 0)
				query += " WHERE t.estado = 1";			
			
			statement = conexion.getSQLConexion().prepareStatement(query);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				turnos.add(getTurno(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return turnos;
	}

	@Override
	public Turno searchTurno(int idTurno) {
		PreparedStatement statement;
		ResultSet resultSet;
		Turno turno = null;
		Conexion conexion = Conexion.getConexion();
		try {
			statement = conexion.getSQLConexion().prepareStatement(READALL+WHERE_SEARCH);
			statement.setInt(1, idTurno);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				turno = getTurno(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return turno;
	}
	
	private Turno getTurno(ResultSet resultSet) throws SQLException {
		int idTurno = resultSet.getInt("idTurno");
		Medico medico = getMedico(resultSet);
		Paciente paciente = getPaciente(resultSet);
		Date fechaReserva = resultSet.getDate("FechaReserva");
		String observacion = resultSet.getString("observacion");
		EstadoTurno estadoTurno = new EstadoTurno(resultSet.getInt("IdTurnoEstado"), resultSet.getString("TurnoEstado"));
		int hora = resultSet.getInt("hora");
		
		return new Turno(idTurno, medico, paciente, fechaReserva, observacion, estadoTurno, hora);
	}
	
	private Medico getMedico(ResultSet resultSet) throws SQLException {
		int idUsuario = resultSet.getInt("IdUsuarioMed");
		int dni = resultSet.getInt("DniMed");
		String nombre = resultSet.getString("NombreMed");
		String apellido = resultSet.getString("ApellidoMed");
		String sexo = resultSet.getString("SexoMed");
		int idEspecialidad = resultSet.getInt("IdEspecialidadMed");
		String especialidad = resultSet.getString("EspecialidadMed");
		int idNacionalidad = resultSet.getInt("IdNacionalidadMed");
		String nacionalidad = resultSet.getString("NacionalidadMed");
		String fechaNac = resultSet.getString("FechaNacimientoMed");
		String direccion = resultSet.getString("DireccionMed");
		int idLocalidad = resultSet.getInt("IdLocalidadMed");
		String localidad = resultSet.getString("LocalidadMed");
		int idProvincia = resultSet.getInt("IdProvinciaMed");
		String provincia = resultSet.getString("ProvinciaMed");
		String correoElec = resultSet.getString("CorreoElectronicoMed");
		String telefono = resultSet.getString("TelefonoMed");
		int estado = resultSet.getInt("EstadoMed");			
		return new Medico(idUsuario, dni, nombre, apellido, sexo, new Especialidad(idEspecialidad,especialidad), new Nacionalidad(idNacionalidad,nacionalidad), fechaNac, direccion, 
				new Localidad(idLocalidad, localidad, new Provincia(idProvincia, provincia)), new Provincia(idProvincia, provincia), correoElec, telefono, estado);
	}
	
	private Paciente getPaciente(ResultSet resultSet) throws SQLException {
		int dni = resultSet.getInt("DniPac");
		String nombre = resultSet.getString("NombrePac");
		String apellido = resultSet.getString("ApellidoPac");
		String sexo = resultSet.getString("SexoPac");
		int idNacionalidad = resultSet.getInt("IdNacionalidadPac");
		String nacionalidad = resultSet.getString("NacionalidadPac");
		String fechaNac = resultSet.getString("FechaNacimientoPac");
		String direccion = resultSet.getString("DireccionPac");
		int idLocalidad = resultSet.getInt("IdLocalidadPac");
		String localidad = resultSet.getString("LocalidadPac");
		int idProvincia = resultSet.getInt("IdProvinciaPac");
		String provincia = resultSet.getString("ProvinciaPac");
		String correoElec = resultSet.getString("CorreoElectronicoPac");
		String telefono = resultSet.getString("TelefonoPac");
		int estado = resultSet.getInt("EstadoPac");
				
		
		return new Paciente(dni, nombre, apellido, sexo, new Nacionalidad(idNacionalidad,nacionalidad), fechaNac, direccion, 
				new Localidad(idLocalidad, localidad, new Provincia(idProvincia, provincia)), new Provincia(idProvincia, provincia), correoElec, telefono, estado);
	}
	
	@Override
	public ArrayList<Turno> searchTurnosDiaHorario(Date fecha, int hora){
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		Conexion conexion = Conexion.getConexion();
		try {
			statement = conexion.getSQLConexion().prepareStatement(READALL+WHERE_SEARCH_DIA_HORA);
			statement.setDate(1, fecha);
			statement.setInt(2, hora);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				turnos.add(getTurno(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return turnos;
	}
	
	@Override
	public ArrayList<Turno> searchTurnosMedico(int dniMedico){
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		Conexion conexion = Conexion.getConexion();
		try {
			statement = conexion.getSQLConexion().prepareStatement(READALL+WHERE_SEARCH_MEDICO);
			statement.setInt(1, dniMedico);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				turnos.add(getTurno(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return turnos;
	}

}

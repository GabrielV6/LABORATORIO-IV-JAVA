package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.MedicoDao;
import entidad.Localidad;
import entidad.Medico;
import entidad.Nacionalidad;
import entidad.Provincia;

public class MedicoDaoImpl implements MedicoDao {

	private static final String INSERT = "INSERT INTO clinica_medica.medicos"
			+ "(IdUsuario, Dni, Nombre, Apellido, Sexo, IdNacionalidad, FechaNacimiento, Direccion, "
			+ "IdLocalidad, IdProvincia, CorreoElectronico, Telefono, Estado) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static final String SELECT_COUNT = "SELECT COUNT(*) FROM clinica_medica.medicos";
	private static final String CAMBIA_ESTADO = "UPDATE clinica_medica.medicos SET Estado = ? WHERE Dni = ?";
	private static final String UPDATE = "UPDATE clinica_medica.medicos SET IdUsuario = ?, Nombre = ?, Apellido = ?, Sexo = ?, IdNacionalidad = ?, "
			+ "FechaNacimiento = ?, Direccion = ?, IdLocalidad = ?, IdProvincia = ?, CorreoElectronico = ?, Telefono = ?, Estado = ? WHERE Dni = ?";
	private static final String READALL = "SELECT m.p.Id, m.IdPerfil, m.Dni, m.Nombre, m.Apellido, m.Sexo, m.Nacionalidad, m.FechaNacimiento, m.Direccion, "
			+ "m.Localidad, m.Provincia, m.CorreoElectronico, m.Telefono, m.IdJornada, m.Estado "
			+ "FROM clinica_medica.medicos m "
			+ "INNER JOIN clinica_medica.nacionalidades n ON n.IdNacionalidad = m.IdNacionalidad "
			+ "INNER JOIN clinica_medica.provincias pr ON pr.IdProvincia = m.IdProvincia "
			+ "INNER JOIN clinica_medica.localidades l ON l.IdLocalidad = m.IdLocalidad";
	private static final String SEARCH = "SELECT p.IdUsuario, p.Dni, p.Nombre, p.Apellido, p.Sexo, p.IdNacionalidad, n.Nacionalidad, p.FechaNacimiento, p.Direccion, "
			+ "p.IdLocalidad, l.Localidad, p.IdProvincia, pr.Provincia, p.CorreoElectronico, p.Telefono, p.Estado "
			+ "FROM clinica_medica.medicos m "
			+ "INNER JOIN clinica_medica.nacionalidades n ON n.IdNacionalidad = m.IdNacionalidad "
			+ "INNER JOIN clinica_medica.provincias pr ON pr.IdProvincia = m.IdProvincia "
			+ "INNER JOIN clinica_medica.localidades l ON l.IdLocalidad = m.IdLocalidad WHERE p.Dni = ?";

	@Override
	public boolean insert(Medico medico) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try {
			statement = conexion.prepareStatement(INSERT);
			statement.setInt(1, medico.getIdUsuario());
			statement.setInt(2, medico.getDni());
			statement.setString(3, medico.getNombre());
			statement.setString(4, medico.getApellido());
			statement.setString(5, medico.getSexo());
			statement.setInt(6, medico.getNacionalidad().getIdNacionalidad());
			statement.setString(7, medico.getFechaNacimiento());
			statement.setString(8, medico.getDireccion());
			statement.setInt(9, medico.getLocalidad().getIdLocalidad());
			statement.setInt(10, medico.getProvincia().getIdProvincia());
			statement.setString(11, medico.geteMail());
			statement.setString(12, medico.getTelefono());
			statement.setInt(13, 1);
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
	public int selectCount(Medico medico) {
		// TODO Auto-generated method stub
		PreparedStatement statement;
		ResultSet resultSet;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		int count = 0;
		try {
			String query = SELECT_COUNT;
			if (medico != null) {
				String where = " WHERE Dni = ?";
				statement = conexion.prepareStatement(query + where);
				statement.setInt(1, medico.getDni());
			} else
				statement = conexion.prepareStatement(query);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public boolean update(Medico medico, boolean esBaja) {
		if (esBaja)
			return bajaMedico(medico);
		else
			return actualizaMedico(medico);
	}

	private boolean actualizaMedico(Medico medico) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean actualizado = false;
		try {
			statement = conexion.prepareStatement(UPDATE);
			statement.setInt(1, medico.getIdUsuario());
			statement.setString(2, medico.getNombre());
			statement.setString(3, medico.getApellido());
			statement.setString(4, medico.getSexo());
			statement.setInt(5, medico.getNacionalidad().getIdNacionalidad());
			statement.setString(6, medico.getFechaNacimiento());
			statement.setString(7, medico.getDireccion());
			statement.setInt(8, medico.getLocalidad().getIdLocalidad());
			statement.setInt(9, medico.getProvincia().getIdProvincia());
			statement.setString(10, medico.geteMail());
			statement.setString(11, medico.getTelefono());
			statement.setInt(12, medico.getEstado());
			statement.setInt(13, medico.getDni());
			if (statement.executeUpdate() > 0) {
				conexion.commit();
				actualizado = true;
			}
		} catch (SQLException e) {
		}
		return actualizado;
	}

	private boolean bajaMedico(Medico medico) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean estadoCambiado = false;
		try {
			statement = conexion.prepareStatement(CAMBIA_ESTADO);
			statement.setInt(1, 0);
			statement.setInt(2, medico.getDni());
			if (statement.executeUpdate() > 0) {
				conexion.commit();
				estadoCambiado = true;
			}
		} catch (SQLException e) {
		}
		return estadoCambiado;
	}

	@Override
	public ArrayList<Medico> readAll(int estado) {
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<Medico> medicos = new ArrayList<Medico>();
		Conexion conexion = Conexion.getConexion();
		try {
			String query = READALL;
			
			if (estado == 0)
				query += " WHERE p.estado = 0";
			else if (estado > 0)
				query += " WHERE p.estado = 1";			
			
			statement = conexion.getSQLConexion().prepareStatement(query);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				medicos.add(getMedico(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return medicos;
	}
	
	private Medico getMedico(ResultSet resultSet) throws SQLException {
		int idUsuario = resultSet.getInt("IdUsuario");
		int dni = resultSet.getInt("Dni");
		String nombre = resultSet.getString("Nombre");
		String apellido = resultSet.getString("Apellido");
		String sexo = resultSet.getString("Sexo");
		int idNacionalidad = resultSet.getInt("IdNacionalidad");
		String nacionalidad = resultSet.getString("Nacionalidad");
		String fechaNac = resultSet.getString("FechaNacimiento");
		String direccion = resultSet.getString("Direccion");
		int idLocalidad = resultSet.getInt("IdLocalidad");
		String localidad = resultSet.getString("Localidad");
		int idProvincia = resultSet.getInt("IdProvincia");
		String provincia = resultSet.getString("Provincia");
		String correoElec = resultSet.getString("CorreoElectronico");
		String telefono = resultSet.getString("Telefono");
		int estado = resultSet.getInt("Estado");				
		
		return new Medico(idUsuario, dni, nombre, apellido, sexo, new Nacionalidad(idNacionalidad,nacionalidad), fechaNac, direccion, 
				new Localidad(idLocalidad, localidad, new Provincia(idProvincia, provincia)), new Provincia(idProvincia, provincia), correoElec, telefono, estado);
	}
	
	@Override
	public Medico searchMedico(int dni) {
		PreparedStatement statement;
		ResultSet resultSet;
		Medico medico = null;
		Conexion conexion = Conexion.getConexion();
		try {
			statement = conexion.getSQLConexion().prepareStatement(SEARCH);
			statement.setInt(1, dni);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				medico = getMedico(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return medico;
	}

}
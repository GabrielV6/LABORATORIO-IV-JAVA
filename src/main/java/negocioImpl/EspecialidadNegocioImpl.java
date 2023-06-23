package negocioImpl;

import java.util.ArrayList;

import dao.*;
import daoImpl.*;
import entidad.Especialidad;
import negocio.EspecialidadNegocio;

public class EspecialidadNegocioImpl implements EspecialidadNegocio{

	@Override
	public ArrayList<Especialidad> readAll() {
		EspecialidadDao espDao = new EspecialidadDaoImpl();
		return espDao.readAll();
	}

}

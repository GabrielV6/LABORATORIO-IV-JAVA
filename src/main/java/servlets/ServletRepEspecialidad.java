package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entidad.*;
import negocio.*;
import negocioImpl.*;

/**
 * Servlet implementation class ServletReporteEspecialidad
 */
@WebServlet("/ServletRepEspecialidad")
public class ServletRepEspecialidad extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletRepEspecialidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Usuario usuario = (Usuario) request.getSession().getAttribute("Usuario");

		if (request.getSession().getAttribute("Usuario") != null && usuario.getIdPerfil() != Usuario.ROL_MEDICO) {
			response.getWriter().append("Served at: ").append(request.getContextPath());
			try {
				// Cargar controles de la pagina
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error", "Hubo un problema al cargar la pantalla del reporte");
			}
			RequestDispatcher rd = request.getRequestDispatcher("ReporteEspecialidad.jsp");
			rd.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/Error.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("mesEspecialidad") != null) {

			String anioMesEsp = (request.getParameter("mesEspecialidad").toString());

			RepEspecialidadNegocio repEsp = new RepEspecialidadNegocioImpl();

			try {

				ReporteEspecialidad report = repEsp.searchReport(anioMesEsp);
				if (report != null && report.getTotalColumnas() > 0 && report.getTotalFilasPorColumnas() > 0) {
					request.setAttribute("repEspecialidad", report);
					request.setAttribute("anioMes", anioMesEsp);
					
				}
				else {
					request.setAttribute("error", "No se encontraron datos para el reporte");
				}
				RequestDispatcher rd = request.getRequestDispatcher("ReporteEspecialidad.jsp");
				rd.forward(request, response);

			} catch (Exception e) {
				response.sendRedirect(request.getContextPath() + "/Error.jsp");
			}
		}
	}

}

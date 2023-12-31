package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entidad.Usuario;
import negocio.LoginNegocio;
import negocioImpl.LoginNegocioImpl;



@WebServlet("/servletLogin")
public class servletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public servletLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//Cierra session.
		request.getSession().setAttribute("Usuario", null);
		RequestDispatcher rd=request.getRequestDispatcher("Login.jsp");
		rd.forward(request, response); 
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if (request.getParameter("txtUsuario") != null && request.getParameter("txtContraseña") != null) {
				
				String userLogin= (request.getParameter("txtUsuario").toString());
				String password= (request.getParameter("txtContraseña").toString());
				
				LoginNegocio lg = new LoginNegocioImpl();
				
				Usuario usuario = lg.ValidarUsuario(userLogin,password);
				if(usuario != null) {
					
			
				request.getSession().setAttribute("Usuario", usuario);
				request.getSession().setAttribute("UsuarioLogeado", request.getParameter("txtUsuario").toString());
				
				RequestDispatcher rd=request.getRequestDispatcher("Inicio.jsp");
				rd.forward(request, response); 
				
				}else {
				    response.sendRedirect(request.getContextPath() + "/Error.jsp");
				}
			}
		

	}
	

}

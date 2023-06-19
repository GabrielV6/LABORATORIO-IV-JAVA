
<%@ page import="java.util.ArrayList"%>
<%@page import="entidad.Nacionalidad"%>
<%@page import="entidad.Provincia"%>
<%@page import="entidad.Medico"%>
<%@page import="entidad.Localidad"%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Medico</title>

</head>
<body>
	<%@include file="Nav.html"%>
	<%
	//TODO -> Validar error / success
	String op = request.getAttribute("op") != null ? request.getAttribute("op").toString() : "add";
	String apellido, nombre, dni, eMail, fechaNacimiento, telefono, direccion, sexo;
	apellido = nombre = dni = eMail = fechaNacimiento = telefono = direccion = sexo = "";

	int maxId, loc, nacion, prov;
	maxId = loc = nacion = prov = 0;

	Medico medico = null;

	if (request.getAttribute("paciente") != null || !op.equals("add")) {
		try {
			medico = (Medico) request.getAttribute("paciente");
			apellido = medico.getApellido();
			nombre = medico.getNombre();
			dni = String.format("%s", medico.getDni());
			eMail = medico.geteMail();
			fechaNacimiento = medico.getFechaNacimiento().toString().substring(0, 10);
			loc = medico.getLocalidad().getIdLocalidad();
			nacion = medico.getNacionalidad().getIdNacionalidad();
			prov = medico.getProvincia().getIdProvincia();
			sexo = medico.getSexo();
			telefono = medico.getTelefono();
			direccion = medico.getDireccion();
		} catch (Exception e) {
			if (request.getAttribute("error") == null)
		request.setAttribute("error", "Hubo inconvenientes al procesar los datos");
		}
	}
	%>
	<form action="servletMedico" method="post">

		<h2>Alta y Modificación de Medicos</h2>
		<br>
		<table>
			<tr>
                <td><label>DNI</label></td>
                <td><input type="text" name="txtDni" value="<%= dni %>" required <%= op.equals("add") ? "" : "disabled" %>><input type="hidden" name="txtDniHide" value="<%= dni %>" ></td>
            </tr>			
			<tr>
				<td><label>Nombre</label></td>
				<td><input type="text" name="txtNombre" value="<%= nombre %>" required></td>
			</tr>
			<tr>
				<td><label>Apellido</label></td>
				<td><input type="text" name="txtApellido" value="<%= apellido %>" required></td>
			</tr>
			<tr>
				<td><label>Nacionalidad</label></td>
				<td><select name="selNacionalidad">
						<%
						ArrayList<Nacionalidad> nacionalidades = null;

						if (request.getAttribute("nacionalidades") != null) {
							nacionalidades = (ArrayList<Nacionalidad>) request.getAttribute("nacionalidades");
						
						for (Nacionalidad nacionalidad : nacionalidades) {
						%>
						<option  value="<%=nacionalidad.getIdNacionalidad()%>"  <%= nacion == nacionalidad.getIdNacionalidad() ? "selected" : "" %>>
							<%=nacionalidad.getNacionalidad()%>
						</option>
						
						<%
						}}
						%>
				</select></td>
			</tr>
			<tr>
				<td><label>Sexo</label></td>
				<td><select name="selSexo">
						<option value="No indica" <%= sexo == "No indica" ? "selected" : "" %>>No indica</option>
						<option value="Femenino" <%= sexo == "Femenino" ? "selected" : "" %>>Femenino</option>
						<option value="Masculino" <%= sexo == "Masculino" ? "selected" : "" %>>Masculino</option>
				</select></td>
			</tr>
			<tr>
				<td><label>Fecha Nacimiento</label></td>
				<td><input type="date" name="txtFecNacimiento" value="<%= fechaNacimiento %>"
					required></td>
			</tr>

			<tr>
				<td><label>Provincia</label></td>
				<td><select name="selProvincia" Id="selProvincia">
						<%
						ArrayList<Provincia> provincias = null;

						if (request.getAttribute("provincias") != null) {
							provincias = (ArrayList<Provincia>) request.getAttribute("provincias");
						
						for (Provincia provincia : provincias) {
						%>
							<option value="<%=provincia.getIdProvincia()%>" <%= prov == provincia.getIdProvincia() ? "selected" : "" %>>
								<%=provincia.getProvincia()%>
							</option>
						<%
						}}
						%>
				</select></td>
			</tr>

			<tr>
                <td><label>Localidad</label></td>
                <td><select name="selLocalidad" Id="selLocalidad">
                        <%
                        ArrayList<Localidad> localidades = null;

                        if (request.getAttribute("localidades") != null) {
                            localidades = (ArrayList<Localidad>) request.getAttribute("localidades");

                        for (Localidad localidad : localidades) {
                        %>
                        <option value="<%=localidad.getIdLocalidad()%>" provincias="<%=localidad.getProvincia().getIdProvincia() %>" <%= loc == localidad.getIdLocalidad() ? "selected" : "" %>>
                        	<%=localidad.getLocalidad()%>
                        </option>
						
                        <%
                        }}
                        %>
                </select></td>
            </tr>
			<tr>
				<td><label>Direccion</label></td>
				<td><input type="text" name="txtDireccion" value="<%= direccion %>" required></td>
			</tr>
			<tr>
				<td><label>Correo Electronico</label></td>
				<td><input type="email" name="txtEmail" value="<%= eMail %>" required></td>
			</tr>
			<tr>
				<td><label>Telefono</label></td>
				<td><input type="text" name="txtTelefono" value="<%= telefono %>" required></td>
			</tr>
			<tr>
				<% if(op.equals("add")) {%>
					<td><input type="submit" name="btnGrabar" value="Grabar"></td>
				<% } else { %>
					<td><input type="submit" name="btnActualizar" value="Grabar"></td>
					<td><input type="submit" name="btnEliminar" value="Eliminar"></td>
				<% } %>
			</tr>
		</table>

		<%
		if (request.getAttribute("success") != null) {
		%>
		<div class="success"><%=request.getAttribute("success")%></div>
		<%
		}
		%>
		<%
		if (request.getAttribute("error") != null) {
		%>
		<div class="error"><%=request.getAttribute("error")%></div>
		<%
		}
		%>
	</form>
	<script type="text/javascript">
	
	const desplegableA = document.getElementById('selProvincia');
	desplegableA.addEventListener('change', filtrarDesplegableB);

	function filtrarDesplegableB() {
	 
	  const valorSeleccionado = desplegableA.value;

	  const desplegableB = document.getElementById('selLocalidad');
	  Array.from(desplegableB.options).forEach(option => {
	    if (option.getAttribute('provincias') === valorSeleccionado) {
	      option.style.display = 'block';
	      option.setAttribute('selected',true);
	    } else {
	      option.style.display = 'none';
	    }
	  });
	}

	
	filtrarDesplegableB();
	</script>
</body>
</html>
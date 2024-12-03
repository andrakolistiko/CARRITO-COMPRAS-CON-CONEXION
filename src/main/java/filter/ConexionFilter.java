package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import utils.Conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

// Implementamos una anotación que permite utilizar la conexión en cualquier parte
// de la aplicación.
@WebFilter("/*")
public class ConexionFilter implements Filter {

    /*
     * Una clase Filter en Java es un objeto que realiza un filtrado
     * en las solicitudes y respuestas a un recurso. Los filtros se pueden ejecutar en servidores
     * web compatibles con Jakarta EE.
     * Los filtros interceptan solicitudes y respuestas de manera dinámica para transformarlos
     * o utilizar la información que contienen. El filtro se realiza en el método doFilter.
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Aseguramos que la conexión a la base de datos se maneje correctamente
        try (Connection conn = Conexion.getConnection()) { // Corregido: se añadió el operador punto (.)
            // Verificamos si la conexión realiza un autocommit
            if (conn.getAutoCommit()) {
                // Si está activo, desactivamos el autocommit
                conn.setAutoCommit(false);
            }

            try {
                // Agregamos la conexión como un atributo en la solicitud, lo que permitirá
                // que otros componentes (servlets o DAOs) puedan acceder a la conexión
                // desde el objeto request
                request.setAttribute("conn", conn);

                // Pasa la solicitud y la respuesta al siguiente filtro del recurso o destino (servlet o JSP)
                chain.doFilter(request, response);

                // Si el procesamiento se realizó correctamente sin lanzar ninguna excepción,
                // confirmamos la solicitud y aplicamos los cambios a la base de datos
                conn.commit();

            } catch (SQLException e) {
                // Si ocurre una excepción SQL, se deshacen los cambios con rollback
                // y de esa forma se mantiene la integridad de los datos
                conn.rollback();
                throw new ServletException("Error en la operación de la base de datos", e);

            } catch (Exception e) {
                // Manejo de otras excepciones
                throw new ServletException("Error inesperado", e);
            }
        } catch (SQLException e) {
            throw new ServletException("No se pudo establecer la conexión a la base de datos", e);
        }
    }
}
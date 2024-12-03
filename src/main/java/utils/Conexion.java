package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    //DECLARAMOS E INICIALIZAMOS 3 PARAMETROS PARA LA CONEXION A LA BASE DE DATOS
    private static String url = "jdbc:mysql://localhost:3306/carro?ustimezone=true&serverTimezone=UTC";
    private static String username = "root";
    private static String password = "";

    //IMPLEMENTAMOS UN METODO PARA OBTENER LA CONEXION
    public static Connection getConnection() throws SQLException {
return DriverManager.getConnection(url,username,password)
    }



}

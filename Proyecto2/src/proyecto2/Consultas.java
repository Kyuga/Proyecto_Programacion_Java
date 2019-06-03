/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import static proyecto2.Proyecto2.con;

/**
 *
 * @author RubénLlorenç
 */
public class Consultas {
    /**
     * Este método ejecutará la opción seleccionada por el usuario.
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void pedirConsultas(Connection con) throws SQLException {
        boolean exit = true;
        while (exit) {
            switch (menuConsultas()) {
                case 1:
                    mostrar_todos(con);
                    ;
                    break;
                case 2:
                    mostrar_concreto(con);
                    ;
                    break;
                case 3:
                    mostrar_media_salarios(con);
                    ;
                    break;
                case 4:
                    exit = false;
                    break;
            }
        }
    }
    /**
     * El método imprime el menú consultas
     * @return Devuelve la opción seleccionada
     */
    public static int menuConsultas() {
        Scanner sc = new Scanner(System.in);
        System.out.println("--------------------------");
        System.out.println("1) Mostrar todos los empleados");
        System.out.println("2) Mostrar un empleado concreto");
        System.out.println("3) Mostrar media salarios");
        System.out.println("4) Atrás");
        System.out.println("--------------------------");
        int x = sc.nextInt();
        return x;
    }
    /**
     * Este método muestra todos los empleados presentes en la BBDD.
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void mostrar_todos(Connection con) throws SQLException {
        Statement st = con().createStatement();
        ResultSet rs = st.executeQuery("Select * from employees");
        int contador = 0;

        System.out.println("Los datos de la tabla 'employees' son los siguientes\n");
        System.out.print("======================================\n");
        while (rs.next()) {
            contador++;//Por cada iteracción sumamos uno para obtener el número de empleados totales.
            System.out.println("EMPLEADO " + contador + "\n");
            System.out.println(" ID: " + rs.getString(1) + "\n");
            System.out.println(" Apellido: " + rs.getString(2) + "\n");
            System.out.println(" Nombre: " + rs.getString(3) + "\n");
            System.out.println(" Profesion: " + rs.getString(4) + "\n");
            System.out.println(" Manager: " + rs.getString(5) + "\n");
            System.out.println(" Fecha de inicio: " + rs.getString(6) + "\n");
            System.out.println(" Salario: " + rs.getString(7) + " €\n");
            System.out.println(" Horas extra: " + rs.getString(8) + " €\n");
            System.out.println("======================================\n");
        }
    }
    /**
     * Este métodos nos pedirá un empleado concreto y mostrará todos sus datos.
     * @param con Recibe la conexión a la BBDD.
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void mostrar_concreto(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el número del empleado que quieres mostrar: ");
        int id = sc.nextInt();
        Statement st = con().createStatement();
        ResultSet rs = st.executeQuery("Select * from employees where num = " + id);

        System.out.println("Los datos del empleado son los siguientes\n");
        System.out.print("======================================\n");
        while (rs.next()) {
            System.out.println(" ID: " + rs.getString(1) + "\n");
            System.out.println(" Apellido: " + rs.getString(2) + "\n");
            System.out.println(" Nombre: " + rs.getString(3) + "\n");
            System.out.println(" Profesion: " + rs.getString(4) + "\n");
            System.out.println(" Manager: " + rs.getString(5) + "\n");
            System.out.println(" Fecha de inicio: " + rs.getString(6) + "\n");
            System.out.println(" Salario: " + rs.getString(7) + " €\n");
            System.out.println(" Horas extra: " + rs.getString(8) + " €\n");
            System.out.println("======================================\n");
        }

    }
    /**
     * Este método nos mostrará la media del salario entre todos los empleados
     * @param con Recibe la conexión a la BBDD.
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void mostrar_media_salarios(Connection con) throws SQLException {
        Statement st = con().createStatement();
        ResultSet rs = st.executeQuery("Select * from employees");
        int contador = 0;
        int total_salarios = 0;
        while (rs.next()) {
            contador++;//Por cada iteracción sumamos uno para obtener el número de empleados totales para hacer la media.
            total_salarios += rs.getInt(7);
        }
        System.out.println("De los "+ contador + " empleados la media salarial es: " + total_salarios/contador +" €\n");
    }
}

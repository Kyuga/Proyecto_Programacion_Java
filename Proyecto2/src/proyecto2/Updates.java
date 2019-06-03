/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import static proyecto2.Proyecto2.con;

/**
 *
 * @author RubénLlorenç
 */
public class Updates {
    /**
     * Este método ejecutara la opción seleccionada por el usuario.
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void pedirUpdates(Connection con) throws SQLException {
        boolean exit = true;
        while (exit) {
            switch (menuUpdates()) {
                case 1:
                    modificar_sueldo(con);
                    break;
                case 2:
                    subir_sueldo_managers(con);
                    break;
                case 3:
                    modificar_horas_extra(con);
                    break;
                case 4:
                    exit = false;
                    break;
            }
        }
    }
    /**
     * Este método imprime el menú de posibles modificaciones de la BBDD y nos pide que opción queremos realizar.
     * @return Nos devuelve la opción seleccionada 
     */
    public static int menuUpdates() {
        Scanner sc = new Scanner(System.in);
        System.out.println("--------------------------");
        System.out.println("1) Modificar sueldo empleado concreto");
        System.out.println("2) Subir sueldo managers (según %)");
        System.out.println("3) Modificar horas extras empleado concreto");
        System.out.println("4) Atrás");
        System.out.println("--------------------------");
        int x = sc.nextInt();
        return x;
    }

    /**
     * Este métodos nos pedirá un número de empleado y podremos cambiar su sueldo actual por otro de nuevo.
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void modificar_sueldo(Connection con) throws SQLException {
        Statement st = con().createStatement();
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el numero del empleado a modificar: ");
        int num = sc.nextInt();
        //Imprimimos por pantalla el sueldo actual
        ResultSet rs1 = st.executeQuery("Select * from employees where num = " + num);
        while (rs1.next()) {
            System.out.println("El sueldo actual del empleado es: " + rs1.getString(7));
        }
        System.out.println("----------------------------------------");
        //Pedimos al usuario un sueldo nuevo y lo modificamos
        System.out.println("Introduce el nuevo sueldo: ");
        int nuevo_salario = sc.nextInt();
        PreparedStatement pst = con.prepareStatement("Update employees set salary=" + nuevo_salario + " where num = " + num);
        pst.executeUpdate();//Ejecutamos la sentencia.
    }
    /**
     * Este método nos permite subir o bajar mediante un % el sueldo a los managers
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void subir_sueldo_managers(Connection con) throws SQLException {
        Statement st = con().createStatement();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println("¿Quieres realizar un incremento de sueldo o una disminución?(i/d)");
            String respuesta = sc.next();
            //Lo pasamos todo a minúscula para aceptar cualquier input.
            if (respuesta.toLowerCase().equals("i")) {
                System.out.println("¿Qué % de augmento deseas aplicar?");
                double porcentaje = sc.nextDouble();
                ResultSet rs1 = st.executeQuery("Select * from employees where num= " + 26);
                //Mostramos el sueldo actual por pantalla y a la vez lo modificamos segun el %
                while (rs1.next()) {
                    System.out.println("El sueldo actual es: " + rs1.getString(7));
                    System.out.println("==========================");
                    PreparedStatement pst = con.prepareStatement("Update employees set salary=" + (rs1.getInt(7) + (rs1.getInt(7) * porcentaje / 100)) + " where num = 26");
                    pst.executeUpdate();//Ejecutamos la sentencia
                }
                //Mostramos el nuevo sueldo por pantalla
                ResultSet rs2 = st.executeQuery("Select * from employees where num=" + 26);
                while(rs2.next()) {
                    System.out.println("El nuevo sueldo es: " + rs2.getString(7));
                }
                salir = true;
            } else if (respuesta.toLowerCase().equals("d")) {
                System.out.println("¿Qué % de disminución quieres aplicar?");
                double porcentaje = sc.nextDouble();
                ResultSet rs1 = st.executeQuery("Select * from employees where num= " + 26);
                //Mostramos el sueldo actual por pantalla y a la vez lo modificamos segun el %
                while (rs1.next()) {
                    System.out.println("El sueldo actual es: " + rs1.getString(7));
                    System.out.println("==========================");
                    PreparedStatement pst = con.prepareStatement("Update employees set salary=" + (rs1.getInt(7) - (rs1.getInt(7) * porcentaje / 100)) + " where num = 26");
                    pst.executeUpdate();//Ejecutamos la sentencia
                }
                //Mostramos el nuevo sueldo por pantalla
                ResultSet rs2 = st.executeQuery("Select * from employees where num=" + 26);
                while(rs2.next()) {
                    System.out.println("El nuevo sueldo es: " + rs2.getString(7));
                }
                salir = true;
            }
        }
    }
    /**
     * Este métodos nos pedirá un número de empleado y podremos cambiar sus horas extras de este mes por otro valor.
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void modificar_horas_extra(Connection con) throws SQLException {
        Statement st = con().createStatement();
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el numero del empleado a modificar: ");
        int num = sc.nextInt();
        //Mostramos las horas del mes actuales
        ResultSet rs1 = st.executeQuery("Select * from employees where num = " + num);
        while (rs1.next()) {
            System.out.println("Las horas extra actuales del empleado son: " + rs1.getString(8));
        }
        System.out.println("----------------------------------------");
        System.out.println("Introduce las nuevas horas extra: ");
        int nuevas_horas = sc.nextInt();
        PreparedStatement pst = con.prepareStatement("Update employees set overtime=" + nuevas_horas + " where num = " + num);
        pst.executeUpdate();//Ejecutamos la sentencia
    }
}

package proyecto2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;
import static proyecto2.Proyecto2.con;

/**
 *
 * @author RubénLlorenç
 */
public class Proyecto2 {

    /**
     *
     * En el main ejecutamos la opción seleccionada y tratamos las excepciones.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            //Conectamos a la BBDD
            try (Connection con = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/RubenLlorenc", "root", "")) {
                switch (pedir_opcion()) {
                    case 1:
                        System.out.print("Dame el número de empleado: \n");
                        int id = sc.nextInt();
                        Nomina.imprimir(id, con);
                        break;
                    case 2:
                        Updates.pedirUpdates(con);
                        break;
                    case 3:
                        añadir_empleado(con);
                        break;
                    case 4:
                        System.out.print("Dame el número de empleado: ");
                        int num = sc.nextInt();
                        despedir_empleado(num, con);
                        break;
                    case 5:
                        Consultas.pedirConsultas(con);
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Introduce una opción valida");
                }
                //Control de excepciones--------------------
            } catch (SQLException ex) {
                System.out.println("Error en la base de datos.");
                System.out.println(ex.getSQLState());
                System.out.println(ex.getMessage());
                System.out.println(Arrays.toString(ex.getStackTrace()));
            } catch (IOException ex) {
                System.out.println("Error de entrada/salida del fitxero.");
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error no controlado.");
                System.out.println(ex.getMessage());
                System.out.println(Arrays.toString(ex.getStackTrace()));
            } 
        }
    }

    /**
     * Este método nos mostrará el menú principal y nos pedirá la opción a
     * ejecutar.
     * @return Devuelve la opción del menú seleccionada por el usuario
     */
    public static int pedir_opcion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("--------------------------");
        System.out.println("1) Imprimir nómina");
        System.out.println("2) Modificar base de datos");
        System.out.println("3) Añadir empleado");
        System.out.println("4) Despedir empleado");
        System.out.println("5) Consultar datos");
        System.out.println("6) Salir");
        System.out.println("--------------------------");
        int x = sc.nextInt();
        return x;
    }

    /**
     * Conexión a la BBDD
     *
     * @return Devuelve la conexión
     * @throws SQLException Sube la posible excepción de tipo SQL al main para
     * tratarla.
     */
    public static Connection con() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/RubenLlorenc";
        Connection con = (Connection) DriverManager.getConnection(url, "root", "");
        return (con);
    }

    /**
     * Este método nos permitirá eliminar un empleado de la BBDD
     *
     * @param num Recibe por parámetro el número de empleado ya que en este
     * método la acción es sobre un solo empleado
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para
     * tratarla.
     */
    public static void despedir_empleado(int num, Connection con) throws SQLException {
        Statement st = con().createStatement();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        //Mostramos los datos del empleado antes de confirmar su despido por si nos equivocamos de número
        ResultSet rs1 = st.executeQuery("Select * from employees where num = " + num);
        System.out.println("Los datos del empleado que va a despedir son los siguientes: \n");
        while (rs1.next()) {
            System.out.println(" ID: " + rs1.getString(1) + "\n");
            System.out.println(" Apellido: " + rs1.getString(2) + "\n");
            System.out.println(" Nombre: " + rs1.getString(3) + "\n");
            System.out.println(" Profesion: " + rs1.getString(4) + "\n");
            System.out.println(" Manager: " + rs1.getString(5) + "\n");
            System.out.println(" Fecha de inicio: " + rs1.getString(6) + "\n");
            System.out.println(" Salario: " + rs1.getString(7) + " €\n");
            System.out.println(" Horas extra: " + rs1.getString(8) + " €\n");
            System.out.println("----------------------------------------\n");
        }
        //Confirmamos el despido del empleado
        while (!salir) {
            System.out.println("Seguro que desea continuar? (y/n)");
            String respuesta = sc.next();
            if (respuesta.toLowerCase().equals("y")) {
                ResultSet rs2 = st.executeQuery("Select * from employees where num = " + num);
                PreparedStatement pst = con.prepareStatement("Delete from employees where num = " + num);
                pst.execute();
                //Imprimimos por pantalla la confirmación del despido junto al nombre del empleado en questión.
                while (rs2.next()) {
                    System.out.println("El empleado " + rs2.getString(3) + " " + rs2.getString(2) + " ha sido despedido con éxito.");
                }
                salir = true;
            } else if (respuesta.toLowerCase().equals("n")) {
                System.out.println("El empleado introducido NO será despedido");
                salir = true;
            }
        }
    }

    /**
     * Este método nos irá pidiendo los diferentes campos y añadiremos un
     * empleado nuevo a la plantilla.
     *
     * @param con Recibe la conexión a la BBDD
     * @throws SQLException Sube la posible excepción de tipo SQL al main para
     * tratarla.
     */
    public static void añadir_empleado(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        PreparedStatement ps = con.prepareStatement("Insert into employees values (?,?,?,?,?,?,?,?,?,?)");
        System.out.println("Inserta los datos del empleado nuevo: ");
        System.out.println("-------------------------------------");
        System.out.println("Inserta el ID: ");
        ps.setString(1, sc.next());
        System.out.println("Inserta el apellido: ");
        ps.setString(2, sc.next());
        System.out.println("Inserta el nombre: ");
        ps.setString(3, sc.next());
        System.out.println("Inserta la profesión: ");
        ps.setString(4, sc.next());
        System.out.println("Inserta el ID de su manager (solo si tiene): ");
        ps.setString(5, sc.next());
        System.out.println("Inserta la fecha de contratación (YYYY/MM/DD): ");
        ps.setString(6, sc.next());
        System.out.println("Inserta el salario: ");
        ps.setString(7, sc.next());
        System.out.println("Inserta las horas extras de este mes: ");
        ps.setString(8, sc.next());
        //Ya que tenemos dos opciones hacemos un bucle hasta que introduzcan una opción correcta.
        while (!salir) {
            System.out.println("Inserta el número de pagas extras que correspondan(2/3): ");
            int pagas = sc.nextInt();
            if (pagas == 2) {
                ps.setInt(9, pagas);
                salir = true;
            } else if (pagas == 3) {
                ps.setInt(9, pagas);
                salir = true;
            }
        }
        System.out.println("Inserta el DNI del empleado (8 números + 1 letra): ");
        ps.setString(10, sc.next());
        ps.executeUpdate(); //Finalmente ejecutamos el insert.
    }
}

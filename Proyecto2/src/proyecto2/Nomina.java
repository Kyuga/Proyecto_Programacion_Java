/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static proyecto2.Proyecto2.con;

/**
 *
 * @author RubénLlorenç
 */
public class Nomina {

    /**
     * Esté método nos generará una nómina de un empleado concreto.
     * @param id Recibe el número de empleado del que se hará la nómina
     * @param con Recibe la conexión a la BBDD
     * @throws java.io.IOException Sube la posible execptión de ficheros al main para tratarla.
     * @throws java.sql.SQLException Sube la posible excepción de tipo SQL al main para tratarla.
     */
    public static void imprimir(int id, Connection con) throws IOException, SQLException {
        
        //Usamos NumberFormat y DecimalFormat para forzar a los double a tener 2 decimales siempre. En caso de tener más de 2 decimales, redondeara
        NumberFormat formatter = new DecimalFormat("#0.00");

        String empresa = "BEE-WIDTH-YOO";
        Scanner sc = new Scanner(System.in);
        
        //-------LISTAS------------
        ArrayList<List> lista_complementos = new ArrayList<>();
        ArrayList<String> lista_nombres = new ArrayList<>();
        ArrayList<Integer> lista_valores = new ArrayList<>();

        //-------BBDD--------------
        Statement st = con().createStatement();
        ResultSet rs = st.executeQuery("Select * from employees where num = " + id);

        //-------CONSTANTES----------
        double tipo_contingencias = 4.70;
        double tipo_formacion = 0.10;

        double porcentaje_desempleo = 5.50;
        double porcentaje_ATEP = 1.65;
        double porcentaje_FP = 0.60;
        double porcentaje_fogasa = 0.20;
        double irpf_cotizacion = 23.60;

        //-----------CÓDIGO------------
        boolean salir = true;
        double tipo_desempleo = 0;
        
        //Pediremos al ususrio los siguientes inputs: Tipo de contrato, porcentaje de IRPF, Complementos salariales
        while (salir) {
            System.out.println("Introduce el tipo de contrato:");
            System.out.println("1.- Temporal");
            System.out.println("2.- Indefinido");
            int contr = sc.nextInt();

            if (contr == 1) {
                tipo_desempleo = 1.55;
                salir = false;
            } else if (contr == 2) {
                tipo_desempleo = 1.60;
                salir = false;
            }
        }
        //Pedimos el % de IRPF ya que varia para cada empleado
        System.out.println("");
        System.out.print("Introduce el porcentaje de IRPF: ");
        double tipo_irpf = sc.nextDouble();
        System.out.println("");
        //Pedimos los complementos a tener en cuenta para calcular la nómina
        System.out.print("Introduce el número de complementos salariales: ");
        int comp = sc.nextInt();
        System.out.println("");

        //Pedimos el nombre del complento y posteriormente su valor, guardandolos después en una lista
        if (comp > 0) {
            for (int i = 0; i < comp; i++) {
                System.out.print("Complemeto " + (i + 1) + ": ");
                String name_comp = sc.next();
                System.out.print("Valor: ");
                int valor_comp = sc.nextInt();
                System.out.println("");
                lista_nombres.add(name_comp);
                lista_valores.add(valor_comp);
            }
        }
        
        lista_complementos.add(lista_nombres);
        lista_complementos.add(lista_valores);

        int total_complementos = 0;

        //Recorremos la lista de los valores de los complementos para obtener la suma total de ellos.
        for (int i = 0; i < lista_nombres.size(); i++) {
            total_complementos += (int) (lista_complementos.get(1).get(i));
        }

        while (rs.next()) {

            String num = rs.getString(1);
            String apellidos = rs.getString(2);
            String nombre = rs.getString(3);
            int salario = rs.getInt(7);
            int horas_extra = rs.getInt(8);
            int pagas_extra = rs.getInt(9);
            String dni = rs.getString(10);

            int irpf = salario + total_complementos;
            int valor_paga_extra = (salario + total_complementos) * pagas_extra / 12;
            int base = salario + +total_complementos + valor_paga_extra;
            int base2 = base + horas_extra;

            int total_aportaciones = (int) (((base * tipo_contingencias) / 100) + ((base2 * tipo_desempleo) / 100) + ((base2 * tipo_formacion) / 100) + ((horas_extra * tipo_contingencias) / 100));
            int total_cotizacion = (int) ((((irpf + valor_paga_extra) * irpf_cotizacion) / 100) + ((base2 * porcentaje_ATEP) / 100) + ((base2 * porcentaje_desempleo) / 100) + ((base2 * porcentaje_FP) / 100) + ((base2 * porcentaje_fogasa) / 100) + ((horas_extra * irpf_cotizacion) / 100));
            
            //Creamos o editamos el archivo Nomina.txt y empezamos a escribir los datos en la nomina
            File archivoSalida = new File("Nomina.txt");
            try (FileWriter escritor = new FileWriter(archivoSalida)) {
                escritor.write("╔═══════════════════════════════════════════════╗\n");
                escritor.write("║Empresa: " + empresa + "           Empleado: " + nombre + " " + apellidos + "\n");
                escritor.write("║DNI: " + dni + "                   Nº: " + num + "\n");
                escritor.write("╠═══════════════════════════════════════════════╝\n");
                escritor.write("║                            DEVENGOS                                           \n");
                escritor.write("║                                                                 \n");
                escritor.write("║Salario base: " + formatter.format(salario) + " €\n");
                escritor.write("║Complementos salariales:\n");
                
                //Recorrera la lista con los complementos y sus valores, escribiendolos en el documento
                for (int i = 0; i < lista_nombres.size(); i++) {
                    escritor.write("║" + (String) lista_complementos.get(0).get(i) + "       ");
                    escritor.write(formatter.format((int) lista_complementos.get(1).get(i)) + " €\n");
                }
                
                escritor.write("║                                                                 \n");
                escritor.write("║                                                                 \n");
                escritor.write("║                                                                 \n");
                escritor.write("║                                                Total devengado: " + formatter.format(irpf) + " €\n");
                escritor.write("╠═══════════════════════════════════════════════╗\n");
                escritor.write("║                            DEDUCCIONES                                        \n");
                escritor.write("║                                   Bases             Tipo            \n");
                escritor.write("║Contingencias comunes:          " + formatter.format(base) + " €            " + formatter.format(tipo_contingencias) + " %" + "          " + formatter.format(((base * tipo_contingencias) / 100)) + " €\n");
                escritor.write("║Desempleo:                      " + formatter.format(base2) + " €            " + formatter.format(tipo_desempleo) + " %" + "          " + formatter.format(((base2 * tipo_desempleo) / 100)) + " €\n");
                escritor.write("║Formacion profesional:          " + formatter.format(base2) + " €            " + formatter.format(tipo_formacion) + " %" + "          " + formatter.format(((base2 * tipo_formacion) / 100)) + " €\n");
                escritor.write("║Horas extraordinarias:           " + formatter.format(horas_extra) + " €            " + formatter.format(tipo_contingencias) + " %" + "          " + formatter.format(((horas_extra * tipo_contingencias) / 100)) + " €\n");
                escritor.write("║                                                                 \n");
                escritor.write("║Total aportaciones:                                                  " + total_aportaciones + " €\n");
                escritor.write("║                                                                 \n");
                escritor.write("║IRPF:                           " + formatter.format(irpf) + " €" + "            " + tipo_irpf + " %" + "          " + formatter.format(((irpf * tipo_irpf) / 100)) + " €\n");
                escritor.write("║                                                                 \n");
                escritor.write("║                                                                 \n");
                escritor.write("║                                                Total a deducir:    " + formatter.format(total_aportaciones + ((irpf * tipo_irpf) / 100)) + " €\n");
                escritor.write("║                                                Total a percibir:   " + formatter.format(irpf - (total_aportaciones + ((irpf * tipo_irpf) / 100))) + " €\n");
                escritor.write("╠═══════════════════════════════════════════════╣\n");
                escritor.write("║                            BASES DE COTIZACION                                \n");
                escritor.write("║                                                                 \n");
                escritor.write("║Bases de cotización por contingencias comunes (BCcc): \n");
                escritor.write("║Remuneración mensual: " + irpf + "€\n");
                escritor.write("║Prorrata pagas extra: " + valor_paga_extra + "€          Tipo                Aportación empresa" + "\n");
                escritor.write("║            TOTAL:    " + (irpf + valor_paga_extra) + "€         " + formatter.format(irpf_cotizacion) + " %" + "                 " + formatter.format((((irpf + valor_paga_extra) * irpf_cotizacion) / 100)) + " €\n");
                escritor.write("║                                                                 \n");
                escritor.write("║                                    Base \n");
                escritor.write("║                                    " + base2 + " € \n");
                escritor.write("║                      AT y EP:                   " + formatter.format(porcentaje_ATEP) + " %     " + formatter.format(((base2 * porcentaje_ATEP) / 100)) + " €\n");
                escritor.write("║                    Desempleo:                   " + formatter.format(porcentaje_desempleo) + " %     " + formatter.format(((base2 * porcentaje_desempleo) / 100)) + " €\n");
                escritor.write("║                           FP:                   " + formatter.format(porcentaje_FP) + " %     " + formatter.format(((base2 * porcentaje_FP) / 100)) + " €\n");
                escritor.write("║                       FOGASA:                   " + formatter.format(porcentaje_fogasa) + " %     " + formatter.format(((base2 * porcentaje_fogasa) / 100)) + " €\n");
                escritor.write("║   Cotización por horas extra:      " + formatter.format(horas_extra) + " €     " + formatter.format(irpf_cotizacion) + " %    " + formatter.format(((horas_extra * irpf_cotizacion) / 100)) + " €\n");
                escritor.write("║                                                                 \n");
                escritor.write("║                                                           Total: " + formatter.format(total_cotizacion) + " €\n");
                escritor.write("╚═══════════════════════════════════════════════╝\n");
            }
        }

    }

}

package model;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Imprimir{
    Imprimir(int sala) throws FileNotFoundException, IOException{
        if(sala==1) {
            imprimir(new FileWriter("Sala1.html"), Main.getCalendari1());
        }else if(sala==2) {
            imprimir(new FileWriter("Sala2.html"), Main.getCalendari2());
        }
    }
    public void imprimir(FileWriter fw, Evento[][] calendari) throws FileNotFoundException, IOException  {
        //x es desfase del mes 0 = el mes empieza lunes 6 = " Domingo
        Integer desfase = Main.desfase;

        String strs[] = {"<html>\n"
                + "    <head>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <title></title>\n"
                + "        <style>\n"
                + "            body{\n"
                + "\n"
                + "            }\n"
                + "\n"
                + "            h1{\n"
                + "                position: relative;\n"
                + "                left: 18%;\n"
                + "            }\n"
                + "            .gris{\n"                            
                + "                background-color: grey;\n"
                + "            }\n"
                + "\n"
                + "            table{\n"
                + "                position: relative;\n"
                + "                left: 18%;\n"
                + "                border-spacing : 2px;\n"
                + "            }\n"
                + "\n"
                + "\n"
                + "\n"
                + "            .cabezera > td {\n"
                + "                background-color: appworkspace;\n"
                + "            }\n"
                + "\n"
                + "            td {\n"
                + "                padding : 20px;\n"
                + "                padding-left: 50px;\n"
                + "                padding-right: 50px;\n"
                + "                border-style : solid;\n"
                + "                border-width : 3px;\n"
                + "                border-color : #999999;\n"
                + "            }\n"
                + "            tr{\n"
                + "                text-align: center;\n"
                + "            }\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "\n"
                + "        <h1>"+Main.salidaInter.getAgenda()+"<br>"+Main.salidaInter.getMesos()[Main.configuracio.getMes()-1]+"</h1>\n"
                + "\n"
                + "    " + horario(0, desfase, calendari) + "    <br>"
                + "    " + horario(7, desfase, calendari) + "    <br>"
                + "    " + horario(14, desfase, calendari) + "   <br>"
                + "    " + horario(21, desfase, calendari) + "   <br>"
                + "    " + horario(28, desfase, calendari) + "   <br>"
                + "    " + horario(35, desfase, calendari) + "   <br>"
                + "    </body>\n"
                + "</html>"};

        fw.flush();
        for (int i = 0; i < strs.length; i++) {
            fw.write(strs[i] + "\n");
        }

        fw.close();
    }
    
    public static String horario(int semana, int desfase, Evento[][] calendari) {
        String[] bgcolor = new String[7];
        Integer[] diasemana = new Integer[7];
        for (int i = 0; i < 7; i++) {
            bgcolor[i] = desfaseColor(semana, desfase, (1 + i));
        }
        for (int i = 0; i < 7; i++) {
            diasemana[i] = desfaseInt(semana, desfase, (1 + i)-1);
        }
        String tabla = "<table>\n"
                + "            <tr class=\"cabezera\"><td><strong>Week:</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[0]+"</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[1]+"</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[2]+"</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[3]+"</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[4]+"</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[5]+"</strong></td>\n"
                + "                <td><strong>"+Main.salidaInter.getDias()[6]+"</strong></td></tr>\n"
                + "\n"
                + "            <tr>\n"
                + "                <td>Day:</td>\n"
                + "                <td>" + desfase(semana, desfase, 1) + "</td>\n"
                + "                <td>" + desfase(semana, desfase, 2) + "</td>\n"
                + "                <td>" + desfase(semana, desfase, 3) + "</td>\n"
                + "                <td>" + desfase(semana, desfase, 4) + "</td>\n"
                + "                <td>" + desfase(semana, desfase, 5) + "</td>\n"
                + "                <td>" + desfase(semana, desfase, 6) + "</td>\n"
                + "                <td>" + desfase(semana, desfase, 7) + "</td>\n"
                + "            </tr>\n"
                + "\n"
                +imprimirSemana(bgcolor, diasemana, calendari)
                + "        </table>";
        return tabla;
    }

    public static String desfase(int semana, int desfase, int dia) {
        if ((dia + semana - desfase) > 0 && (dia + semana - desfase) < ultimoDiaDelMes()) {
            return "" + (dia + semana - desfase);
        } else {
            return "";
        }
    }
    
    public static Integer desfaseInt(int semana, int desfase, int dia) {
        if ((dia + semana - desfase) >= 0 && (dia + semana - desfase) < ultimoDiaDelMes()) {
            return (dia + semana - desfase);
        } else {
            return -1;
        }
    }
    
    public static String clase(int x, int y, Evento[][] calendari) {
        if(y==-1)
            return "";
        else
            return calendari[x][y].getAssignatura();
    }
    
    public static String desfaseColor(int semana, int desfase, int dia) {
        if ((dia + semana - desfase) > 0 && (dia + semana - desfase) < ultimoDiaDelMes()) {
            return "";
        } else {
            return "gris";
        }
    }
    
    public static String imprimirHoraSemana(int hora, String[] bgcolor, Integer[] diasemana, Evento[][] calendari){
        String fin="";
        for(int i = 0;i<7;i++) { 
            fin+="                <td class=" + bgcolor[i] + ">"+clase(hora, diasemana[i], calendari)+"</td>\n";
            }
        return fin;
    }
    public static String imprimirTabla(int i, String[] bgcolor, Integer[] diasemana, Evento[][] calendari) {
        String fin = "";
        fin+=
        "            <tr>\n"
        + "                <td>"+i+":"+(i+1)+"</td>\n"
        + imprimirHoraSemana(i, bgcolor, diasemana, calendari)
        + "            </tr>\n"
        + "\n";
        return fin;
    }
    public static String imprimirSemana(String[] bgcolor, Integer[] diasemana, Evento[][] calendari) {
        String fin="";
        for(int i = 0;i<24;i++) { 
            fin+=imprimirTabla(i, bgcolor, diasemana, calendari);
            }
        return fin;
    }

    //Funcion que devuelve el ultimo dia de un mes
    public static int ultimoDiaDelMes() {
        Calendar dia = Calendar.getInstance();
        dia.set(Main.configuracio.getAny(), Main.configuracio.getMes(), 1);
        return dia.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
package model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
	static Config configuracio = new Config();
	static Internacional entradaInter=new Internacional();
	static Internacional salidaInter=new Internacional();
	static Integer desfase;
	static ArrayList<Peticion> peticiones = new ArrayList<Peticion>();
	static ArrayList<Evento> eventos = new ArrayList<Evento>();
	static ArrayList<Incidencia> incidencias = new ArrayList<Incidencia>();
	static final Evento[][] calenSala1 = new Evento[24][Imprimir.ultimoDiaDelMes()];
	static final Evento[][] calenSala2 = new Evento[24][Imprimir.ultimoDiaDelMes()];

	public static void main(String[] args) throws IOException, ParseException {
		//*******************************entrada de archivos********************************************
		llenarCalen1();
		llenarCalen2();
		leerConfig();
		desfase=takeDia(configuracio.getAny(), configuracio.getMes(), 1);
		leerInternationalEntrada();
		leerInternationalSalida();
		crearPeticiones();
		crearEventos();
		imprimirIncidencia(incidencias);

		new Imprimir(1);
		new Imprimir(2);
	}

	private static boolean comprobarDatos(String datos) throws ParseException {

		String[] parts = datos.split(" ");
		boolean goodData = true;
		//FECHAS ---------------------------------->
		//Separar -----------------------------------------------------------------------------------
		/*-*/String[] dateAux = parts[2].split("-");//Separamos la fecha de inicio y de fin
		/*-*/String[] dateInicio = dateAux[0].split("/");//En la fecha de inicio quitamos las barras
		/*-*/String[] dateFinal = dateAux[1].split("/");//En la fecha de fin quitamos las barras
		//-------------------------------------------------------------------------------------------
		//Comprobar ---------------------------------------------------------------------------------
		/*-*/LocalDate dateEntradaIni = LocalDate.of(Integer.parseInt(dateInicio[2]), Integer.parseInt(dateInicio[1]), Integer.parseInt(dateInicio[0]));//Creamos un local date de la fecha de inicio
		/*-*/LocalDate dateEntradaFin = LocalDate.of(Integer.parseInt(dateFinal[2]), Integer.parseInt(dateFinal[1]), Integer.parseInt(dateFinal[0]));//Creamos un local date de la fecha de fin
		/*-*/LocalDate actualDate = LocalDate.now(); //Cogemos la hora actual
		/*-*/System.out.println(dateEntradaIni.compareTo(actualDate)); //Miramos que la fecha introducida no sea anterior a la actual. ERROR: La fecha introducida es anterior a la actual

		if (dateEntradaIni.compareTo(actualDate) == -1){
			//ERROR: No puede introducir una fecha anterior a la actual
			new Incidencia("No puede introducir una fecha anterior a la actual");
			return false;
		}else if (dateEntradaIni.compareTo(actualDate) == 0){
			//ERROR: No puede introducir una fecha igual a la actual
			new Incidencia("Minimo un dia de antelacion para las reservas");
			return false;
		}
		/*-*/System.out.println(dateEntradaFin.compareTo(dateEntradaIni));//Miramos que la fecha sin sea mayor a la de inicio. ERROR la fecha de fin es anterior a la inicial
		//NOTA: compareTo devuelve -1 cuando es menor, 0 si es la misma fecha y 1 cuando es mayor en las dos a de dar 1
		if (dateEntradaFin.compareTo(dateEntradaIni) == -1){
			//ERROR: No puede introducir una fecha fin anterior a la fecha inicio
			new Incidencia("No puede introducir una fecha fin anterior a la fecha inicio");
			return false;
		}//NOTA: puede cojer el mismo dia en este caso, es decir puede poner esto: 12/12/2018-12/12/2018
		//-------------------------------------------------------------------------------------------


		//HORAS ---------------------------------->
		//Separar -----------------------------------------------------------------------------------
		String[] horasAux = parts[4].split("_");//Creamos array con los intervalos (12-13, 15-18)
		//          System.out.println("Aux: " + Arrays.toString(horasAux));
		String hora1[] = new String[2];
		ArrayList<Integer> horaInicio = new ArrayList<>();
		ArrayList<Integer> horaFin = new ArrayList<>();
		if (horasAux.length == 1) {//Miramos cuantos parametros nos a pasado 
			hora1 = horasAux[0].split("-");
			//Comprobar ---------------------------------------------------------------------------------
			if(Integer.parseInt(hora1[0])>=Integer.parseInt(hora1[1])){
				//Error la primera hora a de ser menor que la segunda (12-13) NO: (13-12), (12-12)
				new Incidencia("La primera hora a de ser menor que la segunda");
				return false;
			}//NOTA: En el caso de que no coja el dia siguiente aqui ya lo miramos, ya que la primera hora a de ser menor que la segunda por lo que si pone (23-02) el primer numero es mayor que el segundo asi que error entra en el if
			//-------------------------------------------------------------------------------------------
			horaInicio.add(Integer.parseInt(hora1[0]));
			horaFin.add(Integer.parseInt(hora1[1]));
		} else if (horasAux.length == 2) {
			String hora2[] = new String[2];
			hora1 = horasAux[0].split("-");
			hora2 = horasAux[1].split("-");
			//Comprobar ---------------------------------------------------------------------------------
			if(Integer.parseInt(hora1[0])>=Integer.parseInt(hora1[1])){
				//Error la primera hora a de ser menor que la segunda (12-13) NO: (13-12), (12-12)
				new Incidencia("La primera hora a de ser menor que la segunda");
				return false;
			}else if(Integer.parseInt(hora2[0])>=Integer.parseInt(hora2[1])){
				//Error la primera hora a de ser menor que la segunda (12-13) NO: (13-12), (12-12)
				new Incidencia("La primera hora a de ser menor que la segunda");
				return false;
			}
			horaInicio.add(Integer.parseInt(hora1[0]));
			horaFin.add(Integer.parseInt(hora1[1]));
			horaInicio.add(Integer.parseInt(hora2[0]));
			horaFin.add(Integer.parseInt(hora2[1]));
			//-------------------------------------------------------------------------------------------
		} else if (horasAux.length == 3) {
			String hora2[] = new String[2];
			String hora3[] = new String[2];
			hora1 = horasAux[0].split("-");
			hora2 = horasAux[1].split("-");
			hora3 = horasAux[2].split("-");
			//Comprobar ---------------------------------------------------------------------------------
			if(Integer.parseInt(hora1[0])>=Integer.parseInt(hora1[1])){
				//Error la primera hora a de ser menor que la segunda (12-13) NO: (13-12), (12-12)
				new Incidencia("La primera hora a de ser menor que la segunda");
				return false;
			}else if(Integer.parseInt(hora2[0])>=Integer.parseInt(hora2[1])){
				//Error la primera hora a de ser menor que la segunda (12-13) NO: (13-12), (12-12)
				new Incidencia("La primera hora a de ser menor que la segunda");
				return false;
			}else if(Integer.parseInt(hora3[0])>=Integer.parseInt(hora3[1])){
				//Error la primera hora a de ser menor que la segunda (12-13) NO: (13-12), (12-12)
				new Incidencia("La primera hora a de ser menor que la segunda");
				return false;
			}
			horaInicio.add(Integer.parseInt(hora1[0]));
			horaFin.add(Integer.parseInt(hora1[1]));
			horaInicio.add(Integer.parseInt(hora2[0]));
			horaFin.add(Integer.parseInt(hora2[1]));
			horaInicio.add(Integer.parseInt(hora3[0]));
			horaFin.add(Integer.parseInt(hora3[1]));
			//-------------------------------------------------------------------------------------------
		}//Aqui no da ningun error ya que esto lo miramos en el regex que no pase mas de 3 parametros de hora
		//-------------------------------------------------------------------------------------------


		//DIAS ---------------------------------->
		//Comprobar ---------------------------------------------------------------------------------
		char x, y;
		int cont = 0;
		for(int i = 0; parts[3].length()>i; i++){//Cogemos una a una las letras para mirar si se repiten 
			x = parts[3].charAt(i);
			cont = 0;
			for(int e = 0; parts[3].length()>e; e++){//En el for anterior cogiamos una letra aqui volvemos a recorrer el String de dias para ver si se repite la letra
				y = parts[3].charAt(e);
				if(x == y){//En el caso de encontrar una letra igual el contador suma
					cont++;
				}
			}
			if(cont >1){//En principio el contador siempre sera 1 ya que almenos a de estar una vez esa letra en el String, pero si es mayor a 1 es que se repite la letra por lo tanto sales del bucle
				System.out.println("dia repetido");
				new Incidencia("Hay un dia repetido");
				return goodData = false;//ERROR: un dia se repite
			}
		}

		if(goodData == true){
			Calendar fechaI = Calendar.getInstance(Locale.UK);
			Calendar fechaF = Calendar.getInstance(Locale.UK);
			fechaI.set(Integer.parseInt(dateInicio[2]),Integer.parseInt(dateInicio[1]),Integer.parseInt(dateInicio[0]));
			fechaF.set(Integer.parseInt(dateFinal[2]),Integer.parseInt(dateFinal[1]) , Integer.parseInt(dateFinal[0]));

			Peticion peticion = new Peticion(parts[0], fechaI, fechaF, parts[1], parts[3], horaInicio, horaFin);
			peticiones.add(peticion);
		}
		return goodData;
	}

	public static boolean comprobarEntrada(String linePeticion) throws IOException, ParseException{
		//******* archivo peticiones********
		boolean goodData = true;

		//Introducimos datos
		//System.out.print("Reserva (Nombre Sala dia/mes/a�o-dia/mes/a�o dias h-h_h-h)");
		/*
		 *El regex tiene los siguientes parametros:
		 *1�: Acepta un String sin espacios (Nombre)
		 *2�: Acepta un String sin espacios (Sala)
		 *3�: Mira que la fecha tenga el siguiente parametro dd/mm/aaaa-dd/mm/aaaa, aparte mira que el mes no sea mayor a 12 y que el dia no sea mayor a 31
		 *4�: Acepta un String pero solo con las siguientes letras en mayusculas LMCJVSD en el caso de que haya una letra que no este no lo acepta
		 *5�: Revisa el formato de las horas hora-hora y que no superen las 23h solo de 0 a 23
		 */

		String regex  = "^[A-z]+ [a-zA-Z_0-9]+ (3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}-(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]+ ["+entradaInter.getDiasReduStr()+"]+ ([0-9]|1[0-9]|2[0-4])+-+([0-9]|1[0-9]|2[0-4])$";
		String regex2 = "^[A-z]+ [a-zA-Z_0-9]+ (3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}-(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]+ ["+entradaInter.getDiasReduStr()+"]+ ([0-9]|1[0-9]|2[0-4])+-+([0-9]|1[0-9]|2[0-4])+_+([0-9]|1[0-9]|2[0-4])+-+([0-9]|1[0-9]|2[0-4])$";
		String regex3 = "^[A-z]+ [a-zA-Z_0-9]+ (3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}-(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]+ ["+entradaInter.getDiasReduStr()+"]+ ([0-9]|1[0-9]|2[0-4])+-+([0-9]|1[0-9]|2[0-4])+_+([0-9]|1[0-9]|2[0-4])+-+([0-9]|1[0-9]|2[0-4])+_+([0-9]|1[0-9]|2[0-4])+-+([0-9]|1[0-9]|2[0-4])$";

		//Miramos que el formato de entrada sea el correcto
		//-------------------Regex 1-------------------
		Pattern patron = Pattern.compile(regex); //Expresion regular
		Matcher m = patron.matcher(linePeticion);//Miramos si tiene el mismo formato

		//-------------------Regex 2-------------------
		Pattern patron2 = Pattern.compile(regex2);
		Matcher m2 = patron2.matcher(linePeticion);

		//-------------------Regex 3-------------------
		Pattern patron3 = Pattern.compile(regex3);
		Matcher m3 = patron3.matcher(linePeticion);

		if (m.find()) {//-----Regex 1----
			goodData = comprobarDatos(linePeticion);//pasamos la linea de datos para separarlo y hacer una comprobacion mas detallada
		} else if (m2.find()) {//-----Regex 2-----
			goodData = comprobarDatos(linePeticion);
		} else if (m3.find()) {//-----Regex 3-----
			goodData = comprobarDatos(linePeticion);
		}else{ 
			System.out.println(1);
			//ERROR: la sentencia no respeta la estructura establecida. cada vez que hay un error, se debe crear un new Incidencia("mensaje de error")
			new Incidencia("La sentencia no respeta la estructura establecida");
			goodData = false;
		}
		return goodData;
	}

	public static void crearPeticiones() throws IOException, ParseException{
		FileReader fpeticones = new FileReader("peticiones.txt");
		BufferedReader bfpeticones = new BufferedReader(fpeticones);
		String line ;
		//        boolean correctLine = true;
		//System.out.println(line);
		//        ArrayList<String> linePeticion = new ArrayList<>();
		while ((line = bfpeticones.readLine()) != null) {
			comprobarEntrada(line);
		}
		//        for(int i=0;i<linePeticion.size();i++){
		//            correctLine = comprobarEntrada(linePeticion.get(i));
		//        }
		//        System.out.println("todo correcto");
		bfpeticones.close();
	}


	public static void crearEventos(){
		for (Peticion petic : peticiones) {
			if(petic.getAssignatura().equals("Cerrado")) {
				if(petic.getAula().equals("Sala1")) {
					ArrayList<Integer> horas = extraerHoras(petic);
					ArrayList<Integer> dias = dias(petic.getFechaInicio(), petic.getFechaFin(), configuracio.getMes());
					String mascaraDias = petic.getMascaraDias();
					for (Integer dia : dias) {
						if(mascaraDias.contains(entradaInter.getDiasRedu()[takeDia(configuracio.getAny(), configuracio.getMes(), dia)])) {
							for (Integer hora : horas) {
								calenSala1[hora][dia-1]=new Evento(petic.getAssignatura());
							}
						}
					}
				}else if(petic.getAula().equals("Sala2")) {
					ArrayList<Integer> horas = extraerHoras(petic);
					ArrayList<Integer> dias = dias(petic.getFechaInicio(), petic.getFechaFin(), configuracio.getMes());
					String mascaraDias = petic.getMascaraDias();
					for (Integer dia : dias) {
						if(mascaraDias.contains(entradaInter.getDiasRedu()[takeDia(configuracio.getAny(), configuracio.getMes(), dia)])) {
							for (Integer hora : horas) {
								calenSala2[hora][dia-1]=new Evento(petic.getAssignatura());
							}
						}
					}
				}
			}else{
				if(petic.getAula().equals("Sala1")) {
					ArrayList<Integer> horas = extraerHoras(petic);
					ArrayList<Integer> dias = dias(petic.getFechaInicio(), petic.getFechaFin(), configuracio.getMes());
					String mascaraDias = petic.getMascaraDias();
					for (Integer dia : dias) {
						if(mascaraDias.contains(entradaInter.getDiasRedu()[takeDia(configuracio.getAny(), configuracio.getMes(), dia)])) {
							for (Integer hora : horas) {
								if(calenSala1[hora][dia-1].equals(new Evento(""))){
									calenSala1[hora][dia-1]=new Evento(petic.getAssignatura());
								}
								else {
									new Incidencia("Peticion denegada por solicitar una hora ya ocupada");
								}

							}
						}
					}
				}else if(petic.getAula().equals("Sala2")) {
					ArrayList<Integer> horas = extraerHoras(petic);
					ArrayList<Integer> dias = dias(petic.getFechaInicio(), petic.getFechaFin(), configuracio.getMes());
					String mascaraDias = petic.getMascaraDias();
					for (Integer dia : dias) {
						if(mascaraDias.contains(entradaInter.getDiasRedu()[takeDia(configuracio.getAny(), configuracio.getMes(), dia)])) {
							for (Integer hora : horas) {
								if(calenSala2[hora][dia-1].equals(new Evento(""))){
									calenSala2[hora][dia-1]=new Evento(petic.getAssignatura());
								}
								else {
									new Incidencia("Peticion denegada por solicitar una hora ya ocupada");
								}
							}
						}
					}
				}
			}
		}
		/*  for (Peticion petic : peticiones) {


        }*/
	}

	public static void imprimirIncidencia(ArrayList<Incidencia> incidencia) throws IOException{
		FileWriter fImprimir = new FileWriter("Incidencias.txt");
		for (int i = 0; i < incidencia.size(); i++) {
			fImprimir.write(incidencia.get(i).getMsg()+ "\n");
		}
		fImprimir.close();
	}

	public static void leerConfig() throws IOException {
		//******* archivo configuracion********
		FileReader config = new FileReader("config.txt");
		BufferedReader fconfig = new BufferedReader(config);
		String line ;
		//System.out.println(line);
		ArrayList<String> conf = new ArrayList<>();
		while ((line = fconfig.readLine()) != null) {
			conf.add(line);
		}
		for(int i=0;i<conf.size();i++){
			String[] aux = conf.get(i).split(" ");//linea de config.txt separado por espacios
			if(i == 0){
				configuracio.setAny(Integer.parseInt(aux[0]));
				configuracio.setMes(Integer.parseInt(aux[1]));
			} else if(i == 1){
				configuracio.setIdiomaEntrada(aux[0]);
				configuracio.setIdiomaSalida(aux[1]);
			}
		}
		fconfig.close();
	}

	public static void leerInternationalEntrada() throws IOException {
		//******* archivo international entrada********
		FileReader entra = new FileReader("internacional."+configuracio.getIdiomaEntrada()+".txt");
		BufferedReader fentra = new BufferedReader(entra);
		String line;
		while ((line = fentra.readLine()) != null) {
			String[] aux = line.split(";");
			switch (aux[0]) {
			case "001":
				entradaInter.setAgenda(aux[1]);
				break;
			case "002":
				entradaInter.setDias(aux[1].split(","));
				break;
			case "003":
				entradaInter.setDiasReduStr(aux[1]);
				entradaInter.setDiasRedu(aux[1].split(","));
				break;
			case "004":
				entradaInter.setMesos(aux[1].split(","));
				break;
			default:
				break;
			}
		}
		fentra.close();
	}

	public static void leerInternationalSalida() throws IOException {
		//******* archivo international sortida********
		FileReader salida = new FileReader("internacional."+configuracio.getIdiomaSalida()+".txt");
		BufferedReader fsalida = new BufferedReader(salida);
		String line;
		while ((line = fsalida.readLine()) != null) {
			String[] aux = line.split(";");
			switch (aux[0]) {
			case "001":
				salidaInter.setAgenda(aux[1]);
				break;
			case "002":
				salidaInter.setDias(aux[1].split(","));
				break;
			case "003":
				salidaInter.setDiasReduStr(aux[1]);
				salidaInter.setDiasRedu(aux[1].split(","));
				break;
			case "004":
				salidaInter.setMesos(aux[1].split(","));
				break;
			default:
				break;
			}
		}
		fsalida.close();
	}
	//agafa una peticio i extre les hores de inici del event del rang que te
	public static ArrayList<Integer> extraerHoras(Peticion peticion){
		ArrayList<Integer> horas = new ArrayList<Integer>();
		for(int i=0; i<peticion.getHorasInicio().size(); i++){
			//            while(peticion.getHorasInicio().get(i)<peticion.getHorasFin().get(i)){}
			for(int z=peticion.getHorasInicio().get(i); peticion.getHorasFin().get(i)>z;z++) {
				horas.add(z);
			}
		}
		return horas;
	}

	public static Evento[][] getCalendari1() {
		return calenSala1;
	}
	public static Evento[][] getCalendari2() {
		return calenSala2;
	}
	public static int takeDia(int year, int month, int dia){

		//now.clear();
		Calendar cal = Calendar.getInstance(Locale.UK);
		//0 = enero 11 = diciembre
		cal.setLenient(false);    
		cal.set(year,month,dia);
		//orden de las dias
		// 1=domingo 2=lunes 7=sabado
		//        String[] strDays = {"Sunday" , "Monday", "Tuesday",
		//                "Wednesday", "Thusday", "Friday", "Saturday" };
		//obtiene el numero de dia
		if(cal.get(Calendar.DAY_OF_WEEK)==1)
			return 6;
		return cal.get(Calendar.DAY_OF_WEEK)-2;

	}

	//funcion que devuelve de un evento los dias que ocupa
	public static ArrayList<Integer> dias(Calendar inicio, Calendar fin, int mes) {
		ArrayList<Integer> dias = new ArrayList<>();
		int primerDia = 0;
		int ultimoDia = 0;
		//si el mes de inicio esta mal se cambia
		if (inicio.get(Calendar.MONTH) != mes) {
			inicio.set(inicio.get(Calendar.YEAR), mes, inicio.get(Calendar.DAY_OF_MONTH));
		}
		//mira si el mes 1 es > que el mes 2 y si lo es, no devuelve nada
		if (inicio.get(Calendar.MONTH) > fin.get(Calendar.MONTH)) {
			dias = null;

			//mira si el mes 2 es < que el mes 1
			//si es asi cambia el dia y el mes del 2 
			//para que sea el mismo mes que el 1 y que sea su ULTIMO dia
		} else if (inicio.get(Calendar.MONTH) < fin.get(Calendar.MONTH)) {

			fin.set(inicio.get(Calendar.YEAR), inicio.get(Calendar.MONTH), inicio.getActualMaximum(Calendar.DAY_OF_MONTH));

			primerDia = inicio.get(Calendar.DAY_OF_MONTH);
			ultimoDia = fin.get(Calendar.DAY_OF_MONTH);

			while (primerDia <= ultimoDia) {
				dias.add(primerDia);
				primerDia++;
			}

			//mira si los meses coinciden
		} else if (inicio.get(Calendar.MONTH) == fin.get(Calendar.MONTH)) {

			//si el dia de la variable 1 es < que el de la variable 2
			if (inicio.get(Calendar.DAY_OF_MONTH) < fin.get(Calendar.DAY_OF_MONTH)) {

				primerDia = inicio.get(Calendar.DAY_OF_MONTH);
				ultimoDia = fin.get(Calendar.DAY_OF_MONTH);

				while (primerDia <= ultimoDia) {
					dias.add(primerDia);
					primerDia++;
				}

				//si el dia de la variable 1 es > que el de la variable 2
			} else if (inicio.get(Calendar.DAY_OF_MONTH) > fin.get(Calendar.DAY_OF_MONTH)) {
				dias = null;
			}
		}
		//devuelve un ArrayList
		return dias;
	}

	public static void llenarCalen1() {
		for(int i = 0; i<calenSala1.length; i++) {
			for(int z = 0; z<calenSala1[i].length;z++) {
				calenSala1[i][z]=new Evento("");
			}
		}
	}
	public static void llenarCalen2() {
		for(int i = 0; i<calenSala2.length; i++) {
			for(int z = 0; z<calenSala2[i].length;z++) {
				calenSala2[i][z]=new Evento("");
			}
		}
	}
}
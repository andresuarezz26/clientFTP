package control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientPI extends Thread {

	// Parámetros de la conexión de control
	private Socket sktControl;
	private PrintWriter outControl;
	private boolean cerrarConexion;
	private boolean isAscii=true;

	// Client DTP
	private ClientDTP clientDTP;

	public ClientPI(InetAddress dirServer, int portServer) 
	{

		try {
			sktControl = new Socket(dirServer, portServer);
			outControl = new PrintWriter(sktControl.getOutputStream(), true);
			cerrarConexion = false;

			clientDTP = new ClientDTP(dirServer, 4001);

		} catch (IOException e) {
			System.out.println("Error creando el socket");
		}
	}


	@Override
	public void run() 
	{

		while (cerrarConexion == false) {
			try {


				// Escritura de comandos
				System.out.print(">>");
				Scanner in = new Scanner(System.in);
				String texto = in.nextLine();
				String particion[] = texto.split(" ");

				// Finalizar la conexión con el servidor

				if (texto.equalsIgnoreCase("END")) {
					outControl.print("END");
					outControl.close();
					sktControl.close();
					cerrarConexion = true;
				}else
				{
					if(texto.equalsIgnoreCase("ascii"))
					{

						isAscii=true;						
						outControl.println("ascii");

					}else if(texto.equalsIgnoreCase("binary"))
					{

						isAscii=false;
						outControl.println("binary");
					}
					else if (particion.length == 2) {
						// Enviar comando
						outControl.println(texto);

						// Comando STOR
						if (particion[0].equalsIgnoreCase("STOR")) {
							if(isAscii){
								// Envíar archivo
								clientDTP.sendFile(clientDTP.getCurrentPath() + "/"
										+ particion[1],true);
							}else{


							}

						}

						// Comando RETR
						else if (particion[0].equalsIgnoreCase("RETR")) {

						}

					}
					// Enviar texto al servidor
					else {
						outControl.println(texto);
					}
				}






				// Recepción de respuestas
				if (!sktControl.isClosed()) {
					BufferedReader input = new BufferedReader(
							new InputStreamReader(sktControl.getInputStream()));
					// Mientras el buffer está vacío
					while (!input.ready()) {
					}

					// Obtener e imprimir el resultado
					String respuesta = input.readLine();
					//System.out.println(respuesta);

					if (respuesta.equalsIgnoreCase("successful_retr")) 
					{	
						// Recibir archivo
						clientDTP.receiveFile(particion[1]);			
					} 

					if (respuesta.equalsIgnoreCase("successful_dele")) 
					{	
						// Recibir archivo
						System.out.println("El archivo ha sido eliminado");			
					}
					if (respuesta.equalsIgnoreCase("successful_rnfr"))
					{

					}
					/*	if (respuesta.equalsIgnoreCase("successful_rename"))
					{

					}
					if (respuesta.equalsIgnoreCase("autentication_error"))
					{

					}
					if (respuesta.equalsIgnoreCase("autentication_success"))
					{

					}
					if (respuesta.equalsIgnoreCase("waiting_pass"))
					{

					}*/

					String divisionRespuesta[] = respuesta.split(";");

					for (String item : divisionRespuesta) {
						System.out.println(item);
					}

				}

			} catch (IOException e) {
				System.out
				.println("Hay un problema en el flujo de información");
			}

		}

		System.out.println("La conexión se ha cerrado");
	}
}

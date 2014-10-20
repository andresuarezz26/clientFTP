package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientPI extends Thread
{

	// Parámetros de la conexión de control
	private Socket sktControl;
	private PrintWriter outControl;
	private boolean cerrarConexion;
	private boolean isAscii = true;

	// Client DTP
	private ClientDTP clientDTP;

	public ClientPI(InetAddress dirServer, int portServer)
	{

		try
		{
			sktControl = new Socket(dirServer, portServer);
			outControl = new PrintWriter(sktControl.getOutputStream(), true);
			cerrarConexion = false;

			clientDTP = new ClientDTP(dirServer, 4001);

		} catch (IOException e)
		{
			System.out.println("Error creando el socket");
		}
	}

	@Override
	public void run()
	{

		while (cerrarConexion == false)
		{
			try
			{

				// Escritura de comandos
				System.out.print(">>");
				Scanner in = new Scanner(System.in);
				String texto = in.nextLine();
				String particion[] = texto.split(" ");

				// Comando END
				if (texto.equalsIgnoreCase("EXIT") || texto.equalsIgnoreCase("END"))
				{
					outControl.print("END");
					outControl.close();
					sktControl.close();
					in.close();
					cerrarConexion = true;
				}
				// Comando ASCII
				else if (texto.equalsIgnoreCase("ASCII"))
				{
					isAscii = true;
					outControl.println("ASCII");
				}
				// Comando BINARY
				else if (texto.equalsIgnoreCase("BINARY"))
				{
					isAscii = false;
					outControl.println("BINARY");
				}
				// Si el comando tiene un parámetro
				else if (particion.length == 2)
				{
					// Enviar comando
					outControl.println(texto);

					// Comando STOR
					if (particion[0].equalsIgnoreCase("STOR") || texto.equalsIgnoreCase("PUT"))
					{

						if (isAscii)
						{
							// Enviar archivo
							clientDTP.sendFile(clientDTP.getCurrentPath() + "/" + particion[1], true);
						} else
						{
							clientDTP.sendFile(clientDTP.getCurrentPath() + "/" + particion[1], false);
						}

					}

				}
				// Enviar texto al servidor
				else
				{
					outControl.println(texto);
				}

				// Recepción de respuestas
				if (!sktControl.isClosed())
				{
					BufferedReader input = new BufferedReader(new InputStreamReader(sktControl.getInputStream()));
					// Mientras el buffer estÃ¡ vacÃ­o
					while (!input.ready())
					{
					}

					// Obtener e imprimir el resultado
					String respuesta = input.readLine();
					// System.out.println(respuesta);

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

					// Se divide la respuesta
					String divisionRespuesta[] = respuesta.split(";");

					for (String item : divisionRespuesta)
					{
						System.out.println(item);
					}

				}

			} catch (IOException e)
			{
				System.out.println("Hay un problema en el flujo de información");
			}

		}

		System.out.println("La conexiÃ³n se ha cerrado");
	}
}
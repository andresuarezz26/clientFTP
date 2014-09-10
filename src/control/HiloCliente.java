package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class HiloCliente extends Thread
{

	// Parámetros de la conexión de control
	private Socket sktControl;
	private PrintWriter outControl;
	private boolean cerrarConexion;

	public HiloCliente(InetAddress dirServer, int portServer)
	{

		try
		{
			sktControl = new Socket(dirServer, portServer);
			outControl = new PrintWriter(sktControl.getOutputStream(), true);
			cerrarConexion = false;

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
				System.out.println("Ingrese su comando");
				Scanner in = new Scanner(System.in);
				String texto = in.nextLine();

				// Finalizar la conexión con el servidor
				if (texto.equalsIgnoreCase("FINISH"))
				{
					outControl.close();
					sktControl.close();
					cerrarConexion = true;
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
					// Mientras el buffer está vacío
					while (!input.ready())
					{
					}

					String respuesta = input.readLine();
					System.out.println(respuesta);
				}

			} catch (IOException e)
			{
				System.out.println("Hay un problema en el flujo de información");
			}

		}

		System.out.println("La conexión se ha cerrado");
	}

}

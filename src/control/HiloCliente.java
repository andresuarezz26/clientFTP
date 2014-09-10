package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class HiloCliente extends Thread
{

	private Socket skt;
	private PrintWriter out;
	private boolean cerrarConexion;

	public HiloCliente(InetAddress dirServer, int portServer)
	{

		try
		{
			skt = new Socket(dirServer,portServer);
			out = new PrintWriter(skt.getOutputStream(), true);
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
				System.out.println("Ingrese su comando");
				Scanner in = new Scanner(System.in);
				String texto = in.nextLine();

				// Finalizar la conexión con el servidor
				if (texto.equalsIgnoreCase("FINISH"))
				{
					out.close();
					skt.close();
					cerrarConexion = true;
				}
				// Enviar texto al servidor
				else
				{
					out.println(texto);
				}

			} catch (IOException e)
			{
				System.out.println("Hay un problema en el flujo de información");
			}

		}
		
		System.out.println("La conexión se ha cerrado");
	}

}

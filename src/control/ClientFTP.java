package control;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientFTP
{

	// Dirección IP del servidor
	static InetAddress localhost = null;

	public static void main(String[] args)
	{
		try
		{
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e1)
		{
			System.out.println("Error obteniendo la direccion localhost");
		}

		ClientPI hilo = new ClientPI(localhost, 4000);
		hilo.start();

	}

}

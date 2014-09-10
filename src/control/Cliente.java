package control;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente
{

	// Dirección IP del servidor
	static InetAddress localhost = null;
	// Socket
	static Socket skt;

	public static void main(String[] args)
	{
		try
		{
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e1)
		{
			System.out.println("Error obteniendo la direccion localhost");
		}

		HiloCliente hilo = new HiloCliente(localhost, 4000);
		hilo.start();

	}

}

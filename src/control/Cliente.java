package control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;



public class Cliente {
	
	//Dirección IP del servidor
	static InetAddress localhost = null;
	//Puerto del servidor
	static int puertoHilo = 0;
	//DatagramPacket para enviar y recibir paquetes
	static DatagramPacket dPacketRecibe, dPacketEnvio;
	//DatagramSocket
	static DatagramSocket dSocketCliente;

	public static void main(String[] args) 
	{
		try 
		{
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) 
		{
			System.out.println("Error obteniendo la direccion localhost");
		}

		try 
		{
			// Enviando info al servidor para conectarse
			dSocketCliente = new DatagramSocket();
			byte[] envio = new byte[250];
			dPacketEnvio = new DatagramPacket(envio, envio.length, localhost, 4000);
			dSocketCliente.send(dPacketEnvio);
			// Recibiendo respuesta para guardar parámetros del servidor
			byte[] buzon = new byte[250];
			dPacketRecibe = new DatagramPacket(buzon, buzon.length);
			dSocketCliente.receive(dPacketRecibe);
			puertoHilo = dPacketRecibe.getPort();

			//Leer cada mensaje, cifrarlo y enviarlo al servidor
			Scanner in = new Scanner(System.in);
			while (true)
			{
				System.out.println("Escribe tu mensaje");
				String mensaje = in.nextLine();
				byte[] mensajeCifradoDatos = mensaje.getBytes();
				dPacketEnvio = new DatagramPacket(mensajeCifradoDatos, mensajeCifradoDatos.length, localhost, puertoHilo);
				dSocketCliente.send(dPacketEnvio);
			}

		} catch (SocketException e) 
		{
			System.out.println("Error creando el socket");
		} catch (IOException e) 
		{
			System.out.println("Error en el flujo de informacion");
		}

	}





}

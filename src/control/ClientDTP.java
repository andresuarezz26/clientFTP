package control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDTP
{

	// Directorio raíz
	private String currentPath;

	private InetAddress dirServer;
	private int portServer;

	// Socket de Datos

	public ClientDTP(InetAddress dirServer, int portServer)
	{
		currentPath = System.getProperty("user.dir") + "/root";

		this.dirServer = dirServer;
		this.portServer = portServer;
	}

	public String getCurrentPath()
	{
		return currentPath;
	}

	public void setCurrentPath(String rootPath)
	{
		this.currentPath = rootPath;
	}

	/**
	 * Permite enviar un archivo al servidor
	 * 
	 * @param path
	 *            Ruta del archivo
	 */
	public void sendFile(String path)
	{

		try
		{

			File myFile = new File(path);
			if (myFile.exists() && myFile.isFile())
			{
				Socket sktData = new Socket(dirServer, portServer);
				OutputStream out = sktData.getOutputStream();

				byte[] mybytearray = new byte[(int) myFile.length() + 1];
				FileInputStream fis = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray, 0, mybytearray.length);
				System.out.println("Sending...");
				out.write(mybytearray, 0, mybytearray.length);

				out.flush();
				bis.close();
				out.close();
				sktData.close();
			} else
			{

				System.out.println("El archivo no existe");

			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Permite obtener el archivo envíado por el servidor
	 * 
	 * @param is
	 *            Flujo por el cual se recibe el archivo
	 * @param path
	 *            Ruta del archivo del cliente
	 * @throws Exception
	 *             Excepción en caso de que haya problemas en el flujo
	 */
	public void receiveFile(String path)
	{
		Socket sktData;
		try
		{
			sktData = new Socket(dirServer, portServer);
			InputStream is = sktData.getInputStream();

			// Obtener nombre del archivo
			String[] particion = path.split("/");
			String nombreArchivo = particion[particion.length - 1];

			int filesize = 6022386;
			int bytesRead;
			int current = 0;
			byte[] mybytearray = new byte[filesize];

			FileOutputStream fos = new FileOutputStream(getCurrentPath() + "/" + nombreArchivo);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			current = bytesRead;

			do
			{
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);

			bos.write(mybytearray, 0, current);
			bos.flush();
			bos.close();
			sktData.close();

			System.out.println("El archivo se recibió exitosamento");
		} catch (IOException e)
		{
			System.out.println("No se pudo recibir el archivo");
		}

	}
}

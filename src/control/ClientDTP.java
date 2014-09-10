package control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDTP
{

	// Directorio raíz
	private String currentPath;

	private InetAddress dirServer;
	private int portServer;

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
}

clientFTP
=========

Cliente FTP básico

Funcionalidades:

1. Enviar archivos a un sevidor FTP
2. Recibir archivos de un servidor FTP

Comandos que acepta el cliente:
1. CWD [ruta] - Cambiar de directorio
2. LIST [ruta] - Listar información de un archivo o un directorio
3. STOR <ruta-archivo> - Almacenar archivo en el servidor FTP
4. RETR <ruta-archivo> - Obtener archivo del servidor FTP
5. DELE <ruta-archivo> - Eliminar un archivo del servidor FTP
6. RNFR <ruta-archivo> - Especificar el archivo al cual se le va a cambiar el nombre
7. RNTO <ruta-archivo> - Especificar el nuevo nombre del archivo que se especificó con el comando RNFR
8. TYPE <type-character> - Especificar el tipo de transferencia (E para ASCII e I para binaria)

Nota: Es ideal que haya una interfaz gráfica

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ReceiveFile {
private String target_path;
private double downloaded;
	public ReceiveFile(String target_path)
	{
		this.target_path=target_path;
	}
	public void start()
	{
	try {
		ServerSocket ss=new ServerSocket(4000);
		Socket socket=ss.accept();
		InputStream input=socket.getInputStream();
		File dir=new File(target_path);
		dir.mkdirs();
		SimpleDateFormat df=new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
		Date date=new Date(System.currentTimeMillis());
        String name=df.format(date);
		OutputStream out=new FileOutputStream(dir+File.separator+name+".zip");
		byte b[]=new byte[1024];
		while((input.read(b))>0)
		{
			out.write(b,0,1024);
			int rem=100*input.available()/65535;
			downloaded=(100-rem)/100;
		}
		try {
			ZipFile zipFile=new ZipFile(dir+File.separator+name+".zip");
			zipFile.extractAll(dir+File.separator+name+File.separator);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
		input.close();
		socket.close();
		ss.close();
		File file=new File(dir+File.separator+name+".zip");
		file.delete();

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public double getProgress()
	{
		return downloaded;
	}

}

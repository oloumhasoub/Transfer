package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendFile {
private String filePath,ip_address;
private boolean status=false;
private double amount=0;
private double fileSize=0;
	public SendFile(String filePath,String ip_address)
	{
		this.filePath=filePath;
		this.ip_address=ip_address;
	}
	public void start()
	{
		try {
			Socket socket=new Socket(ip_address,4000);
			File dir = new File(filePath);
		    String zipDirName = "compressed.zip";
			ZipTool zipFiles = new ZipTool();
			if(dir.isDirectory())
			{
				zipFiles.zipDirectory(dir, zipDirName);
			}
			else
			{
				zipFiles.zipSingleFile(dir, zipDirName);
			}

		    File file=new File(zipDirName);
		    double filebytes=file.length();
		    fileSize=filebytes/1024;
			InputStream input=new FileInputStream(file);
			OutputStream out=socket.getOutputStream();
			byte b[]=new byte[1024];
			while((input.read(b))>0)
			{
				out.write(b,0,1024);
				amount=amount+1;
			}
			status=true;
			out.close();
			input.close();
			file.delete();
			socket.close();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
			System.out.println("Receiver not Found !!");
		}
	}
	public double getProgress()
	{
		if(fileSize!=0)
		{
			double res=(double) (amount/fileSize);
			return res;
		}
		else
		{
			return 0;
		}
		
	}
	public boolean getStatus()
	{
		return status;
	}
	

}

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.ProcessBuilder;
import java.lang.Thread;
import java.lang.InterruptedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Java extends HttpServlet
{
	static String getParameter(HttpServletRequest request, String Parameter, String[] EligibleValues, String DefaultValue)
	{
		String value;
		if((value = request.getParameter(Parameter)) == null)
			return DefaultValue;
		for (String str : EligibleValues) {
			if(str.equals(value))
				return value;
		}
		return DefaultValue;
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		String msg = request.getParameter("msg");
		ProcessBuilder builder = new ProcessBuilder("cmd","/c","start cmd.exe /K echo " + msg);
		builder.start();
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin","*");
		PrintWriter writer = response.getWriter();
		writer.println("{\"msg\":[\"" + msg + "\"]}");
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		String SessionID = request.getSession().getId();

		String InputMode = getParameter(request, "Input-Mode", new String[]{"Enable", "Disable"}, "Disable");
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin","*");
		PrintWriter writer = response.getWriter();
		
		File directory = new File(System.getenv("SERVER_HOME") + "\\temp\\" + SessionID);
		directory.mkdir();
		File workfile = new File(System.getenv("SERVER_HOME") + "\\temp\\" + SessionID + "\\Work.java");
		workfile.createNewFile();
		File out = new File(System.getenv("SERVER_HOME") + "\\temp\\" + SessionID + "\\out.txt");
		out.createNewFile();
		File compile = new File(System.getenv("SERVER_HOME") + "\\temp\\" + SessionID + "\\compile.txt");
		compile.createNewFile();

		FileOutputStream fout = new FileOutputStream(workfile);
		InputStream iread = request.getInputStream();
		byte[] bytestream = iread.readAllBytes();
		fout.write(bytestream);
		iread.close();
		fout.close();

		Process workproc;
		if(InputMode.equals("Enable"))
		{
			ProcessBuilder work = new ProcessBuilder("cmd","/c","start cmd /k \"" + System.getenv("SERVER_HOME").charAt(0) + ": && cd " + System.getenv("SERVER_HOME") + " && cd core && runwork " + SessionID + "\"");
			workproc = work.start();
		}
		else
		{
			ProcessBuilder work = new ProcessBuilder("cmd","/c",System.getenv("SERVER_HOME").charAt(0) + ": && cd " + System.getenv("SERVER_HOME") + " && cd core && runwork " + SessionID);
			workproc = work.start();
		}
		try
		{
			Thread.sleep(3000);
		}
		catch(InterruptedException msg)
		{
			writer.println("{\"msg\":[\"" + msg.getMessage() + "\"]}");
		}
		workproc.destroy();
		
		String str;
		boolean first;
		
		writer.println("{");

		BufferedReader brcompile = new BufferedReader(new FileReader(compile));
		str = "";
		first = true;
		writer.println("\"compile log\":[");
		while((str = brcompile.readLine()) != null)
		{
			if(first)
			{
				first = false;
				writer.println(new String("\"" + str + "\""));
			}
			else
				writer.println(new String("," + "\"" + str + "\""));
		}
		writer.println("]");
		brcompile.close();

		writer.println(",");

		BufferedReader brout = new BufferedReader(new FileReader(out));
		str = "";
		first = true;
		writer.println("\"output\":[");
		while((str = brout.readLine()) != null)
		{
			if(first)
			{
				first = false;
				writer.println(new String("\"" + str + "\""));
			}
			else
				writer.println(new String("," + "\"" + str + "\""));
		}
		writer.println("]");
		brout.close();

		writer.println("}");

		workfile.delete();
		out.delete();
		compile.delete();
		File classfile = new File(System.getenv("SERVER_HOME") + "\\temp\\" + SessionID + "\\Work.class");
		classfile.delete();
		directory.delete();
		try
		{
			Thread.sleep(10);
		}
		catch(InterruptedException msg)
		{
			writer.println("{\"msg\":[\"" + msg.getMessage() + "\"]}");
		}
	}
}
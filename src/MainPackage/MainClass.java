package MainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainClass {
	

	private static final int BUFFER_SIZE = 4096;
	private static PrintWriter writer = null;


	public static int counter = 0;

	   public static void main(String[] args) {
	        try
	        {
	    		String Region_Code[] = { "UK", "England", "Wales", "Scotland" };
		        String Weather_Param[] = { "Tmax", "Tmin", "Tmean", "Sunshine", "Rainfall" };
				
		        
		        try {
		        	writer = new PrintWriter("E:/Download/Output.csv", "UTF-8");		
				} catch (Exception e) {
					// TODO: handle exception
				}
		       
			

				//Region_Code.length
				
				
				try {
		    		File folder = new File("E:/Download");
		    		final File[] files = folder.listFiles();
		    		for (File f: files) f.delete();
		    		folder.delete();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				 new File("E:/Download").mkdirs();
			        String saveDir = "E:/Download";

	        	for (int i=0; i<Region_Code.length; i++ ) {
				    for (int j=0;j<Weather_Param.length;j++)
					{
			    		DownloadFile(Weather_Param[j] , Region_Code[i],saveDir);
					}
				}
	        }
	        catch(Exception ex)
	        {
	            System.out.println(ex);
	        }
	   }

	    public static void DownloadFile(String Weather_Param,String Region_Code, String saveDir) throws IOException {

	    	String fileURL = "https://www.metoffice.gov.uk/pub/data/weather/uk/climate/datasets/" + Weather_Param + "/date/" + Region_Code + ".txt";

	        URL url = new URL(fileURL);
	        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	        int responseCode = httpConn.getResponseCode();

	        // always check HTTP response code first
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            String fileName = "";
	            String disposition = httpConn.getHeaderField("Content-Disposition");
	            String contentType = httpConn.getContentType();
	            int contentLength = httpConn.getContentLength();

	            if (disposition != null) {
	                // extracts file name from header field
	                int index = disposition.indexOf("filename=");
	                if (index > 0) {
	                    fileName = disposition.substring(index + 10,
	                            disposition.length() - 1);
	                }
	            } else {
	                // extracts file name from URL
	                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
	                        fileURL.length());
	            }

	        	fileName = Region_Code + "_" +Weather_Param + ".txt";

	            // opens input stream from the HTTP connection
	            InputStream inputStream = httpConn.getInputStream();
	            String saveFilePath = saveDir + File.separator + fileName;

	            // opens an output stream to save into file
	            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

	            int bytesRead = -1;
	            byte[] buffer = new byte[BUFFER_SIZE];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }

	            outputStream.close();
	            inputStream.close();

				GenerateCSV(Weather_Param, Region_Code);

	        } else {
	            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
	        }
	        httpConn.disconnect();
	    }



	    public static void GenerateCSV(String Weather_Param,String Region_Code)
	    {
	    	try
			{

			  String FileName = Region_Code + "_" + Weather_Param;
		
			  File file = new File("E:/Download/"+FileName+".txt");
		      StringBuffer str = new StringBuffer();
		      BufferedReader br = null;

			  	
		            br = new BufferedReader(new FileReader(file));
		            String text = null;

					for(int j=0;j<7;j++)
					{
						br.readLine();
					}

	                String months = br.readLine();

	                months = months.replaceAll("\\s+", ",");

					String Months[] = months.split(",");	

					String PushData = "";

		            while ((text = br.readLine()) != null)
		            {
	                    text =  text.replaceAll("\\s+", ",");
	                    
			            String splitarray[] = text.split(",");

						for(int xi=1;xi<13;xi++)
						{
							PushData = Region_Code + ", " + Weather_Param + ", "+ splitarray[0] + ", " + Months[xi] + ", " + splitarray[xi];
							writer.println(PushData);
						}
		            }
		    }
		    catch(Exception ex)
	        {
	            System.out.println(ex);
	        }
	       
		        















			  
	    }


	}

package userAPI.utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;



public class DataProviders {

	//DataProvider-1
	@DataProvider(name="PostUser")
	public static String [][] PostData() throws IOException
	{
		String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
		
		ExcelUtility xlutil=new ExcelUtility(path);
		
		int totalrows=xlutil.getRowCount("PostData");	
		int totalcols=xlutil.getCellCount("PostData",1); 
				
		String apidata[][]=new String[totalrows][totalcols];
		
		for(int i=1;i<=totalrows;i++)  
		{		
			for(int j=0;j<totalcols;j++) 
			{
				apidata[i-1][j]= xlutil.getCellData("PostData",i, j); 
			}
		}
	return apidata;
	
	}
	
	//DataProvider 2
	@DataProvider(name="GetAllUsers")
	public static String [][] getAllUserData() throws IOException
	{
		String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
		
		ExcelUtility xlutil=new ExcelUtility(path);
		
		int totalrows=xlutil.getRowCount("GetAllUsersData");	
		int totalcols=xlutil.getCellCount("GetAllUsersData",1); 
				
		String apidata[][]=new String[totalrows][totalcols];
		
		for(int i=1;i<=totalrows;i++)  
		{		
			for(int j=0;j<totalcols;j++)
			{
				apidata[i-1][j]= xlutil.getCellData("GetAllUsersData",i, j); 
			}
		}
	return apidata;
				
	}
	
	//DataProvider 3
		@DataProvider(name="GetUserById")
		public static String [][] GetUserByIdData() throws IOException
		{
			String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
			
			ExcelUtility xlutil=new ExcelUtility(path);
			
			int totalrows=xlutil.getRowCount("GetUserByID");	
			int totalcols=xlutil.getCellCount("GetUserByID",1); 
					
			String apidata[][]=new String[totalrows][totalcols];
			
			for(int i=1;i<=totalrows;i++)  
			{		
				for(int j=0;j<totalcols;j++) 
				{
					apidata[i-1][j]= xlutil.getCellData("GetUserByID",i, j); 
				}
			}
		return apidata;
		
		}
	
	//DataProvider 4
		@DataProvider(name="GetUserByFirstName")
		public static String [][] GetUserByFirstName() throws IOException
		{
			String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
			
			ExcelUtility xlutil=new ExcelUtility(path);
			
			int totalrows=xlutil.getRowCount("GetUserByFirstName");	
			int totalcols=xlutil.getCellCount("GetUserByFirstName",1); 
					
			String apidata[][]=new String[totalrows][totalcols];
			
			for(int i=1;i<=totalrows;i++)  
			{		
				for(int j=0;j<totalcols;j++) 
				{
					apidata[i-1][j]= xlutil.getCellData("GetUserByFirstName",i, j); 
				}
			}
		return apidata;
		
		}
		
		//DataProvider 5
		@DataProvider(name="DeleteUserById")
		public static String [][] DeleteUserById() throws IOException
		{
			String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
			
			ExcelUtility xlutil=new ExcelUtility(path);
			
			int totalrows=xlutil.getRowCount("DeleteUserById");	
			int totalcols=xlutil.getCellCount("DeleteUserById",1); 
					
			String apidata[][]=new String[totalrows][totalcols];
			
			for(int i=1;i<=totalrows;i++)  
			{		
				for(int j=0;j<totalcols;j++) 
				{
					apidata[i-1][j]= xlutil.getCellData("DeleteUserById",i, j); 
				}
			}
		return apidata;
		
		}
		
		//DataProvider 6
		@DataProvider(name="DeleteUserByFirstName")
		public static String [][] DeleteUserByFirstName() throws IOException
		{
			String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
			
			ExcelUtility xlutil=new ExcelUtility(path);
			
			int totalrows=xlutil.getRowCount("DeleteByFirstName");	
			int totalcols=xlutil.getCellCount("DeleteByFirstName",1); 
					
			String apidata[][]=new String[totalrows][totalcols];
			
			for(int i=1;i<=totalrows;i++)  
			{		
				for(int j=0;j<totalcols;j++) 
				{
					apidata[i-1][j]= xlutil.getCellData("DeleteByFirstName",i, j); 
				}
			}
		return apidata;
		
		}	
		
		//DataProvider- 7
		@DataProvider(name="UpdateUserById")
		public static String [][] UpdateUserByIdData() throws IOException
		{
			String path=System.getProperty("user.dir")+"//src//test//resources//Testdata//POST_RESTAssured_testdata.xlsx";
			
			ExcelUtility xlutil=new ExcelUtility(path);
			
			int totalrows=xlutil.getRowCount("UpdateUserById");	
			int totalcols=xlutil.getCellCount("UpdateUserById",1); 
					
			String apidata[][]=new String[totalrows][totalcols];
			
			for(int i=1;i<=totalrows;i++)  
			{		
				for(int j=0;j<totalcols;j++) 
				{
					apidata[i-1][j]= xlutil.getCellData("UpdateUserById",i, j); 
				}
			}
		return apidata;
		
		}
		}



import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;

public class ReaderRFID  implements Runnable
{	
	public static AlienClass1Reader reader = new AlienClass1Reader();
	public static String[] maniotTags = new String[3];
	
	// Construction method
	public ReaderRFID() throws AlienReaderException{	
		// Define the ID of the tag that will be used in ManIoT platform
		maniotTags[0] = "E200 3412 DC03 0118 2801 3353";
		maniotTags[1] = "E200 3411 B802 0114 5234 2042";
		maniotTags[2] = "E200 3412 DC03 0118 2815 3659";
		
		// To connect to a networked reader instead, use the following:
  		reader.setConnection("150.164.10.42", 23);
  		reader.setUsername("alien");
  		reader.setPassword("password");
  		
  		reader.open();
	}
		
	// Ask the reader to read tags. Return the tagList.
	public static Tag[] getTags() throws AlienReaderException, SQLException{	
  		Tag tagList[] = reader.getTagList();
//  		printTagList(tagList);
		
		return tagList;
	}
	
	// Print the tagList in the console
	public static void printTagList(Tag tagList[]){		
		if (tagList == null){
			System.out.println("\nNo Tags Found");
		}
		
		else{
			System.out.println("\nTag(s) found:");
			for (int i=0; i<tagList.length; i++){
				Tag tag = tagList[i];
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date renewTime = new Date(tag.getRenewTime());
				System.out.printf("ID: " + tag.getTagID() + "\tTime: " + dateFormat.format(renewTime) + "\n");				
			}
		}
	}
	
	// Print a tag in the console
	public static void printTag(Tag tag){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date renewTime = new Date(tag.getRenewTime());
		
		System.out.printf("ID: " + tag.getTagID() + "\tTime: " + dateFormat.format(renewTime) + "\t");
	}
	
	// Storage a tag in the table 'data' in ManIoT's database
	public static void tagToDB(int idResource, Tag tag) throws SQLException{
//		Date renewTime = new Date(tag.getRenewTime());
		Storage.insertData(idResource, tag.getTagID());
	}
	
	// Search for the ManIoT's tags and storage them into the database
	public static int findManiotTags(Tag tagList[]) throws AlienReaderException, SQLException{
		int numberOfTags = 0;
		boolean isManiotTag = false;
		
		if(tagList == null){
			System.out.println("\nNo Tags Found");
			return 0;
		}
		
		System.out.println("\nTag(s) found:");
		
		for (int i=0; i<tagList.length; i++){
			for(int j=0; j<maniotTags.length; j++){
				isManiotTag = false;
				if(maniotTags[j].equalsIgnoreCase(tagList[i].getTagID())){
					numberOfTags++;
					printTag(tagList[i]);
					tagToDB(5, tagList[i]);
					isManiotTag = true;
					break;
				}
			}
			
			if(!isManiotTag){
				printTag(tagList[i]);
				System.out.println("Not added in table 'data'.");
			}
		}
		
		return numberOfTags;
	}
	
	// Identify if a tag was or was not readed in a certain time interval
	public static boolean isTagPresent(String idTag) throws SQLException{
		int timeInterval = 120; // seconds
		ResultSet rs = Storage.executeQuery("SELECT * FROM data WHERE time >= DATE_SUB(now(), INTERVAL " + timeInterval + " SECOND)");
		String tag;
		
		while(rs.next()){
			tag = rs.getString("value");
			System.out.println(tag);
			
			if(tag == idTag)
				return true;
		}
		
		return false;
	}
	 
	public void run(){
		int readTime = 0;
		try {
			do {			
				if(findManiotTags(getTags()) == 0)
					readTime = 500;
				else
					readTime = 1000;
				Thread.sleep(readTime);
			}while(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static final void main(String args[]) throws Exception{		
		ReaderRFID rfid = new ReaderRFID();
		Thread t1 = new Thread(rfid);
		t1.start();
	}
}

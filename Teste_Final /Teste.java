import java.net.*;
import java.io.*;
import java.util.*;


public class Teste { 

        Boolean TemTag=false; //Informa se existe tags no Banco

        ManIoTHueDriver m; //Driver Lampadas Phillips Hue
        Storage s;  //Driver Banco de Dados
        ControllerSwitchWemo cw; //Driver Tomada Belkin Wemo

        int[] anteriores = new int[]{100,100,100};


	public class Leitura_Tags extends Thread implements Runnable{	
	  public static AlienClass1Reader reader = new AlienClass1Reader();
	  public static String[] maniotTags = new String[3];
	
	  // Construction method
	  public Leitura_Tags() throws AlienReaderException{	
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
//  	    printTagList(tagList);
	    return tagList;
	  }
	
	  // Print the tagList in the console
	  public static void printTagList(Tag tagList[]){		
	    if(tagList == null){
	      System.out.println("\nNo Tags Found");
	    }
	    else{
	      System.out.println("\nTag(s) found:");
	      for(int i=0; i<tagList.length; i++){
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
//	    Date renewTime = new Date(tag.getRenewTime());
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
		
	    for(int i=0; i<tagList.length; i++){
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
 	}


        public class Atualizar extends Thread implements Runnable{
            volatile boolean flag = true;

            public void run() {
                while(flag){
                    //Insere Consumo
                    try{
                        Integer consumo=cw.ReadConsumptionStatus();
                        String c=consumo.toString();
                        s.insertData(4,c);
                    }
                    catch(Exception e){
                    }

                    //Atualiza o vetor anteriores com os valores dos iris
		    UpdateIris();
                    //Verifica se Existe tags recentes no banco
		    try{
                        if(s.selectData(5)==1){
                          TemTag=true;
                        }
                        else{
                          TemTag=false;
                        }
                    }
                    catch(Exception e){
                    }
              
                    if(!TemTag){
                        System.out.println("Nao tem tags, desligando as luzes !");
                        for(int i=1; i<=3; i++){
                            m.changeOnOff(i,false);
                        }
                    }
                    else{
                        for(int i=1; i<=3; i++) {
                            m.changeOnOff(i,true);
                            try {
                                m.changeBrightness(i,Convert_Brightness(anteriores[i-1]));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try{
                        Thread.sleep(100);
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA\n\n\n\n");
                    } catch(Exception e){

                    }  
                }
            }
        }

        public void UpdateIris(){  //Atualizar os valores da luminosidade com os dados do iris
          for(int i=1; i<=3; i++){
            int aux=-1;
            try{
              aux=s.selectData(i);
            }
            catch(Exception e){
            }
            if(aux!=-1){
              anteriores[i-1]=aux;
            }
          }
          for(int i=1; i<=3; i++){
            System.out.println("L"+i+": " + anteriores[i-1]);
          }
        }

	public Teste() {
            m=new  ManIoTHueDriver();
            s=new Storage();
            cw=new ControllerSwitchWemo();

            Leitura_Tags rfid = new Leitura_Tags();
	    Thread t1 = new Thread(rfid);
	    t1.start();
            
            Atualizar a=new Atualizar();
            a.start();
    	}

        public int Convert_Brightness(int lux){
            if(lux<200) {
                return 250;
            }
            if(lux>200 && lux<300) {
                return 200;
            }
            if(lux>300 && lux<400) {
                return 150;
            }
            if(lux>400 && lux<500) {
                return 125;
            }
            if(lux>500 && lux<600) {
                return 100;
            }
            if(lux>600 && lux<700) {
                return 75;
            }
            if(lux>700 && lux<800) {
                return 50;
            }
            if(lux>800){
                return 25;
            }
            return 0;
        }
	
	public static void main(String[] args) throws Exception {
            Teste e = new Teste();
	}
}

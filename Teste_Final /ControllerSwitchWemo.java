import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

/**
 *
 * A complete Java class that shows how to open a URL, then read data (text) from that URL,
 * HttpURLConnection class (in combination with an InputStreamReader and BufferedReader).
 *
 * @author alvin alexander, devdaily.com.
 *
 */
public class ControllerSwitchWemo{

    private String HOST = "150.164.10.47:49153";
    private String ip = "150.164.10.74";
    private HttpURLConnection connection;

    public static void main(String[] args) throws Exception{
        new ControllerSwitchWemo();
    }

    public ControllerSwitchWemo(){
        try{
            // if your url can contain weird characters you will want to
            // encode it here, something like this:
            // myUrl = URLEncoder.encode(myUrl, "UTF-8");
            //int state = SwitchOn();
            //System.out.println(state);
            //getKWH(ip);
        }
        catch (Exception e)
        {
            // deal with the exception in your "controller"
        }
    }

    public void Connect() throws Exception{
        URL url;
        BufferedReader reader = null;
        WemoStats retVal = null;

        try{
            // create the HttpURLConnection
            url = new URL("http://"+ip+":49153/upnp/control/basicevent1");
            connection = (HttpURLConnection) url.openConnection();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean ReadStatus(String ip) throws Exception{
        URL url = null;
        BufferedReader reader = null;

        try{
            // create the HttpURLConnection
            url = new URL("http://"+ip+":49153/upnp/control/basicevent1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", HOST+":49153");
            connection.setRequestProperty("Content-type","text/xml; charset=\"utf-8\"");
            connection.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#GetBinaryState\"");
            String data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>0</BinaryState></u:GetBinaryState></s:Body></s:Envelope>";
            connection.setRequestProperty("Content-Length",Integer.toString(data.length()));

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes (data);
            wr.flush ();
            wr.close ();

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;
            while ((line = reader.readLine()) != null){
                if(line.contains("<BinaryState>")) {
                    connection.disconnect();
                    reader.close();
                    return line.contains("<BinaryState>1</BinaryState>");
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return false;
    }

    public int SwitchOn() throws Exception{
        URL url;
        BufferedReader reader = null;
        WemoStats retVal = null;
        boolean state=true;

        try{
            Connect();
            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", HOST+":49153");
            connection.setRequestProperty("Content-type","text/xml; charset=\"utf-8\"");
            connection.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#SetBinaryState\"");
            String stateStr = (state?"1":"0");
            String data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:SetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>"+stateStr+"</BinaryState></u:SetBinaryState></s:Body></s:Envelope>";
            connection.setRequestProperty("Content-Length",Integer.toString(data.length()));

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes (data);
            wr.flush ();
            wr.close ();

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        return 0;
    }


    public int SwitchOff() throws Exception{
        URL url;
        BufferedReader reader = null;
        WemoStats retVal = null;
        boolean state=false;

        try{
            Connect();
            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", HOST+":49153");
            connection.setRequestProperty("Content-type","text/xml; charset=\"utf-8\"");
            connection.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#SetBinaryState\"");
            String stateStr = (state?"1":"0");
            String data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:SetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>"+stateStr+"</BinaryState></u:SetBinaryState></s:Body></s:Envelope>";
            connection.setRequestProperty("Content-Length",Integer.toString(data.length()));

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes (data);
            wr.flush ();
            wr.close ();

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        return 0;
    }


    public void ClearReads(){}


    public int ReadTimeStatus() throws Exception{
        URL url;
        BufferedReader reader = null;
        WemoStats retVal = null;
        boolean state=true;

        try{
            Connect();
            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", HOST+":49153");
            connection.setRequestProperty("Content-type","text/xml; charset=\"utf-8\"");
            connection.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#SetBinaryState\"");
            String stateStr = (state?"1":"0");
            String data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:SetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>"+stateStr+"</BinaryState></u:SetBinaryState></s:Body></s:Envelope>";
            connection.setRequestProperty("Content-Length",Integer.toString(data.length()));

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes (data);
            wr.flush ();
            wr.close ();

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null){
                if(line.contains("<BinaryState>")) {
                    int end = line.indexOf("</BinaryState>");
                    int start = line.indexOf("<BinaryState>")+13;
                    StringTokenizer st = new StringTokenizer(line.substring(start,end),"|");
                    boolean onState = false;
                    int onSince = 0;
                    int consumptionState = 0;
                    long[] fieldVals = new long[10];
                    for(int i=1;i<=10;i++) {
                        String token = st.nextToken();
                        if(i == 1) {
                            //On/StandBy
                            onState = (token.equals("0")?false:true);
                        }
                        else if(i == 3) {
                            //Time Since On
                            onSince=Integer.parseInt(token);
                        }
                        else if(i == 8) {
                            //Consumption in Watts
                            consumptionState=Integer.parseInt(token);
                        }
                        fieldVals[i-1] = Long.parseLong(token);
                    }

                    retVal = new WemoStats(onState,onSince,consumptionState);
                    retVal.setWemoStatsValues(fieldVals);
                }
            }
            connection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        return retVal.getOnForSeconds();
    }


    public int ReadConsumptionStatus() throws Exception{
        URL url;
        BufferedReader reader = null;
        WemoStats retVal = null;
        boolean state=true;

        try{
            Connect();
            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", HOST+":49153");
            connection.setRequestProperty("Content-type","text/xml; charset=\"utf-8\"");
            connection.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#SetBinaryState\"");
            String stateStr = (state?"1":"0");
            String data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:SetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>"+stateStr+"</BinaryState></u:SetBinaryState></s:Body></s:Envelope>";
            connection.setRequestProperty("Content-Length",Integer.toString(data.length()));

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (data);
            wr.flush ();
            wr.close ();

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null){
                if(line.contains("<BinaryState>")) {
                    int end = line.indexOf("</BinaryState>");
                    int start = line.indexOf("<BinaryState>")+13;
                    StringTokenizer st = new StringTokenizer(line.substring(start,end),"|");
                    boolean onState = false;
                    int onSince = 0;
                    int consumptionState = 0;
                    long[] fieldVals = new long[10];
                    for(int i=1;i<=10;i++) {
                        String token = st.nextToken();
                        if(i == 1) {
                            //On/StandBy
                            onState = (token.equals("0")?false:true);
                        }
                        else if(i == 3) {
                            //Time Since On
                            onSince=Integer.parseInt(token);
                        }
                        else if(i == 8) {
                            //Consumption in Watts
                            consumptionState=Integer.parseInt(token);
                        }
                        fieldVals[i-1] = Long.parseLong(token);
                    }

                    retVal = new WemoStats(onState,onSince,consumptionState);
                    retVal.setWemoStatsValues(fieldVals);
                }
            }
            connection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        return retVal.getConsumptionMilliWatts();
    }

    private int getSignalStrength(String ip) throws Exception{
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try{
            // create the HttpURLConnection
            url = new URL("http://"+ip+":49153/upnp/control/basicevent1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", HOST+":49153");
            connection.setRequestProperty("Content-type","text/xml; charset=\"utf-8\"");
            connection.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent1:1#GetSignalStrength\"");
            String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetSignalStrength xmlns:u=\"urn:Belkin:service:basicevent:1\"><GetSignalStrength>0</GetSignalStrength></u:GetSignalStrength></s:Body></s:Envelope>";
            connection.setRequestProperty("Content-Length",Integer.toString(data.length()));
            connection.setRequestProperty("Content-Length","0");

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (data);
            wr.flush ();
            wr.close ();

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            String returnVal = stringBuilder.toString();
            System.out.println(returnVal);
            connection.disconnect();
            return 0;
        }
        catch (Exception e){
            e.printStackTrace();

            throw e;
        }
        finally{
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }
}

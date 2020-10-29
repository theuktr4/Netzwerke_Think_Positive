import javax.swing.text.Document;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Think_Positive_Umwandler {
    public static final int PORT=8082;
    private String URL;
    public Think_Positive_Umwandler(String URL){
        this.URL = URL;
        try(ServerSocket server = new ServerSocket(PORT)){
            while(true) {
                try (Socket s = server.accept()) {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.ISO_8859_1));
                    URL urlObj = new URL(URL);
                    URLConnection connection = urlObj.openConnection();
                    String body = getWebsiteBody(connection);
                    int contentLength = body.getBytes("ISO_8859_1").length;
                    //Header
                    bw.write(getHeaderAsString(getwebsiteResponseheader(connection,contentLength)));
                    //bw.write("HTTP/1.1 200 OK\r\n");
                    //bw.write("\r\n");
                    bw.flush();
                    //Body
                    bw.write(body);
                    bw.close();

                } catch (IOException ex) {
                    System.out.println("Client disconnected");
                }
            }
        } catch (IOException ex){
            System.out.println("Server beendet");
        }
    }
    String getWebsiteBody(URLConnection conn){
        String res= "";
        try {
            InputStream input = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
            System.out.println("\n received:");
            //HTML einlesen
            for(String line = br.readLine();line != null; line = br.readLine()) {
                //HTML verändern
                res = res + modifyLine(line)+"\r\n";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("body: "+res);
        return res;
    }
    Map<String,List<String>> getwebsiteResponseheader(URLConnection conn, int contentLength){
        Map<String, List<String>> header = new HashMap<String, List<String>>(conn.getHeaderFields());
        List<String> temp = new ArrayList<String>();
        temp.add(String.valueOf(contentLength));
        header.replace("Content-Length",temp);
        return header;
    }
    String getHeaderAsString(Map<String,List<String>> rawHeader) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : rawHeader.entrySet()) {
            if(entry.getKey()==null){
                sb.insert(0,entry.getValue().get(0)+"\r\n");
            }
            else if(entry.getValue().size()>1){
                sb.append(entry.getKey());
                sb.append(":");
                for(String s:entry.getValue()){
                    sb.append(s+",");
                }
                sb.deleteCharAt(sb.length()-1);
            }
            else{
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().get(0)+"\r\n");
            }

        }
        sb.append("\r\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    String modifyLine(String line){ ;
        String[] keywords = {"KI","Maschinelles Lernen", "Java", "Computer", "MMIX", "RISC","CISC","Debugger","Informatik","Student", "Studentin","Studierende","Windows","Linux","Software","Informtiker","infomatikerInnen","informatikerin"};
        for(String s: keywords){
            line = line.replaceAll(s,s+"(Yeah)");
        }
        line = line.replaceAll("<img\s.*>","<img src =https://upload.wikimedia.org/wikipedia/commons/8/8d/Smiley_head_happy.svg>");
        return line;
    }

    public static void main(String[] args) {
        new Think_Positive_Umwandler(args[0]);
    }
}

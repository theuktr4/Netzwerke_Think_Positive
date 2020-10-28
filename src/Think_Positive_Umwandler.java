import javax.swing.text.Document;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Think_Positive_Umwandler {
    public static final int PORT=8082;
    public static final String HTML_DOC="<html> Hi wie gehts</html>";
    private String URL;
    public Think_Positive_Umwandler(String URL){
        this.URL = URL;
        try(ServerSocket server = new ServerSocket(PORT)){
            try(Socket s = server.accept()){
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

                //header
                bw.write("HTTP/1.1 200 OK\r\n");
                bw.write("\r\n");

                Map<String,List<String>>header =getwebsiteResponseheader(URL);
                for (Map.Entry<String, List<String>> entry : header.entrySet()) {
                    System.out.println(entry.getValue()+"+\n");
                }
                //body
                getWebsiteBody(URL);
                bw.close();

            } catch (IOException ex){
                System.out.println("Client disconnected");
            }
        } catch (IOException ex){
            System.out.println("Server beendet");
        }
    }
    String getWebsiteBody(String urlString){
        String res= "";
        try {
            URL urlObj = new URL(urlString);
            URLConnection conn = urlObj.openConnection();
            InputStream input = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            System.out.println("\n received:");

            for(String line = br.readLine();line != null; line = br.readLine()) {
                res += line;
                System.out.println(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    Map<String,List<String>> getwebsiteResponseheader(String urlString){
        try {
            URL urlObj = new URL(urlString);
            URLConnection conn = urlObj.openConnection();
            Map<String, List<String>> header = conn.getHeaderFields();
            return header;
        } catch (IOException e) {
            System.out.println("Fehler bei Headerabfrage");
            return null;
        }
    }

    String modifyHTML(String original){
        return null;
    }

    public static void main(String[] args) {
        new Think_Positive_Umwandler(args[0]);
    }
}

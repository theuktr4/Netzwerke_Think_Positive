import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

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
                //body
                bw.write(getWebsite(URL));
                bw.close();

            } catch (IOException ex){
                System.out.println("Client disconnected");
            }
        } catch (IOException ex){
            System.out.println("Server beendet");
        }
    }
    String getWebsite(String urlString){
        String res= "";
        try(InputStream input = new URL(urlString).openStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            System.out.println("\n received:");
            for(String line = br.readLine();!line.isEmpty(); line = br.readLine()) {
                res +=  line;
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    String modifyHTML(String original){
        return null;
    }

    public static void main(String[] args) {
        new Think_Positive_Umwandler(args[0]);
    }
}

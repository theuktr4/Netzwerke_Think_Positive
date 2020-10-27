import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Think_Positive_Umwandler {
    public static final int PORT=8082;
    public static final String HTML_DOC="<html> Hi wie gehts</html>";
    public Think_Positive_Umwandler(){
        try(ServerSocket server = new ServerSocket(PORT)){
            try(Socket s = server.accept()){
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

                //read HTTP Request
                for(String line = br.readLine();!line.isEmpty(); line = br.readLine()) {
                    System.out.println(line);
                }
                //header
                bw.write("HTTP/1.1 200 OK\r\n");
                bw.write("\r\n");
                bw.write(HTML_DOC);
                bw.close();

            } catch (IOException ex){
                System.out.println("Client disconnected");
            }
        } catch (IOException ex){
            System.out.println("Server beendet");
        }
    }
    String getWebsite(){
        return null;
    }

    String modifyHTML(String original){
        return null;
    }

    public static void main(String[] args) {
        new Think_Positive_Umwandler();
    }
}

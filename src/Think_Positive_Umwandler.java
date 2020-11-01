import javax.swing.text.Document;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Think_Positive_Umwandler {
    private static final int PORT = 8082;
    private Charset ENCODING = StandardCharsets.ISO_8859_1;
    private String URL;

    public Think_Positive_Umwandler(String protocol, String URL) {
        this.URL = URL;
        System.out.println("Server started");
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try (Socket s = server.accept()) {
                    System.out.println("Client connected");
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), ENCODING));
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), ENCODING));
                    //Einlesen des HTTP Requests
                    String request = br.readLine();
                    if(!request.contains("GET")){
                        bw.write("HTTP/1.1 405 Method Not Allowed\n\r");
                        bw.write("\n\r");
                        bw.write("<html><body><h>405 Method Not Allowed</h></body></html>");
                        bw.flush();
                    }
                    else {
                        //Verbindung zur übergebenen Webseite
                        URL urlObj = new URL(protocol + "://" + URL);
                        URLConnection targetHost = urlObj.openConnection();
                        //Body ermitteln
                        String body = getWebsiteBody(targetHost);
                        //Content Length berechnen
                        int contentLength = body.getBytes(ENCODING).length;
                        //Header von Zielwebseite an Client senden
                        bw.write(getHeaderAsString(getwebsiteResponseheader(targetHost, contentLength)));
                        bw.flush();
                        //Webseite an Client senden
                        bw.write(body);
                        bw.flush();
                    }

                } catch (IOException ex) {
                    System.err.println("Client disconnected");
                }
            }
        } catch (IOException ex) {
            System.err.println("Server beendet");
        }
    }
    /* Methode liefert als Ergebnis eine HTML Seite als String zurück
        param:  conn Referenz auf die URL Verbindung
        return: html der Webseite als String
     */
    String getWebsiteBody(URLConnection conn) {
        String res = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),ENCODING))){
            //System.out.println("\n received:");
            //HTML einlesen
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                //HTML verändern
                res = res + modifyLine(line) + "\r\n";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Verbindung zum TH abgebrochen");
        }
        //System.out.println("body: " + res);
        return res;
    }
    /* Methode liefert als Ergebnis eine HTML Seite als String zurück
        param:  conn: Referenz auf die URL Verbindung
                contentLength: Die Größe des Antwort payloads
        return: Webseiten Header als Map
     */
    Map<String, List<String>> getwebsiteResponseheader(URLConnection conn, int contentLength) {
        //Header des TH geben
        Map<String, List<String>> header = new HashMap<String, List<String>>(conn.getHeaderFields());
        List<String> temp = new ArrayList<String>();
        temp.add(String.valueOf(contentLength));
        //Content Length anpassen
        header.replace("Content-Length", temp);
        //chunked Encoding löschen (für Webseiten wie google.com)
        header.remove("Transfer-Encoding");
        return header;
    }
    /* Methode liefert den Webseiten Header als versendbare String
    param:  rawHeader Header als Map
    return: Webseiten Header als versendbare String
 */
    String getHeaderAsString(Map<String, List<String>> rawHeader) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : rawHeader.entrySet()) {
            //Header Kopf
            if (entry.getKey() == null) {
                sb.insert(0, entry.getValue().get(0) + "\r\n");

                //mehr als einen Eintrag als Value
            } else if (entry.getValue().size() > 1) {
                sb.append(entry.getKey());
                sb.append(":");
                for (String s : entry.getValue()) {
                    sb.append(s + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().get(0) + "\r\n");
            }

        }
        sb.append("\r\n");
        //System.out.println(sb.toString());
        return sb.toString();
    }
    /* Verändert eine HTML Zeile, wie in den Anforderungen festgelegt
    param:  line    original Zeile
    return: veränderte Zeile als String
 */
    String modifyLine(String line) {
        //Wörter, an denen Yeah drangehängt werden soll
        String[] keywords = {"KI", "Maschinelles Lernen", "Java", "Computer", "MMIX", "RISC", "CISC", "Debugger", "Informatik", "Student", "Studentin", "Studierende", "Windows", "Linux", "Software", "Informtiker", "InfomatikerInnen", "Informatikerin"};
        //Yeah anbringen...
        for (String s : keywords) {
            //(?i) um Groaß-Kleinschreibung zu ignorieren
            line = line.replaceAll("(?i)"+s+"\\s", s + "(Yeah) ");
        }
        //Bilder ersetzen, ohne class, id o.ä zu verändern
        Pattern imageClass = Pattern.compile("<img\\s.*>");
        Matcher matcher = imageClass.matcher(line);
        if(matcher.find()){
            line = line.replaceAll("src=\"(.*?)\"", "src =https://upload.wikimedia.org/wikipedia/commons/8/8d/Smiley_head_happy.svg");
        }
        return line;
    }

    public static void main(String[] args) {
        if(args.length<2){
            System.err.println("Zu wenige Argumente übergeben: expected String<protocol>(http,https) String<URL>");
            System.exit(1);
        }
        new Think_Positive_Umwandler(args[0], args[1]);
    }
}

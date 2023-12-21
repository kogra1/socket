import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class socket {
    public static void main(String[] args) throws Exception {
         
        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:60000/kyle-server");

        Scanner scanner = new Scanner(System.in);
        System.out.print("What message do you want to send the server? ");
        String userMessage = scanner.nextLine();

        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(userMessage))
                .build();

       
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        scanner.close();
    }
}
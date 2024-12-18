import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * LocalDNS_Server_김재인_20223061
 */
public class LocalDNS_Server_김재인_20223061 {
    
    private static final Map<String, String> dnsTable = new HashMap<>();
    private static final Map<String, String> reverseDnsTable = new HashMap<>();

    static {
        // Sample
        dnsTable.put("example.com", "123.456.78.90");
        dnsTable.put("google.com", "142.250.72.78");
        dnsTable.put("daum.net", "203.133.167.81");
        dnsTable.put("naver.com", "223.130.200.104");
        
        for (Map.Entry<String, String> entry : dnsTable.entrySet()) {
            reverseDnsTable.put(entry.getValue(), entry.getKey());
        }
    }

    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        Socket socket = null;

        Scanner scanner = new Scanner(System.in);

        try {
            listener = new ServerSocket(9999); // 서버 소켓 생성
            System.out.println("연결을 기다리고 있습니다.");
            socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
            System.out.println("연결되었습니다."); 

            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 소켓 입력 스트림
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 소켓 출력 스트림

            while (true) {
                String inputMessage = in.readLine(); // 클라이언트로부터 한 행 읽기

                if (inputMessage.equalsIgnoreCase("bye")) {
                    System.out.println("클라이언트에서 bye로 연결을 종료하였습니다.");
                    break;
                }
                 System.out.println("클라이언트: " + inputMessage); // 받은 메시지를 화면에 출력			
                
                String responseMessage = processRequest(inputMessage);
                out.write(responseMessage + "\n");
                out.flush(); // 남아있는 데이터 모두 출력
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close(); // 스캐너 닫기
                socket.close(); // 통신용 소켓 닫기
                listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
            }
        }
    }

    private static String processRequest(String request) {
        if (request.startsWith("N ")) {
            String domainName = request.substring(2).trim();
            return dnsTable.getOrDefault(domainName, "Not found");
        } else if (request.startsWith("R ")) {
            String ipAddress = request.substring(2).trim();
            return reverseDnsTable.getOrDefault(ipAddress, "Not found");
        } else {
            // DNS 요청이 아닌 메시지
            return request;
        }
    }
}

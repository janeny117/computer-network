import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * LocalDNS_Client_김재인_20223061
 */

public class LocalDNS_Client_김재인_20223061 {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;

        Scanner scanner = new Scanner(System.in); // 키보드에서 읽을 스캐너 객체 생성

        try {
            socket = new Socket("localhost", 9999); // 클라이언트 소켓 생성. 서버와 바로 연결
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 소켓 입력 스트림
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 소켓 출력 스트림

            while (true) {
                System.out.println("보내기>> "); // 프롬프트
                String outputMessage = scanner.nextLine(); // 키보드에서 한 행 읽기
                if (outputMessage.equalsIgnoreCase("bye")) {
                    out.write(outputMessage+"\n");
                    out.flush();
                    break; // 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료

                }
                out.write(outputMessage +"\n");
                out.flush();
                String inputMessage = in.readLine(); // 서버로부터 한 행 수신
                System.out.println("서버: "+ inputMessage); //서버로부터 받은 메시지 화면에 출력
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
			
        }
        finally{
            try {
                scanner.close();
                if(socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("서버와 채팅 중 오류가 발생했습니다.");

            }
        }
    }
}
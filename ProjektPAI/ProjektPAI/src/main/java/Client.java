import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private static int PORT = 8080;
    private static String HOST = "192.168.1.10";
    private static ClientThread clientThread;

    public static void main(String[] args)
    {
        Socket clientSocket = null;
        DataInputStream dataInput = null;
        DataOutputStream dataOutput = null;
        System.out.println("Nazwa hosta: " + HOST + " port: " + PORT);
        try
        {
            clientSocket = new Socket("localhost", PORT);
            System.out.println("Połączono z serwerem.");
            dataInput = new DataInputStream(clientSocket.getInputStream());
            dataOutput = new DataOutputStream(clientSocket.getOutputStream());
            clientThread = new ClientThread(clientSocket, dataInput, dataOutput);
            clientThread.start();
        }
        catch(ConnectException e)
        {
            System.out.println("Serwer jest niedostępny!");
        }
        catch(SocketException e)
        {
            System.out.println("Połączenie zostało zerwane!");
        }
        catch(Exception e)
        {
            System.out.println("Wystąpił błąd: " + e);
        }
        finally
        {
            if(clientThread != null)
                try
                {
                    clientThread.interrupt();
                } catch(Exception e) {}
        }
    }
}

class ClientThread extends Thread
{
    private Socket clientSocket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private boolean shouldRun = true;
    private boolean logged = false;

    public ClientThread(Socket socket, DataInputStream input, DataOutputStream output)
    {
        this.clientSocket = socket;
        this.dataInput = input;
        this.dataOutput = output;
    }

    public void run()
    {
        int type = 0;
        Scanner scan = new Scanner(System.in);
        InputData inputData = new InputData(dataInput, dataOutput);
        while(shouldRun)
        {
            try
            {
                if(!logged)
                {
                    System.out.println("1. Zaloguj się\n2. Zarejestruj się\n3. Zreseruj hasło");
                    System.out.print("Wpisz numer, tego co chcesz zrobić: ");
                    for (;;){
                        if(!scan.hasNextInt()){
                            System.out.println("Błędny numer!\nWpisz numer ponownie: ");
                            scan.next();
                            continue;
                        }
                        type = scan.nextInt();
                        break;
                    }
                    if(type == 1 || type == 2 || type == 3)
                    {
                        String message = inputData.checkType(type);
                        System.out.println(message);
                        if(message.equals("Udało się zalogować."))
                        {
                            logged = true;
                        }
                    }
                    else
                    {
                        System.out.println("Błędny numer!");
                    }
                }
                else if(logged)
                {
                    String message = inputData.checkType(4);
                    System.out.println(message);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

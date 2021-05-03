import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.*;
//Klient Transport jest wyposażony w funkcję wykrywania klastra, która umożliwia dynamiczne dodawanie nowych hostów i usuwanie starych.
// Gdy podsłuchiwanie jest włączone, klient transportu połączy się z węzłami na swojej wewnętrznej liście węzłów, która jest tworzona przez wywołania
// addTransportAddress. Następnie klient wywoła wewnętrzny interfejs API stanu klastra w tych węzłach, aby wykryć dostępne węzły danych.
// Wewnętrzna lista węzłów klienta zostanie zastąpiona tylko tymi węzłami danych. Ta lista jest domyślnie odświeżana co pięć sekund. Zauważ, że
// adresy IP, z którymi łączy się sniffer, są adresami zadeklarowanymi jako adresy publikacji w konfiguracji Elasticsearch tego węzła.
//
//Należy pamiętać, że lista może nie zawierać oryginalnego węzła, z którym był połączony, jeśli ten węzeł nie jest węzłem danych.
// Jeśli, na przykład, początkowo łączysz się z węzłem głównym, po podsłuchaniu żadne dalsze żądania nie będą kierowane do tego węzła głównego,
// a zamiast tego do dowolnych węzłów danych. Powodem, dla którego klient transportu wyklucza węzły niebędące węzłami danych, jest unikanie
// wysyłania ruchu związanego z wyszukiwaniem do węzłów tylko głównych.
public class Server
{
    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        System.out.println("Uruchamianie serwera");
        try
        {
            System.out.println("Lączenie z serwerem ElasticSearch");
            // Stworzenie ustawień połączenia z ElasticSearchem włączonym na localhoscie
            Settings settings = Settings.builder()
                    .put("cluster.name", "elasticsearch").build();
            //TransportClient łączy się zdalnie z klastrem Elasticsearch przy użyciu modułu transportu.
            // Nie przyłącza się do klastra, ale po prostu pobiera jeden lub więcej początkowych adresów transportowych i komunikuje się z nimi w
            // sposób okrężny przy każdej akcji.

            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

            serverSocket = new ServerSocket(8080);
            System.out.println("Serwer dziala");
            while(true)
            {
                DataInputStream dataInput = null;
                DataOutputStream dataOutput = null;
                clientSocket = serverSocket.accept();
                dataInput = new DataInputStream(clientSocket.getInputStream());
                dataOutput = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Klient: " + clientSocket);
                Thread serverThread = new ServerThread(client, dataInput, dataOutput);
                serverThread.start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Blad " + e);
        }
    }
}

class ServerThread extends Thread
{
    private TransportClient client = null;
    private DataInputStream dataInput = null;
    private DataOutputStream dataOutput = null;
    private boolean shouldRun = true;

    public ServerThread(TransportClient client, DataInputStream input, DataOutputStream output)
    {
        this.client = client;
        this.dataInput = input;
        this.dataOutput = output;
    }

    public void run()
    {
        CheckData checkData = new CheckData(client ,dataInput, dataOutput);
        while(shouldRun)
        {
            try
            {
                int type = dataInput.readInt();
                String message = checkData.check(type);
                dataOutput.writeUTF(message);
                dataOutput.flush();
            }
            catch (IOException | JAXBException e)
            {
                e.printStackTrace();
            }
        }
    }
}

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class DodajDoElasticSeach
{
    private static String bookPath = "C:\\Users\\48536\\Desktop\\Ksiazki\\Książki\\The_Adventures_of_Sherlock_Holmes.txt";

    public static void main(String[] args)
    {
        try
        {
            // Stworzenie ustawień połączenia z ElasticSearchem włączonym na localhoscie
            Settings settings = Settings.builder()
                    .put("cluster.name", "elasticsearch").build();
            // Stworzenie strumienia/połączenia z ElasticSearchem
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

            // Pobranie książki i odpowiednie dostosowanie jej nazwy
            File file = new File(bookPath);
            String fileName = file.getName();
            fileName = fileName.replace("_", " ");
            fileName = fileName.replace(".txt", "");

            // Pobranie treści ksiązki i stworzenie Objektu JSON, umożliwiającemu wysłanie danych na serwer ElasticSearcha
            Path filePath = Path.of(bookPath);
            String readBook = Files.readString(filePath);
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("tytul", fileName);
            jsonObject.put("tresc", readBook);

            // Wysłanie danych na serwer. Serwer teraz indeksuje sobie całą treść odpowiednią rozdzielając i dzieląc treść na mniejsze części
            IndexResponse indexResponse = client.prepareIndex("database", "ksiazka", Integer.toString(14))
                    .setSource(jsonObject, XContentType.JSON)
                    .get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
    //By w pełni poznać możliwości opisywanej wyszukiwarki, najlepiej jest najpierw poznać ją i jej strukturę.
// Nawet jeśli to tylko podstawowe elementy, to pozwalają lepiej zrozumieć jej elementy i w rezultacie otrzymywać lepsze, dokładniejsze rezultaty swoich działań.

        //Dla przykładu – Node to pojedynczy serwer. To właśnie tutaj dochodzi do przetwarzania i przeszukiwania danych.
// Każdy z takich serwerów ma unikalną nazwę, a także port, na którym działa – standardowo jest to 9200. Zbiór, złożony z jednego lub kilku node’ów, nazywamy Clusterem.

        //Zagłębiając się dalej, znajdziemy pojęcie Index – to z kolei kolekcja dokumentów o bardzo podobnej charakterystyce.
// To na podstawie nazwy indeksu odwołujemy się do konkretnych kolekcji dokumentów i w zależności od naszych potrzeb dodajemy, usuwamy lub po prostu wyszukujemy
// kluczowe dla nas dokumenty. W przypadku standardowych, relacyjnych baz danych indeks moglibyśmy traktować jako pełnoprawną bazę danych.

        //inny sposób grupowania danych nazywamy Type. Często sposób ten nazywa się także tabelą, z tym że jej struktura nie jest ściśle zdefiniowana, co wyróżnia
// ją chociażby od tabel znanych z relacyjnej bazy danych. Tutaj struktura zależna jest przede wszystkim od przechowywanych w grupie dokumentów. Z kolei Document,
// czyli dokumenty, to rekordy zapisywane w formacie JSON.

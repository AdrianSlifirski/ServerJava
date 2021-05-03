import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchPhrase
{
    //TransportClient łączy się zdalnie z klastrem Elasticsearch przy użyciu modułu transportu.
    // Nie przyłącza się do klastra, ale po prostu pobiera jeden lub więcej początkowych adresów transportowych i komunikuje się z nimi w
    // sposób okrężny przy każdej akcji.
    private TransportClient client = null;

    public SearchPhrase(TransportClient client)
    {
        this.client = client;
    }

    public List<String> search(String phrase)
    {
        //Klasa SearchResponse jest wysyłana przez serwer jako odpowiedź na obiekt SearchRequest.
        // Ta odpowiedź zawiera zero lub więcej obiektów SearchResultEntry i zero lub więcej obiektów SearchResultReference.
        //Interfejs API wyszukiwania umożliwia wykonanie zapytania wyszukiwania i odzyskanie wyników wyszukiwania pasujących do zapytania.
        // Może być wykonywany na jednym lub kilku indeksach i jednym lub kilku typach. Zapytanie można wykonać za pomocą zapytania Java API.
        // Treść żądania wyszukiwania jest tworzona przy użyciu SearchSourceBuilder. Oto przykład:
        SearchResponse response = client.prepareSearch("database")
                .setTypes("ksiazka")
                .setQuery(QueryBuilders.matchPhraseQuery("tresc", phrase))
                .get();
        //dokladna podana fraza
        //tablica hitow ktora zawiera informacje ile punktow uzyskala dana pozycja oraz zwraca podane atrybuty
        SearchHit[] hits = response.getHits().getHits();
        List<String> books = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            books.add(sourceAsMap.get("tytul").toString());
        }
        return books;
    }
}

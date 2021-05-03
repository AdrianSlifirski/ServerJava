import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

//JAXB, czyli Java Architecture for XML Binding, jest specyfikacją wchodzącą w skład Javy SE pozwalającą na manipulację dokumentami XML.

//JAXB przypomina nieco frameworki do mapowania obiektowo relacyjnego takie jak Hibernate,
// ponieważ przy pomocy zestawu predefiniowanych adnotacji jesteśmy w stanie kontrolować wynik marshallingu do dokumentu XML
// i unmarshallingu do obiektu POJO. W rzeczywistości spotkamy się z takimi aplikacjami, gdzie klasa definiująca encję
// JPA będzie jednocześnie oznaczona dodatkowymi adnotacjami JAXB.


public class XMLDataBase
{
    private String path = "C:\\Nowy folder\\ProjektPAI\\ProjektPAI\\src\\main\\java\\DataBase.xml";

    public XMLDataBase() {}

    public Users readBase() throws JAXBException
    {

        JAXBContext context = JAXBContext.newInstance(Users.class);

        //Unmarshalling to proces polegający na konwersji obiektu z formatu innego niż Java na obiekt Javy.
        // Najczęściej spotkamy się z unmarshallingiem z postaci XML lub JSON na Javowe POJO/JavaBeans.
        // Wykorzystywane są do tego celu specyfikacja JAXB (dla XML) lub biblioteki takie jak Jackson (dla JSON).

        Unmarshaller unmarshaller = context.createUnmarshaller();

        Users users = (Users) unmarshaller.unmarshal(new File(path));
        return users;
    }

    public void writeBase(Users usersToWrite) throws JAXBException
    {
        //Marshalling jest procesem polegającym na konwersji obiektu Javy na inny typ danych wymagany przez zewnętrzną aplikację.
        //Najczęściej spotkamy się z marshallingiem do formatów XML (z wykorzystaniem specyfikacji JAXB) lub JSON (używając biblioteki typu Jackson).
        JAXBContext context = JAXBContext.newInstance(Users.class);

        Marshaller marshaller = context.createMarshaller();
        //Marshalling odgrywa szczególną rolę w przypadku tworzenia REST API, gdzie backend napisany w Javie musi udostępniać
        // dane w formie czytelnej dla aplikacji napisanej np. z wykorzystaniem języka JavaScript.

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(usersToWrite, new File(path));
    }
}

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
//JAXB definiuje zestaw adnotacji, które pozwalają na konfigurację mapowania obiektów Javy.
// Dzięki takiemu podejściu jest to specyfikacja nieinwazyjna i mogąca być zastosowana do już istniejących, zwykłych klas POJO bez ingerencji w ich kod.
// Dwie najważniejsze adnotacje to:
//
//    @XmlRootElement - definiuje, że dana klasa może być mapowana na dokument XML
//
//    @XmlAccessorType - określa, czy dostęp do składowych klasy będzie odbywał się bezpośrednio, czy poprzez metody dostępowe
//
//Oprócz tego znajdziemy zestaw adnotacji pozwalających określić szczegóły mapowania np. @XmlTransient wyłącza dane pole z mapowania dokumentu
// (analogicznie jak transient przy serializacji), @XmlAttribute mapuje na atrybut XML.

@XmlAccessorType(XmlAccessType.FIELD)
//Każde niestatyczne, nietrwałe pole w klasie powiązanej z JAXB zostanie automatycznie powiązane z kodem XML, chyba że zostanie opatrzone adnotacją XmlTransient.
@XmlRootElement(name = "usersData")
public class Users {
    //Mapuje właściwość JavaBean na element XML pochodzący z nazwy właściwości.
    @XmlElement(name = "userData")
    private List<UserData> users;

    public Users() {}

    public void setUsers(List<UserData> users) {
        this.users = users;
    }

    public List<UserData> getUsers() {
        return users;
    }

}
//klasa calosci bazy danych root element userData
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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

//Każde niestatyczne, nietrwałe pole w klasie powiązanej z JAXB zostanie automatycznie powiązane z kodem XML, chyba że zostanie opatrzone adnotacją XmlTransient.
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "email",
        "password"
})
public class UserData
{
    //Mapuje właściwość JavaBean na element XML pochodzący z nazwy właściwości.
    @XmlElement
    private String email;

    @XmlElement
    private String password;

    public UserData() {}

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

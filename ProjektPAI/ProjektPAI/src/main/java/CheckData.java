import org.elasticsearch.client.transport.TransportClient;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;

public class CheckData
{
    private TransportClient client = null;
    private DataInputStream dataInput = null;
    private DataOutputStream dataOutput = null;

    public CheckData(TransportClient client, DataInputStream input, DataOutputStream output)
    {
        this.client = client;
        this.dataInput = input;
        this.dataOutput = output;
    }

    public String check(int operation) throws IOException, JAXBException
    {
        if(operation == 1)
        {
            String email = dataInput.readUTF();
            String password = dataInput.readUTF();
            XMLDataBase xmlDataBase = new XMLDataBase();
            List<UserData> usersList = xmlDataBase.readBase().getUsers();

            for(int i = 0;i < usersList.size(); i++)
            {
                UserData user = usersList.get(i);
                if(user.getEmail().equals(email))
                {
                    if(user.getPassword().equals(password))
                    {
                        return "Udało się zalogować.";
                    }
                    else
                    {
                        return "Błędne hasło!";
                    }
                }
            }
            return "Nie ma użytkownika o takim emailu.";
        }
        else if(operation == 2)
        {
            String email = dataInput.readUTF();
            String password = dataInput.readUTF();
            String nextPassword = dataInput.readUTF();
            XMLDataBase xmlDataBase = new XMLDataBase();
            List<UserData> usersList = xmlDataBase.readBase().getUsers();

            for(int i = 0;i < usersList.size(); i++)
            {
                UserData user = usersList.get(i);
                if (user.getEmail().equals(email))
                {
                    return "Użytkownik o takim e-mailu już istnieje.";
                }
            }

            if(!password.equals(nextPassword))
            {
                return "Podane hasła muszą być identyczne!";
            }
            else
            {
                UserData newUserData = new UserData(email, password);
                usersList.add(newUserData);
                Users users = xmlDataBase.readBase();
                users.setUsers(usersList);
                xmlDataBase.writeBase(users);
                return "Udało się zarejestrować użytkownika.";
            }
        }
        else if(operation == 3)
        {
            String email = dataInput.readUTF();
            XMLDataBase xmlDataBase = new XMLDataBase();
            List<UserData> usersList = xmlDataBase.readBase().getUsers();

            for(int i = 0;i < usersList.size(); i++)
            {
                UserData user = usersList.get(i);
                if (user.getEmail().equals(email))
                {
                    dataOutput.writeUTF("OK");
                    String password = dataInput.readUTF();
                    String nextPassword = dataInput.readUTF();

                    if(!password.equals(nextPassword))
                    {
                        return "Podane hasła muszą być identyczne!";
                    }
                    else
                    {
                        user.setPassword(password);
                        Users users = xmlDataBase.readBase();
                        users.setUsers(usersList);
                        xmlDataBase.writeBase(users);
                        return "Udało się stworzyć nowe hasło.";
                    }
                }
            }
            return "Nie ma użytkownika o takim emailu.";
        }
        else if(operation == 4)
        {
            String phrase = dataInput.readUTF();
            SearchPhrase searchPhrase = new SearchPhrase(client);
            List<String> books = searchPhrase.search(phrase);
            dataOutput.writeInt(books.size());
            for(String book : books)
            {
                dataOutput.writeUTF(book);
            }
            dataOutput.flush();
            return "Udało się przeanalizować frazę.";
        }
        return "Błędny numer!";
    }
}

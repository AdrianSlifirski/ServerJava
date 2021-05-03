import java.io.*;
import java.util.Scanner;

public class InputData
{
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private Scanner scan;

    public InputData(DataInputStream input, DataOutputStream output)
    {
        this.dataInput = input;
        this.dataOutput = output;
        this.scan = new Scanner(System.in);
    }

    public String checkType(int operation) throws IOException
    {
        String email = "";
        if(operation != 4)
        {
            System.out.print("Podaj swój email:");
            email = scan.nextLine();
        }
        if(operation == 1)
        {
            System.out.print("Podaj swoje hasło: ");
            String password = scan.nextLine();
            dataOutput.writeInt(operation);
            dataOutput.writeUTF(email);
            dataOutput.writeUTF(password);
            dataOutput.flush();
            String message = dataInput.readUTF();
            return message;
        }
        else if(operation == 2)
        {
            System.out.print("Podaj swoje hasło: ");
            String password = scan.nextLine();
            System.out.print("Powtórz hasło: ");
            String nextPassword = scan.nextLine();
            dataOutput.writeInt(operation);
            dataOutput.writeUTF(email);
            dataOutput.writeUTF(password);
            dataOutput.writeUTF(nextPassword);
            dataOutput.flush();
            String message = dataInput.readUTF();
            return message;
        }
        else if(operation == 3)
        {
            dataOutput.writeInt(operation);
            dataOutput.writeUTF(email);
            dataOutput.flush();
            String message = dataInput.readUTF();
            if(message.equals("Nie ma użytkownika o takim emailu."))
            {
                return message;
            }
            System.out.print("Podaj swoje hasło: ");
            String password = scan.nextLine();
            System.out.print("Powtórz hasło: ");
            String nextPassword = scan.nextLine();
            dataOutput.writeUTF(password);
            dataOutput.writeUTF(nextPassword);
            dataOutput.flush();
            message = dataInput.readUTF();
            return message;
        }
        else if(operation == 4)
        {
            System.out.print("Wpisz fraze którą chcesz znaleźć: ");
            String phrase = scan.nextLine();
            dataOutput.writeInt(operation);
            dataOutput.writeUTF(phrase);
            dataOutput.flush();
            int size = dataInput.readInt();
            System.out.println("Udało się znaleźć " + size + " ksiażek zgodnych z podaną frazą:");
            for(int i = 0; i < size; i++)
            {
                String book = dataInput.readUTF();
                System.out.println("    " + (i+1) + ". " + book);
            }
            String message = dataInput.readUTF();
            return message;
        }
        return "Błędny numer!";
    }
}

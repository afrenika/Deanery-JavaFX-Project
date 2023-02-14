package kfu.deanery;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class User {
    private final String login;
    private final String password;
    static final String file = "Users.txt";

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean compare(){
        boolean flag = false;
        try(Scanner scanner = new Scanner(new File(file))){
            while (scanner.hasNext() && !flag){
                String[] s = scanner.nextLine().split(" ");
                if (login.equals(s[0])&&password.equals(s[1])){
                    flag = true;
            }}
        }
        catch (IOException e){
            e.printStackTrace();}
        return flag;
    }

    public boolean loginning(){
        boolean flag = false;
        try(Scanner scanner = new Scanner(new File(file))){
            while (scanner.hasNext() && !flag){
                String[] s = scanner.nextLine().split(" ");
                if (login.equals(s[0])){
                    flag = true;
                }}}
        catch (FileNotFoundException ignored){}

        if (!flag){
            try(FileWriter writer = new FileWriter(file, true)) {
                if (new File(file).length() != 0){
                    writer.append("\n");}
                writer.append(login).append(" ").append(password);}
            catch (IOException e){
                e.printStackTrace();
        }}
        return !flag;
    }

}

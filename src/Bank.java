import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public String getNewUserUUID(){
        String uuid;
        Random rnd = new Random();
        int len = 6;
        boolean nonUnique = false;

        do{
            uuid = "";
            for(int c = 0; c < len; c++){
                uuid += ((Integer) rnd.nextInt(10)).toString();
            }

            for(User u : this.users){
                if(uuid.compareTo(u.getUuid()) == 0){
                    nonUnique = true;
                    break;
                }
            }
        }while(nonUnique);

        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getNewAccountUUID(){
        String uuid;
        Random rnd = new Random();
        int len = 10;
        boolean nonUnique = false;

        do{
            uuid = "";
            for(int c = 0; c < len; c++){
                uuid += ((Integer) rnd.nextInt(10)).toString();
            }

            for(Account a : this.accounts){
                if(uuid.compareTo(a.getUuid()) == 0){
                    nonUnique = true;
                    break;
                }
            }
        }while(nonUnique);

        return uuid;
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public User addUser(String firstName, String lastName, String pin){
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);
        //Create a savings account for the user and add to User and Bank accounts lists
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);
        return newUser;
    }

    public User userLogin(String userID, String pin){
        for(User u : this.users){
            //Check user ID is correct
            if(u.getUuid().compareTo(userID) == 0 && u.validatePin(pin)){
                return u;
            }
        }
        return null;
    }
}


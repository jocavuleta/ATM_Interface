import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String uuid;
    private byte[] pinHash;
    private ArrayList<Account> account;

    public User(String firstName, String lastName, String pin, Bank bank) {
        this.firstName = firstName;
        this.lastName = lastName;
        //Store the pin MD5 hash rather than the original value, for security reasons
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error, caught NoSuchAlgorithmException!");
            e.printStackTrace();
            System.exit(1);
        }

        //get a new unique universal ID for the user
        this.uuid = bank.getNewUserUUID();

        //create empty list of accounts
        this.account = new ArrayList<>();
        System.out.println("New user "+ this.firstName +", "+this.lastName +" with ID "+this.uuid+" was created.\n");
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void addAccount(Account account){
        this.account.add(account);
    }

    public boolean validatePin(String pin){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void printAccountsSummary(){
        System.out.println("\n\n"+this.firstName+"'s account summary");
        for(int a = 0; a < account.size(); a++){
            //System.out.println("\n"+ a + 1+ ") "+ this.account.get(a).getSummaryLine() + "\n");
            System.out.printf("%d) %s\n", a+1, this.account.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts(){
        return this.account.size();
    }

    public void printAcctTransHistory(int acctIndex){
        this.account.get(acctIndex).printTransHistory();
    }

    public double getAcctBalance(int acctIndex){
        return this.account.get(acctIndex).getBalance();
    }

    public String getAcctUUID(int acctIndex){
        return this.account.get(acctIndex).getUuid();
    }

    public void addAccountTransaction(int acctIndex, double amount, String memo){
        this.account.get(acctIndex).addTransaction(amount, memo);
    }
}

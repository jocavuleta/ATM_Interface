import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Bank theBank = new Bank("Bank of Drausin");

        User aUser = theBank.addUser("John", "Doe", "1234");

        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while(true){
            curUser = ATM.mainMenuPrompt(theBank, scanner);

            ATM.printUserMenu(curUser, scanner);
        }
    }





    public static User mainMenuPrompt(Bank theBank, Scanner scanner){
        String userID;
        String pin;
        User authUser;

        do{
            System.out.println("\nWelcome to"+theBank.getName()+"\n\n");
            System.out.print("Enter user ID: ");
            userID = scanner.nextLine();
            System.out.print("Enter pin: ");
            pin = scanner.nextLine();

            authUser = theBank.userLogin(userID, pin);
            if(authUser == null)
                System.out.println("Incorrect user ID/pin combination. " + "Please try again.");
        }while(authUser == null);

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner scanner){
        theUser.printAccountsSummary();

        int choice;

        do{
            System.out.println("Welcome "+theUser.getFirstName()+", what would you like to do?");
            System.out.println(" 1) Show account transaction history");
            System.out.println(" 2) Withdraw");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.print("Enter choice:");
            choice = scanner.nextInt();

            if(choice < 1 || choice > 5){
                System.out.println("Invalid choice. Please choose 1 - 5");
            }
        }while (choice < 1 || choice > 5);

        switch (choice){
            case 1:
                ATM.showTransHistory(theUser, scanner);
                break;
            case 2:
                ATM.withdrawFunds(theUser, scanner);
                break;
            case 3:
                ATM.depositFunds(theUser, scanner);
                break;
            case 4:
                ATM.transferFunds(theUser, scanner);
                break;
            case 5:
                scanner.nextLine();
                break;
        }

        if(choice != 5)
            ATM.printUserMenu(theUser, scanner);
    }

    public static void  showTransHistory(User theUser, Scanner scanner){
        int theAcct;

        do{
            System.out.format("Enter the number (1-%d) of the account whose transactions  you want to see: ", theUser.numAccounts());
            theAcct = scanner.nextInt() - 1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts())
                System.out.println("Invalid account. Please try again");
        }while (theAcct < 0 || theAcct >= theUser.numAccounts());

        theUser.printAcctTransHistory(theAcct);
    }

    public static void transferFunds(User theUser, Scanner scanner){
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;
        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of account to transfer from: ", theUser.numAccounts());
            fromAcct = scanner.nextInt() - 1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        //get the account to transfer to
        acctBal = theUser.getAcctBalance(fromAcct);

        do{
            System.out.printf("Enter the number (1-%d) of account to transfer to: ", theUser.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while (toAcct < 0 || toAcct >= theUser.numAccounts());

        //get the amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = scanner.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero.");
            }else if(amount > acctBal){
                System.out.printf("Amount must not be greater than balance of $%.02f.", acctBal);
            }
        }while (amount < 0 || amount >acctBal);

        //finally, the transfer
        theUser.addAccountTransaction(fromAcct, -1 * amount, String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));

        theUser.addAccountTransaction(toAcct, amount, String.format("Transfer from account %s", theUser.getAcctUUID(fromAcct)));
    }

    public static void withdrawFunds(User theUser, Scanner scanner){
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of account to withdraw from: ", theUser.numAccounts());
            fromAcct = scanner.nextInt() - 1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);

        //get the amount to transfer
        do{
            System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
            amount = scanner.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero.");
            }else if(amount > acctBal){
                System.out.printf("Amount must not be greater than balance of $%.02f.", acctBal);
            }
        }while (amount < 0 || amount >acctBal);

        //gobble up the rest of previous input
        scanner.nextLine();

        //get a memo
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();

        //do the withdraw
        theUser.addAccountTransaction(fromAcct, -1 * amount, memo);
    }

    public static void depositFunds(User theUser, Scanner scanner){
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of account to in: ", theUser.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        //get the amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = scanner.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero.");
            }
        }while (amount < 0);

        //gobble up the rest of previous input
        scanner.nextLine();

        //get a memo
        System.out.print("Enter a memo: ");
        memo = scanner.nextLine();

        //do the withdrawl
        theUser.addAccountTransaction(toAcct, amount, memo);
    }
}

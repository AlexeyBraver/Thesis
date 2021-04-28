import java.util.*;

public class CashInterface {

    private static final int USER_ID = 0;
    private static final int ACCOUNT_ID = 0;
    private static final int PIN_CODE = 3;
    private static final int FIRST_NAME = 1;
    private static final int USER_ACCOUNTS = 4;
    private static final int ACCOUNT_NAME = 1;
    private static final int TRANSACTION_AMOUNT = 0;
    private static final int TRANSACTION_MEMO = 1;
    private static final int ACCOUNT_BALANCE = 2;

    //Entry example: [Bank Name, [User IDs], [Accounts IDs]]
    ArrayList<ArrayList<String>> banks = new ArrayList<>();

    //Entry example: [User ID, Firset Name, Last Name, PIN hash, [Accounts IDs]]
    ArrayList<ArrayList<String>> users = new ArrayList<>();

    //Entry example: [Account ID, Account name, User ID,  [Transactions IDs]]
    ArrayList<ArrayList<String>> accounts = new ArrayList<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String bankName = "Bank of 3igen3ggy";


        Bank theBank = new Bank("Bank of 3igen3ggy");
        User aUser = theBank.addUser("Carl", "Johnson", "1234");
        Account checking = new Account("Checking", aUser, theBank);
        aUser.addAccount(checking);
        theBank.addAccount(checking);
        Account credit = new Account("Credit", aUser, theBank);
        aUser.addAccount(credit);
        theBank.addAccount(credit);
        User curUser;

        while (true) {
            curUser = CashInterface.mainMenuPrompt(theBank, sc);
            CashInterface.printUserMenu(curUser, sc);
        }


    }


    public static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userID;
        String pin;
        User authUser;

        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin. " + "Please try again.");
            }
        } while (authUser == null);
        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc) {
        theUser.printAccountsSummary();
        int choice;
        do {
            System.out.printf("\nWelcome %s, what would you like to do? \n", theUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Withdraw");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Quit");
            System.out.println();
            System.out.println("Enter choice: ");
            choice = sc.nextInt();
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Use 1-5");
            }

        } while (choice < 1 || choice > 5);
        switch (choice) {
            case 1:
                CashInterface.showTransHistory(theUser, sc);
                break;
            case 2:
                CashInterface.withdrawFunds(theUser, sc);
                break;
            case 3:
                CashInterface.depositFunds(theUser, sc);
                break;
            case 4:
                CashInterface.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }
        // redisplay menu
        if (choice != 5) {
            CashInterface.printUserMenu(theUser, sc);
        }
    }




//------------------------------------------------------------BANK-----------------------------------------------------

    public String getNewUserUUID() {
        String uuid;
        Random rnd = new Random();
        int len = 6;
        int id;
        int i = 0;
        int r = 0;
        boolean nonUnique;

        while (i < len - 1) {
            r += 9 * Math.pow(10, i);
            i++;
        }

        do {
            id = (int) (9e5 * Math.random() + r);
            uuid = Integer.toString(id);

            nonUnique = false;

            for (ArrayList<String> u : users) {
                if (uuid.compareTo(u.get(USER_ID)) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid;
    }

    public String getNewAccountUUID() {

        String uuid;
        Random rnd = new Random();
        int len = 10;
        int id;
        int max = (int) 1e10;
        int min = (int) 1e9;
        boolean nonUnique;

        do {
            id = (int) ((max - min + 1) * Math.random() + min);
            uuid = Integer.toString(id);
            nonUnique = false;
            for (ArrayList<String> a : accounts) {
                if (uuid.compareTo(a.get(ACCOUNT_ID)) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;
    }

    public void addAccount(String bankId, ArrayList<String> anAcct) {
        ArrayList<ArrayList<String>> bankAccounts = getBankAccounts(bankId);
        bankAccounts.add(anAcct);
    }

    private ArrayList<ArrayList<String>> getBankAccounts(String bankId) {
        return null;
    }

    private ArrayList<ArrayList<String>> getBankUsers(String bankId) {
        return null;
    }

    public ArrayList<String> addUser(String firstName, String lastName, String pin, String bankId) {
        String userId = this.getNewUserUUID();
        ArrayList<String> newUser = new ArrayList<>(Arrays.asList(userId, firstName, lastName, pin));

        String accountId = this.getNewAccountUUID();
        ArrayList<String> newAccount = new ArrayList<>(Arrays.asList(accountId, "Savings", userId,  "[]"));
        newUser.add("[" + accountId + "]");
        ArrayList<ArrayList<String>> bankAccounts = getBankAccounts(bankId);
        ArrayList<ArrayList<String>> bankUsers = getBankUsers(bankId);
        bankUsers.add(newUser);
        bankAccounts.add(newAccount);
        return newUser;
    }

    public ArrayList<String> userLogin(String bankId, String userID, String pin) {
        ArrayList<ArrayList<String>> bankUsers = getBankUsers(bankId);
        for (ArrayList<String> u : bankUsers) {
            if (u.get(USER_ID).compareTo(userID) == 0 && validatePin(u, pin)) {
                return u;
            }
        }
        return null;
    }


//------------------------------------------------------------USER------------------------------------------------------


    public boolean validatePin(ArrayList<String> user, String pinCode) {

        return pinCode.compareTo(user.get(PIN_CODE)) == 0;
    }


    public void printAccountsSummary(String bankId, ArrayList<String> user) {
        System.out.printf("\n%s's accounts summary\n", user.get(FIRST_NAME));
        ArrayList<String> userAccounts = getUserAccounts(user);
        for (int a = 0; a < userAccounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, getSummaryLine(bankId, userAccounts.get(a)));
        }
    }

    public int numAccounts(ArrayList<String> user) {
        return getUserAccounts(user).size();
    }

    private ArrayList<String> getUserAccounts(ArrayList<String> user) {
        return null;
    }

    public void printAcctTrnsHistory(String bankId, String acctIdx) {
        ArrayList<ArrayList<String>> bankAccounts = getBankAccounts(bankId);
        printTransHistory(bankId, acctIdx);
    }

    public double getAcctBalance(String bankId, String acctId) {
        ArrayList<String> account = getAccountById(bankId, acctId);
        return Double.parseDouble(account.get(ACCOUNT_BALANCE));
    }


    public void addAcctTransaction(String bankId, String acctId, String amount, String memo) {
        ArrayList<String> account = getAccountById(bankId, acctId);
        addTransaction(amount, memo, bankId, acctId);
    }



//-------------------------------------------------ACCOUNT-------------------------------------------------------------

    public String getSummaryLine(String bankId, String accountId) {

        double balance = getBalance(bankId, accountId);
        // format summary line depending on the whether the balance is negative
        if (balance >= 0) {
            return String.format("%s : %.02fPLN : %s", accountId, balance, getAccountById(bankId, accountId).get(ACCOUNT_NAME));
        } else {
            return String.format("%s : (%.02f)PLN : %s", accountId, balance, getAccountById(bankId, accountId).get(ACCOUNT_NAME));
        }
    }

    private ArrayList<String> getAccountById(String bankId, String accountId) {
        ArrayList<ArrayList<String>> bankAccounts = getBankAccounts(bankId);
        return null;
    }

    public double getBalance(String bankId, String accountId) {
        double balance = 0;
        ArrayList<ArrayList<String>> transactions = getTransactionsByAccountId(bankId, accountId);
        for (ArrayList<String> t : transactions) {
            balance += Integer.parseInt(t.get(TRANSACTION_AMOUNT));
        }
        return balance;
    }

    private ArrayList<ArrayList<String>> getTransactionsByAccountId(String bankId, String accountId) {
        return null;
    }

    public void printTransHistory(String bankId, String accountId) {
        ArrayList<ArrayList<String>> transactions = getTransactionsByAccountId(bankId, accountId);
        System.out.printf("\nTransaction history for account %s\n", accountId);
        for (int t = transactions.size() - 1; t >= 0; t--) {
            System.out.println(getTransactionSummaryLine(transactions.get(t)));
        }
        System.out.println();
    }

    public void addTransaction(String amount, String memo, String bankId, String accountId) {
        ArrayList<ArrayList<String>> transactions = getTransactionsByAccountId(bankId, accountId);
        ArrayList<String> newTrans = new ArrayList<>(Arrays.asList(amount, memo));
        transactions.add(newTrans);
    }



//-----------------------------------------------Transaction-----------------------------------------------------------


    public String getTransactionSummaryLine(ArrayList<String> s) {
        if (Integer.parseInt(s.get(TRANSACTION_AMOUNT)) >= 0) {
            return String.format("%.02fPLN : %s", Double.parseDouble(s.get(TRANSACTION_AMOUNT)), s.get(TRANSACTION_MEMO));
        } else {
            return String.format("-%.02fPLN : %s", -Double.parseDouble(s.get(TRANSACTION_AMOUNT)), s.get(TRANSACTION_MEMO));
        }

    }
//---------------------------------------------------------------------------------------------------------------------

    public static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;

        do {
            System.out.printf("Enter the number (1-%d) of the account" + " Whose transactions you want to see: ",
                    theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());
        theUser.printAcctTrnsHistory(theAcct);
    }

    public static void transferFunds(User theUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from : ",
                    theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        // transfer to

        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to : ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again.");
            }

        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        // amount to transfer to

        do {
            System.out.printf("Enter the amount to transfer (max %.02fPLN) ", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" + "balance of %.02fPLN.\n", acctBal);
            }

        } while (amount < 0 || amount > acctBal);
        // transfer
        theUser.addAcctTransaction(fromAcct, -1 * amount,
                String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount,
                String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
    }

    public static void withdrawFunds(User theUser, Scanner sc) {

        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        // transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from:", theUser.numAccounts());

            fromAcct = sc.nextInt() - 1;

            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again.");
            }

        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
        do {
            System.out.printf("Enter the amount to withdraw (max %.02fPLN) ", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" + "balance of %.02fPLN.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        sc.nextLine();
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();
        theUser.addAcctTransaction(fromAcct, -1 * amount, memo);
    }

    public static void depositFunds(User theUser, Scanner sc) {

        int toAcct;
        double amount;
        double acctBal;
        String memo;
        // transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);
        do {
            System.out.printf("Enter the amount to transfer (max %.02fPLN)", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            }

        } while (amount < 0);
        sc.nextLine();
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();
        theUser.addAcctTransaction(toAcct, amount, memo);

    }
}

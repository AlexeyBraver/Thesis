import java.util.*;

public class CashInterface {

    private static final int USER_ID = 0;
    private static final int USER_FIRST_NAME = 1;
    private static final int USER_LAST_NAME = 2;
    private static final int USER_PIN_CODE = 3;
    private static final int USER_ACCOUNTS = 4;
    private static final int ACCOUNT_ID = 0;
    private static final int ACCOUNT_NAME = 1;
    private static final int ACCOUNT_OWNER_ID = 2;
    private static final int ACCOUNT_TRANSACTIONS = 3;
    private static final int ACCOUNT_BALANCE = 2;
    private static final int TRANSACTION_ID = 0;
    private static final int TRANSACTION_AMOUNT = 1;
    private static final int TRANSACTION_MEMO = 2;
    private static final int TRANSACTION_ACCOUNT = 4;
    private static final int DB_BANKS = 0;
    private static final int DB_USERS = 1;
    private static final int DB_ACCOUNTS = 2;
    private static final int DB_TRANSACTIONS = 3;
    private static final int BANK_NAME = 0;
    private static final int BANK_USERS = 1;
    private static final int BANK_ACCOUNTS = 2;



    static ArrayList<ArrayList<ArrayList<String>>> db = new ArrayList<>();


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<String> theBank = new ArrayList<>(Arrays.asList("Bank of 3igen3ggy", "[]", "[]"));
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




    public String getNewUserUUID() {
        ArrayList<ArrayList<String>> users = db.get(DB_USERS);
        String uuid;
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

    public String getNewTransactionUID() {
        ArrayList<ArrayList<String>> transactions = db.get(DB_TRANSACTIONS);
        String uuid;
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

            for (ArrayList<String> t : transactions) {
                if (uuid.compareTo(t.get(TRANSACTION_ID)) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }

    public String getNewAccountUUID() {
        ArrayList<ArrayList<String>> accounts = db.get(DB_ACCOUNTS);
        String uuid;
        Random rnd = new Random();
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

    public void addAccountToBank(String bankName, String accountId) {
        ArrayList<ArrayList<String>> allBanks = db.get(DB_BANKS);
        for (ArrayList<String> bank : allBanks) {
            if (bank.get(BANK_NAME).equals(bankName)) {
                String curBankAccounts = bank.get(BANK_ACCOUNTS);
                String bankAccountsAfterAdding = curBankAccounts.substring(0, curBankAccounts.length() - 2) + "," + accountId + "]";
                bank.remove(BANK_ACCOUNTS);
                bank.add(BANK_ACCOUNTS, bankAccountsAfterAdding);
            }
        }
    }

    private String getBankAccounts(String bankName) {
        ArrayList<ArrayList<String>> allBanks = db.get(DB_BANKS);
        for (ArrayList<String> bank : allBanks) {
            if (bank.get(BANK_NAME).equals(bankName)) return bank.get(BANK_ACCOUNTS);
        }
        return "";
    }

    private String getBankUsers(String bankName) {
        ArrayList<ArrayList<String>> allBanks = db.get(DB_BANKS);
        for (ArrayList<String> bank : allBanks) {
            if (bank.get(BANK_NAME).equals(bankName)) return bank.get(BANK_USERS);
        }
        return "";
    }

    public ArrayList<String> addUserToBank(String firstName, String lastName, String pin, String bankName) {
        String userId = getNewUserUUID();
        ArrayList<String> newUser = new ArrayList<>(Arrays.asList(userId, firstName, lastName, pin));
        String accountId = getNewAccountUUID();
        ArrayList<String> newAccount = new ArrayList<>(Arrays.asList(accountId, "Savings", userId, "[]"));
        newUser.add("[" + accountId + "]");
        addAccountToBank(bankName, accountId);
        db.get(DB_USERS).add(newUser);
        db.get(DB_ACCOUNTS).add(newAccount);
        return newUser;
    }

    public ArrayList<String> userLogin(String userID, String pin) {
        ArrayList<ArrayList<String>> users = db.get(DB_USERS);
        for (ArrayList<String> u : users) {
            if (u.get(USER_ID).compareTo(userID) == 0 && validatePin(u, pin)) {
                return u;
            }
        }
        return null;
    }


    public boolean validatePin(ArrayList<String> user, String pinCode) {

        return pinCode.compareTo(user.get(USER_PIN_CODE)) == 0;
    }


    public void printAccountsSummary(ArrayList<String> user) {
        System.out.printf("\n%s's accounts summary\n", user.get(USER_FIRST_NAME));
        ArrayList<ArrayList<String>> userAccounts = getUserAccounts(user);
        for (int a = 0; a < userAccounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, getSummaryLine(userAccounts.get(a).get(ACCOUNT_ID)));
        }
    }

    public static int numAccounts(ArrayList<String> user) {
        String[] userAccounts = user.get(USER_ACCOUNTS).split(",");
        return userAccounts.length;
    }

    private ArrayList<ArrayList<String>> getUserAccounts(ArrayList<String> user) {
        ArrayList<ArrayList<String>> allAccounts = db.get(DB_ACCOUNTS);
        ArrayList<ArrayList<String>> userAccounts = new ArrayList<>();

        for (ArrayList<String> account : allAccounts) {
            if (account.get(ACCOUNT_OWNER_ID).equals(user.get(USER_ID))) userAccounts.add(account);
        }
        return userAccounts;
    }

    public void printAcctTrnsHistory(String acctIdx) {
        printTransHistory(acctIdx);
    }

    public static double getAcctBalance(String acctId) {
        ArrayList<String> account = getAccountById(acctId);
        return Double.parseDouble(account.get(ACCOUNT_BALANCE));
    }


    public void addAcctTransaction(String bankId, String acctId, String amount, String memo) {
        ArrayList<String> account = getAccountById(acctId);
        addTransaction(amount, memo, bankId, acctId);
    }


    public String getSummaryLine(String accountId) {

        double balance = getBalance(accountId);
        // format summary line depending on the whether the balance is negative
        if (balance >= 0) {
            return String.format("%s : %.02fPLN : %s", accountId, balance, getAccountById(accountId).get(ACCOUNT_NAME));
        } else {
            return String.format("%s : (%.02f)PLN : %s", accountId, balance, getAccountById(accountId).get(ACCOUNT_NAME));
        }
    }

    private static ArrayList<String> getAccountById(String bankName, String accountId) {
        ArrayList<ArrayList<String>> allAccounts = db.get(DB_ACCOUNTS);
        for (ArrayList<String> account : allAccounts) {
            if (account.get(ACCOUNT_ID).equals(accountId)) return account;
        }
        return null;
    }

    public double getBalance(String accountId) {
        double balance = 0;
        ArrayList<ArrayList<String>> transactions = getTransactionsByAccountId(accountId);
        for (ArrayList<String> t : transactions) {
            balance += Integer.parseInt(t.get(TRANSACTION_AMOUNT));
        }
        return balance;
    }

    private ArrayList<ArrayList<String>> getTransactionsByAccountId(String accountId) {
        ArrayList<ArrayList<String>> allTransactions = db.get(DB_TRANSACTIONS);
        ArrayList<ArrayList<String>> givenAccountTransactions = new ArrayList<>();
        for (ArrayList<String> transaction : allTransactions) {
            if (transaction.get(TRANSACTION_ACCOUNT).equals(accountId)) givenAccountTransactions.add(transaction);
        }
        return givenAccountTransactions;
    }

    public void printTransHistory(String accountId) {
        ArrayList<ArrayList<String>> transactions = getTransactionsByAccountId(accountId);
        System.out.printf("\nTransaction history for account %s\n", accountId);
        for (int t = transactions.size() - 1; t >= 0; t--) {
            System.out.println(getTransactionSummaryLine(transactions.get(t)));
        }
        System.out.println();
    }

    public ArrayList<String> addTransaction(String amount, String memo, String bankName, String accountId) {
        String transactionId = getNewTransactionUID();
        ArrayList<ArrayList<String>> allTransactions = db.get(DB_TRANSACTIONS);
        ArrayList<String> newTransaction = new ArrayList<>(Arrays.asList(transactionId, amount, new Date().toString(), memo, accountId));
        allTransactions.add(newTransaction);
        ArrayList<String> curAccount = getAccountById(accountId);
        String curAccountTransactions = curAccount.get(ACCOUNT_TRANSACTIONS);
        String transactionsAfterAdding = curAccountTransactions.substring(0, curAccountTransactions.length() - 2) + "," + transactionId + "]";
        curAccount.remove(ACCOUNT_TRANSACTIONS);
        curAccount.add(ACCOUNT_TRANSACTIONS, transactionsAfterAdding);
        return newTransaction;
    }


    public String getTransactionSummaryLine(ArrayList<String> s) {
        if (Integer.parseInt(s.get(TRANSACTION_AMOUNT)) >= 0) {
            return String.format("%.02fPLN : %s", Double.parseDouble(s.get(TRANSACTION_AMOUNT)), s.get(TRANSACTION_MEMO));
        } else {
            return String.format("-%.02fPLN : %s", -Double.parseDouble(s.get(TRANSACTION_AMOUNT)), s.get(TRANSACTION_MEMO));
        }

    }

    public static void showTransHistory(ArrayList<String> theUser, Scanner sc) {
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

    public static void transferFunds(ArrayList<String> theUser, Scanner sc) {
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

    public static void withdrawFunds(ArrayList<String> theUser, Scanner sc) {

        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        // transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from:", CashInterface.numAccounts(theUser));

            fromAcct = sc.nextInt() - 1;

            if (fromAcct < 0 || fromAcct >= CashInterface.numAccounts(theUser)) {
                System.out.println("Invalid account. Try again.");
            }

        } while (fromAcct < 0 || fromAcct >= CashInterface.numAccounts(theUser));
        acctBal = CashInterface.getAcctBalance(fromAcct);
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

    public static void depositFunds(ArrayList<String> theUser, Scanner sc) {

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

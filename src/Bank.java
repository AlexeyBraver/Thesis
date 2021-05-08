import java.util.*;

public class Bank {

    private static final int DB_USERS = 0;
    private static final int DB_ACCOUNTS = 1;
    private static final int DB_TRANSACTIONS = 2;
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


    static ArrayList<ArrayList<ArrayList<String>>> db = new ArrayList<>();


    public static void main(String[] args) {
    }


    public static String getNewUserUUID() {
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

    public static String getNewTransactionUID() {
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

    public static String getNewAccountUUID() {
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

    public static void addAccountToBank(String accountId, String accountName, String accountOwnerId) {
        ArrayList<String> newAccount = new ArrayList<>(Arrays.asList(accountId, "Savings", accountOwnerId, "[]"));
        db.get(DB_ACCOUNTS).add(newAccount);
    }


    public static ArrayList<String> addUserToBank(String firstName, String lastName, String pin) {
        String userId = getNewUserUUID();
        ArrayList<String> newUser = new ArrayList<>(Arrays.asList(userId, firstName, lastName, pin));
        String accountId = getNewAccountUUID();
        newUser.add("[" + accountId + "]");
        addAccountToBank(accountId, "Savings", userId);
        db.get(DB_USERS).add(newUser);
        return newUser;
    }

    public static ArrayList<String> userLogin(String userID, String pinCode) {
        ArrayList<ArrayList<String>> allUsers = db.get(DB_USERS);
        for (ArrayList<String> u : allUsers) {
            if (u.get(USER_ID).compareTo(userID) == 0 && pinCode.compareTo(u.get(USER_PIN_CODE)) == 0) {
                return u;
            }
        }
        return null;
    }


    public static boolean validatePin(ArrayList<String> user, String pinCode) {

        return pinCode.compareTo(user.get(USER_PIN_CODE)) == 0;
    }


    public static void printAccountsSummary(String userId) {
        ArrayList<String> user = getUserById(userId);
        System.out.printf("\n%s's accounts summary\n", user.get(USER_FIRST_NAME));
        ArrayList<ArrayList<String>> userAccounts = getUserAccounts(userId);
        for (int a = 0; a < userAccounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, getSummaryLine(userAccounts.get(a).get(ACCOUNT_ID)));
        }
    }


    public static int numAccountsOfUser(String userId) {
        ArrayList<String> user = getUserById(userId);
        String[] userAccounts = user.get(USER_ACCOUNTS).split(",");
        return userAccounts.length;
    }

    private static ArrayList<ArrayList<String>> getUserAccounts(String userId) {
        ArrayList<ArrayList<String>> allAccounts = db.get(DB_ACCOUNTS);
        ArrayList<ArrayList<String>> userAccounts = new ArrayList<>();
        for (ArrayList<String> account : allAccounts) {
            if (account.get(ACCOUNT_OWNER_ID).equals(userId)) userAccounts.add(account);
        }
        return userAccounts;
    }

    public static void printAcctTrnsHistory(String acctIdx) {
        printAccountTransactionsHistory(acctIdx);
    }

    public static double getAcctBalance(String acctId) {
        ArrayList<String> account = getAccountById(acctId);
        return Double.parseDouble(account.get(ACCOUNT_BALANCE));
    }


    public static String getSummaryLine(String accountId) {

        double balance = getBalance(accountId);
        // format summary line depending on the whether the balance is negative
        if (balance >= 0) {
            return String.format("%s : %.02fPLN : %s", accountId, balance, getAccountById(accountId).get(ACCOUNT_NAME));
        } else {
            return String.format("%s : (%.02f)PLN : %s", accountId, balance, getAccountById(accountId).get(ACCOUNT_NAME));
        }
    }

    private static ArrayList<String> getAccountById(String accountId) {
        ArrayList<ArrayList<String>> allAccounts = db.get(DB_ACCOUNTS);
        for (ArrayList<String> account : allAccounts) {
            if (account.get(ACCOUNT_ID).equals(accountId)) return account;
        }
        return null;
    }

    private static ArrayList<String> getUserById(String userId) {
        ArrayList<ArrayList<String>> allUsers = db.get(DB_USERS);
        for (ArrayList<String> user : allUsers) {
            if (user.get(USER_ID).equals(userId)) return user;
        }
        return null;
    }

    public static double getBalance(String accountId) {
        double balance = 0;
        ArrayList<ArrayList<String>> transactions = getAccountTransactions(accountId);
        for (ArrayList<String> t : transactions) {
            balance += Double.parseDouble(t.get(TRANSACTION_AMOUNT));
        }
        return balance;
    }

    private static ArrayList<ArrayList<String>> getAccountTransactions(String accountId) {
        ArrayList<ArrayList<String>> allTransactions = db.get(DB_TRANSACTIONS);
        ArrayList<ArrayList<String>> givenAccountTransactions = new ArrayList<>();
        for (ArrayList<String> transaction : allTransactions) {
            if (transaction.get(TRANSACTION_ACCOUNT).equals(accountId)) givenAccountTransactions.add(transaction);
        }
        return givenAccountTransactions;
    }

    public static void printAccountTransactionsHistory(String accountId) {
        ArrayList<ArrayList<String>> transactions = getAccountTransactions(accountId);
        System.out.printf("\nTransaction history for account %s\n", accountId);
        for (ArrayList<String> transaction : transactions) {
            System.out.println(getTransactionSummaryLine(transaction.get(TRANSACTION_ID)));
        }
        System.out.println();
    }

    public static ArrayList<String> addTransactionToaccount(String amount, String memo, String accountId) {
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


    public static String getTransactionSummaryLine(String transactionId) {
        ArrayList<String> transaction = getTransactionsById(transactionId);
        if (Integer.parseInt(transaction.get(TRANSACTION_AMOUNT)) >= 0) {
            return String.format("%.02fPLN : %s", Double.parseDouble(transaction.get(TRANSACTION_AMOUNT)), transaction.get(TRANSACTION_MEMO));
        } else {
            return String.format("-%.02fPLN : %s", -Double.parseDouble(transaction.get(TRANSACTION_AMOUNT)), transaction.get(TRANSACTION_MEMO));
        }

    }

    private static ArrayList<String> getTransactionsById(String transactionId) {
        ArrayList<ArrayList<String>> allTransactions = db.get(DB_TRANSACTIONS);
        for (ArrayList<String> transaction : allTransactions) {
            if (transaction.get(TRANSACTION_ID).equals(transactionId)) return transaction;
        }
        return null;
    }

    public static void showTransHistory(String userId, Scanner scanner) {
        int theAcctNumber;
        int numOfUserAccounts = numAccountsOfUser(userId);
        do {
            System.out.printf("Enter the number (1-%d) of the account" + " Whose transactions you want to see: ",
                    numOfUserAccounts);
            theAcctNumber = scanner.nextInt() - 1;
            if (theAcctNumber < 0 || theAcctNumber >= numOfUserAccounts) {
                System.out.println("Invalid account. Try again.");
            }
        } while (theAcctNumber < 0 || theAcctNumber >= numOfUserAccounts);
        ArrayList<ArrayList<String>> userAccounts = getUserAccounts(userId);
        printAcctTrnsHistory(userAccounts.get(theAcctNumber).get(ACCOUNT_ID));
    }

    public static void withdrawFunds(String userId, Scanner scanner) {
        int numOfUserAccounts = numAccountsOfUser(userId);
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        ArrayList<ArrayList<String>> userAccounts = getUserAccounts(userId);
        // transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from:", numOfUserAccounts);
            fromAcct = scanner.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= numOfUserAccounts) {
                System.out.println("Invalid account. Try again.");
            }

        } while (fromAcct < 0 || fromAcct >= numOfUserAccounts);
        acctBal = getAcctBalance(userAccounts.get(fromAcct).get(ACCOUNT_ID));
        do {
            System.out.printf("Enter the amount to withdraw (max %.02fPLN) ", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" + "balance of %.02fPLN.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        scanner.nextLine();
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();
        addTransactionToaccount(Double.toString(-1 * amount), memo, userAccounts.get(fromAcct).get(ACCOUNT_ID));
    }

    public static void depositFunds(String userId, Scanner scanner) {
        int numOfUserAccounts = numAccountsOfUser(userId);
        ArrayList<ArrayList<String>> userAccounts = getUserAccounts(userId);
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        // transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", numOfUserAccounts);
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= numOfUserAccounts) {
                System.out.println("Invalid account. Try again.");
            }
        } while (toAcct < 0 || toAcct >= numOfUserAccounts);
        acctBal = getAcctBalance(userAccounts.get(toAcct).get(ACCOUNT_ID));
        do {
            System.out.printf("Enter the amount to transfer (max %.02fPLN)", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            }
        } while (amount < 0);
        scanner.nextLine();
        System.out.print("Enter a memo: ");
        memo = scanner.nextLine();
        addTransactionToaccount(Double.toString(amount), memo, userAccounts.get(toAcct).get(ACCOUNT_ID));
    }
}

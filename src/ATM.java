import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Bank theBank = new Bank("Bank of 3igen3ggy");
		User aUser = theBank.addUser("Carl", "Johnson", "1234");
		Account checking = new Account("Checking", aUser, theBank);
		aUser.addAccount(checking);
		theBank.addAccount(checking);
		Account credit = new Account("Credit", aUser, theBank);
		aUser.addAccount(credit);
		theBank.addAccount(credit);
		User curUser;
		boolean showMainMenu = true;
		while (showMainMenu) {
			String userID;
			String pin;
			do {
				System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
				System.out.print("Enter user ID: ");
				userID = sc.nextLine();
				System.out.print("Enter pin: ");
				pin = sc.nextLine();
				curUser = theBank.userLogin(userID, pin);
				if (curUser == null) {
					System.out.println("Incorrect user ID/pin. " + "Please try again.");
				}
			} while (curUser == null);
			curUser.printAccountsSummary();
			int choice;
			do {
				System.out.printf("\nWelcome %s, what would you like to do? \n", curUser.getFirstName());
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
			int fromAcct;
			int toAcct;
			double amount;
			double acctBal;
			switch (choice) {
				case 1:
					int theAcct;
					do {
						System.out.printf("Enter the number (1-%d) of the account" + " Whose transactions you want to see: ",
								curUser.numAccounts());
						theAcct = sc.nextInt() - 1;
						if (theAcct < 0 || theAcct >= curUser.numAccounts()) {
							System.out.println("Invalid account. Try again.");
						}
					} while (theAcct < 0 || theAcct >= curUser.numAccounts());
					curUser.printAcctTrnsHistory(theAcct);
					showMainMenu = false;
					break;
				case 2:
					String memo;
					// transfer from
					do {
						System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from:", curUser.numAccounts());

						fromAcct = sc.nextInt() - 1;

						if (fromAcct < 0 || fromAcct >= curUser.numAccounts()) {
							System.out.println("Invalid account. Try again.");
						}

					} while (fromAcct < 0 || fromAcct >= curUser.numAccounts());
					acctBal = curUser.getAcctBalance(fromAcct);
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
					curUser.addAcctTransaction(fromAcct, -1 * amount, memo);
					showMainMenu = false;
					break;
				case 3:
					// transfer from
					do {
						System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", curUser.numAccounts());
						toAcct = sc.nextInt() - 1;
						if (toAcct < 0 || toAcct >= curUser.numAccounts()) {
							System.out.println("Invalid account. Try again.");
						}
					} while (toAcct < 0 || toAcct >= curUser.numAccounts());
					acctBal = curUser.getAcctBalance(toAcct);
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
					curUser.addAcctTransaction(toAcct, amount, memo);
					showMainMenu = false;
					break;
				case 4:
					// transfer from
					do {
						System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from : ",
								curUser.numAccounts());
						fromAcct = sc.nextInt() - 1;
						if (fromAcct < 0 || fromAcct >= curUser.numAccounts()) {
							System.out.println("Invalid account. Try again.");
						}
					} while (fromAcct < 0 || fromAcct >= curUser.numAccounts());
					acctBal = curUser.getAcctBalance(fromAcct);

					// transfer to

					do {
						System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to : ", curUser.numAccounts());
						toAcct = sc.nextInt() - 1;
						if (toAcct < 0 || toAcct >= curUser.numAccounts()) {
							System.out.println("Invalid account. Try again.");
						}

					} while (toAcct < 0 || toAcct >= curUser.numAccounts());

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
					curUser.addAcctTransaction(fromAcct, -1 * amount,
							String.format("Transfer to account %s", curUser.getAcctUUID(toAcct)));
					curUser.addAcctTransaction(toAcct, amount,
							String.format("Transfer to account %s", curUser.getAcctUUID(toAcct)));
					showMainMenu = false;
					break;
				case 5:
					sc.nextLine();
					showMainMenu = false;
					break;
			}
		}
	}
}

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

		while (true) {
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
			ATM.printUserMenu(curUser, sc);
		}
	}
//
//	public static User mainMenuPrompt(Bank theBank, Scanner sc) {
//		String userID;
//		String pin;
//		User authUser;
//
//		do {
//			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
//			System.out.print("Enter user ID: ");
//			userID = sc.nextLine();
//			System.out.print("Enter pin: ");
//			pin = sc.nextLine();
//			authUser = theBank.userLogin(userID, pin);
//			if (authUser == null) {
//				System.out.println("Incorrect user ID/pin. " + "Please try again.");
//			}
//		} while (authUser == null);
//		return authUser;
//	}

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
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		switch (choice) {
		case 1:
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
			break;
		case 2:
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
			break;
		case 3:
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
			break;
		case 4:
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
			break;
		case 5:
			sc.nextLine();
			break;
		}
		// redisplay menu
		if (choice != 5) {
			ATM.printUserMenu(theUser, sc);
		}
	}

//	public static void showTransHistory(User theUser, Scanner sc) {
//		int theAcct;
//
//		do {
//			System.out.printf("Enter the number (1-%d) of the account" + " Whose transactions you want to see: ",
//					theUser.numAccounts());
//			theAcct = sc.nextInt() - 1;
//			if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
//				System.out.println("Invalid account. Try again.");
//			}
//		} while (theAcct < 0 || theAcct >= theUser.numAccounts());
//		theUser.printAcctTrnsHistory(theAcct);
//	}

//	public static void transferFunds(User theUser, Scanner sc) {
//		int fromAcct;
//		int toAcct;
//		double amount;
//		double acctBal;
//
//		// transfer from
//		do {
//			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from : ",
//					theUser.numAccounts());
//			fromAcct = sc.nextInt() - 1;
//			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
//				System.out.println("Invalid account. Try again.");
//			}
//		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
//		acctBal = theUser.getAcctBalance(fromAcct);
//
//		// transfer to
//
//		do {
//			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to : ", theUser.numAccounts());
//			toAcct = sc.nextInt() - 1;
//			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
//				System.out.println("Invalid account. Try again.");
//			}
//
//		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
//
//		// amount to transfer to
//
//		do {
//			System.out.printf("Enter the amount to transfer (max %.02fPLN) ", acctBal);
//			amount = sc.nextDouble();
//			if (amount < 0) {
//				System.out.println("Amount must be greater than 0.");
//			} else if (amount > acctBal) {
//				System.out.printf("Amount must not be greater than\n" + "balance of %.02fPLN.\n", acctBal);
//			}
//
//		} while (amount < 0 || amount > acctBal);
//		// transfer
//		theUser.addAcctTransaction(fromAcct, -1 * amount,
//				String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
//		theUser.addAcctTransaction(toAcct, amount,
//				String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
//	}

//	public static void withdrawFunds(User theUser, Scanner sc) {
//
//		int fromAcct;
//		double amount;
//		double acctBal;
//		String memo;
//		// transfer from
//		do {
//			System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from:", theUser.numAccounts());
//
//			fromAcct = sc.nextInt() - 1;
//
//			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
//				System.out.println("Invalid account. Try again.");
//			}
//
//		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
//		acctBal = theUser.getAcctBalance(fromAcct);
//		do {
//			System.out.printf("Enter the amount to withdraw (max %.02fPLN) ", acctBal);
//			amount = sc.nextDouble();
//			if (amount < 0) {
//				System.out.println("Amount must be greater than 0.");
//			} else if (amount > acctBal) {
//				System.out.printf("Amount must not be greater than\n" + "balance of %.02fPLN.\n", acctBal);
//			}
//		} while (amount < 0 || amount > acctBal);
//		sc.nextLine();
//		System.out.println("Enter a memo: ");
//		memo = sc.nextLine();
//		theUser.addAcctTransaction(fromAcct, -1 * amount, memo);
//	}

//	public static void depositFunds(User theUser, Scanner sc) {
//
//		int toAcct;
//		double amount;
//		double acctBal;
//		String memo;
//		// transfer from
//		do {
//			System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", theUser.numAccounts());
//			toAcct = sc.nextInt() - 1;
//			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
//				System.out.println("Invalid account. Try again.");
//			}
//		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
//		acctBal = theUser.getAcctBalance(toAcct);
//		do {
//			System.out.printf("Enter the amount to transfer (max %.02fPLN)", acctBal);
//			amount = sc.nextDouble();
//			if (amount < 0) {
//				System.out.println("Amount must be greater than 0.");
//			}
//
//		} while (amount < 0);
//		sc.nextLine();
//		System.out.print("Enter a memo: ");
//		memo = sc.nextLine();
//		theUser.addAcctTransaction(toAcct, amount, memo);
//
//	}
}

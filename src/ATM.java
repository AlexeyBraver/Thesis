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
		runMainMenu(sc, curUser);

	}
}

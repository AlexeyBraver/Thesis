import java.util.ArrayList;
import java.util.Random;

public class Bank {

	private String name;
	private ArrayList<User> users;
	private ArrayList<Account> accounts;
	
	public Bank(String name) {
		this.name = name;
		this.users = new ArrayList<User>();
		this.accounts = new ArrayList<Account>();
	}

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

			for (User u : this.users) {
				if (uuid.compareTo(u.getUUID()) == 0) {
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
			for (Account a : this.accounts) {
				if (uuid.compareTo(a.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}

		} while (nonUnique);

		return uuid;
	}

	public void addAccount(Account anAcct) {
		this.accounts.add(anAcct);
	}

	public User addUser(String firstName, String lastName, String pin) {

		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);
		Account newAccount = new Account("Savings", newUser, this);
		newUser.addAccount(newAccount);
		this.addAccount(newAccount);

		return newUser;
	}

	public User userLogin(String userID, String pin) {
		for (User u : this.users) {
			if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
				return u;
			}
		}
		return null;
	}

	public String getName() {
		return this.name;
	}
}

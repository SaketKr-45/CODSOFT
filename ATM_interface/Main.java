package ATM_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class PinOutOfRangeException extends Exception{
    @Override
    public String toString() {
        return "PinOutOfRange{}";
    }
}

class BankAccount{
    private double bankBalance;
    private final int pin;

    class Transactions{
        private final double beforeTransaction;
        private final double transaction;
        private final double afterTransaction;

        public Transactions(double beforeTransaction, double transaction, double afterTransaction) {
            this.beforeTransaction = beforeTransaction;
            this.transaction = transaction;
            this.afterTransaction = afterTransaction;
        }

        public double getBeforeTransaction() {
            return beforeTransaction;
        }

        public double getTransaction() {
            return transaction;
        }

        public double getAfterTransaction() {
            return afterTransaction;
        }
    }
    private final List<Transactions> transactionHistory = new ArrayList<>();

    public BankAccount(double initialBankBalance, int pin) throws PinOutOfRangeException {
        this.bankBalance = initialBankBalance;

        if(pin < 1000 || pin > 9999){
            throw new PinOutOfRangeException();
        }
        else this.pin = pin;
    }

    public boolean checkPin(int pin){
        return this.pin == pin ? true : false;
    }

    public double getBankBalance() {
        return bankBalance;
    }

    public void deposit(double amount){
        this.transactionHistory.add(new Transactions(bankBalance , amount , bankBalance + amount));
        this.bankBalance += amount;
    }

    public void withdraw(double amount){
        this.transactionHistory.add(new Transactions(bankBalance , 0 - amount , bankBalance - amount));
        this.bankBalance -= amount;
    }

    public ArrayList<Transactions> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}

class ATM_machine{
    private BankAccount account;

    public ATM_machine(BankAccount account) {
        this.account = account;
    }

    public void handleUserInput() {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            int pin;
            while (true){
                try {
                    System.out.print("Enter pin: ");
                    pin = Integer.parseInt(br.readLine());

                    if(pin < 1000 || pin > 9999){
                        System.out.println("Pin is of 4-digits.");
                        continue;
                    }

                    if (account.checkPin(pin)){
                        while (true) {
                            System.out.println("1. Check Balance\n2. Deposit\n3. Withdraw\n4. Show transaction history\n5. End transaction");
                            int choice;
                            while(true){
                                try {
                                    System.out.print("Choose: ");
                                    choice = Integer.parseInt(br.readLine());

                                    if (choice < 1 || choice > 5){
                                        throw new IllegalArgumentException("Out of range");
                                    }
                                    break;
                                }catch (Exception e) {
                                    System.out.println("Invalid Input!! Enter number between 1-4.");
                                }
                            }

                            if (choice == 1){
                                checkBalance();
                            }
                            else if (choice == 2){
                                deposit(br);
                            }
                            else if (choice == 3){
                                withdraw(br);
                            }
                            else if (choice == 4){
                                showTransactionHistory();
                            }
                            else{
                                System.out.println("\nThanks for using our services. Have a good day!!");
                                return;
                            }
                        }
                    }
                    else {
                        System.out.println("You have entered wrong pin.");
                        System.out.println("Transaction failed!!");
                    }

                    break;
                }catch (NumberFormatException e){
                    System.out.println("Invalid Inpout!!");
                }
            }
        }catch (IOException e){
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }

    }

    private void deposit(BufferedReader br){
        while (true) {
            try {
                double amount;
                System.out.print("Amount: ");
                amount = Double.parseDouble(br.readLine());

                if (amount < 0){
                    throw new IllegalArgumentException("Negative amount");
                }

                account.deposit(amount);
                System.out.println("Transaction Successful!!");
                checkBalance();
                return;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid Input!!");
            }catch (IllegalArgumentException e){
                System.out.println("Please enter positive amount.");
            }
        }
    }

    private void withdraw(BufferedReader br){
        while (true) {
            try {
                double amount;
                System.out.print("Amount: ");
                amount = Double.parseDouble(br.readLine());

                if (amount < 0){
                    throw new IllegalArgumentException("Negative amount");
                }
                if (amount > account.getBankBalance()){
                    System.out.println("Insufficient bank balance.");
                    System.out.println("Transaction failed!!");
                    checkBalance();
                }
                else {
                    account.withdraw(amount);
                    System.out.println("Transaction Successful!!");
                    checkBalance();
                }


                return;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid Input!!");
            }catch (IllegalArgumentException e){
                System.out.println("Please enter positive amount.");
            }
        }
    }

    private void checkBalance(){
        System.out.printf("Available balance: %.3f\n\n",account.getBankBalance());
    }

    private void showTransactionHistory(){
        ArrayList<BankAccount.Transactions> transactions = account.getTransactionHistory();

        System.out.println("Prv Balance\t\tDeposit/withdrawal\t\tBalance");
        for (BankAccount.Transactions t : transactions){
            System.out.printf("%.3f\t\t%.3f\t\t\t\t%.3f\n", t.getBeforeTransaction() , t.getTransaction() , t.getAfterTransaction());
        }
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) throws PinOutOfRangeException {
        BankAccount savings = new BankAccount(10000 , 1234);

        ATM_machine a1 = new ATM_machine(savings);
        a1.handleUserInput();
    }
}

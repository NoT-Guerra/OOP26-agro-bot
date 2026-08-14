package it.unibo.agrobot.model;

// gestisce il saldo disponibile per acquisti o vendite nel gioco.
public class Wallet {

    private double balance;

    // crea un nuovo portafoglio con saldo iniziale 0.
    public Wallet() {
        this.balance = 0.0;
    }

    /**
     * crea un nuovo portafoglio con un saldo iniziale specificato.
     *
     * @param initialBalance saldo di partenza
     */
    public Wallet(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Il saldo iniziale non può essere negativo.");
        }
        this.balance = initialBalance;
    }

    /**
     * ritorna il saldo attuale.
     *
     * @return il saldo corrente
     */
    public synchronized double getBalance() {
        return this.balance;
    }

    /**
     * aggiunge una determinata somma al saldo.
     *
     * @param amount somma da aggiungere
     */
    public synchronized void addFunds(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Impossibile aggiungere un ammontare negativo.");
        }
        this.balance += amount;
    }

    /**
     * deduce una certa somma dal saldo, verificando prima la disponibilità.
     *
     * @param amount somma da dedurre
     * @return true se la somma è stata dedotta con successo, false se i fondi
     * sono insufficienti
     */
    public synchronized boolean deductFunds(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Impossibile dedurre un ammontare negativo.");
        }
        if (hasEnoughFunds(amount)) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * controlla se i fondi disponibili sono sufficienti per una determinata
     * spesa.
     *
     * @param amount somma richiesta
     * @return true se il saldo è uguale o superiore all'ammontare, false
     * altrimenti
     */
    public synchronized boolean hasEnoughFunds(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("L'ammontare da verificare non può essere negativo.");
        }
        return this.balance >= amount;
    }

    /**
     * imposta direttamente il saldo.
     *
     * @param balance nuovo saldo
     */
    public synchronized void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Il saldo non può essere negativo.");
        }
        this.balance = balance;
    }
}

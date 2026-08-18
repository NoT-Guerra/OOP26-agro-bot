package it.unibo.agrobot.model;

/**
 * implementazione dell'interfaccia Drone Mantiene lo stato interno del drone
 * come le coordinate e ne gestisce l'aggiornamento durante la simulazione
 */
public class DroneImpl implements Drone {

    private final Position position;
    private final Battery battery;
    private final WaterTank waterTank;
    private final Inventory inventory;
    private final Wallet wallet;

    //variabili per il movimento fluido
    private boolean moving;
    private Position targetPosition;
    private static final double SPEED = 2.0;

    //costante per il consumo base del movimento
    private static final double MOVEMENT_ENERGY_COST = 1.0;
    //costante per il consumo delle azioni agricole
    private static final double ACTION_ENERGY_COST = 5.0;

    private final Position startPosition;

    public DroneImpl(Position position) {
        this.startPosition = new Position(position.getX(), position.getY());
        this.position = position;
        this.battery = new Battery(100.0); //la batteria parte sempre carica al max
        this.waterTank = new WaterTank(50.0); //serbatoio parte vuoto, capienza 50
        this.inventory = new Inventory(3); //inventario con 3 slot iniziali
        this.wallet = new Wallet(0.0); //portafoglio con saldo iniziale 0
        this.moving = false;
    }

    @Override
    public synchronized void reset() {
        this.position.setX(this.startPosition.getX());
        this.position.setY(this.startPosition.getY());
        this.battery.reset();
        this.waterTank.empty();
        this.inventory.clear();
        this.wallet.setBalance(0.0);
        this.moving = false;
        this.targetPosition = null;
    }

    @Override
    public synchronized double getBatteryLevel() {
        return this.battery.getLevel();
    }

    @Override
    public synchronized double getWaterLevel() {
        return this.waterTank.getLevel();
    }

    @Override
    public synchronized Position getPosition() {
        return new Position(this.position.getX(), this.position.getY());
    }

    @Override
    public synchronized boolean isMoving() {
        return this.moving;
    }

    @Override
    public synchronized boolean move(Direction dir) {
        if (!this.moving && !this.battery.isDead()) {
            this.battery.decrease(MOVEMENT_ENERGY_COST);
            this.moving = true;

            double targetX = this.position.getX();
            double targetY = this.position.getY();

            switch (dir) {
                case UP -> targetY -= 1.0;
                case DOWN -> targetY += 1.0;
                case LEFT -> targetX -= 1.0;
                case RIGHT -> targetX += 1.0;
            }
            this.targetPosition = new Position(targetX, targetY);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void updateState(double deltaTime) {
        if (this.moving) {
            double distanceToTravel = SPEED * deltaTime;

            double currentX = this.position.getX();
            double currentY = this.position.getY();
            double targetX = this.targetPosition.getX();
            double targetY = this.targetPosition.getY();

            //calcolo distanza verso il target
            double dx = targetX - currentX;
            double dy = targetY - currentY;
            double distanceToTarget = Math.sqrt(dx * dx + dy * dy);

            if (distanceToTravel >= distanceToTarget) {
                //raggiunto o superato il target: allineamento alla griglia
                this.position.setX(targetX);
                this.position.setY(targetY);
                this.moving = false;
            } else {
                //Movimento intermedio
                double dirX = dx / distanceToTarget;
                double dirY = dy / distanceToTarget;
                this.position.setX(currentX + (dirX * distanceToTravel));
                this.position.setY(currentY + (dirY * distanceToTravel));
            }
        }
    }

    @Override
    public synchronized void plow() {
        if (!this.battery.isDead()) {
            this.battery.decrease(ACTION_ENERGY_COST);
            //todo
            //serisci la logica di interazione col terreno 
        }
    }

    @Override
    public synchronized void harvest() {
        if (!this.battery.isDead()) {
            this.battery.decrease(ACTION_ENERGY_COST);
            // La logica effettiva di raccolta risorsa verrà inserita qui
        }
    }

    @Override
    public synchronized boolean irrigate() {
        double IRRIGATION_WATER_COST = 10.0;
        if (!this.battery.isDead() && this.waterTank.getLevel() >= IRRIGATION_WATER_COST) {
            this.battery.decrease(ACTION_ENERGY_COST);
            this.waterTank.remove(IRRIGATION_WATER_COST);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean isDead() {
        return this.battery.isDead();
    }

    @Override
    public synchronized void rechargeAtHangar() {
        this.battery.recharge();
    }

    @Override
    public synchronized void rechargeWaterAtWell() {
        this.waterTank.fill();
    }

    @Override
    public synchronized Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public synchronized Wallet getWallet() {
        return this.wallet;
    }

    @Override
    public synchronized void upgradeBatteryMaxCapacity(double amount) {
        this.battery.increaseMaxCapacity(amount);
    }

    @Override
    public synchronized double getMaxBatteryCapacity() {
        return this.battery.getMaxCapacity();
    }
}

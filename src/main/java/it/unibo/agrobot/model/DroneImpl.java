package it.unibo.agrobot.model;

/**
 * implementazione dell'interfaccia Drone
 * Mantiene lo stato interno del drone come le coordinate e ne gestisce
 * l'aggiornamento durante la simulazione
 */
public class DroneImpl implements Drone {

    private Position position;
    private Battery battery;
    private WaterTank waterTank;
    private Inventory inventory;
    
    //variabili per il movimento fluido
    private boolean moving;
    private Position targetPosition;
    private static final double SPEED = 2.0;
    
    //costante per il consumo base del movimento
    private static final double MOVEMENT_ENERGY_COST = 2.0;
    //costante per il consumo delle azioni agricole
    private static final double ACTION_ENERGY_COST = 5.0;

    public DroneImpl(Position position) {
        this.position = position;
        this.battery = new Battery(100.0); //la batteria parte sempre carica al max
        this.waterTank = new WaterTank(50.0); //serbatoio parte vuoto, capienza 50
        this.inventory = new Inventory(3); //inventario con 3 slot iniziali
        this.moving = false;
    }

    @Override
    public double getBatteryLevel() {
        return this.battery.getLevel();
    }

    @Override
    public double getWaterLevel() {
        return this.waterTank.getLevel();
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public boolean isMoving() {
        return this.moving;
    }

    @Override
    public boolean move(Direction dir) {
        if (!this.moving && !this.battery.isDead()) {
            this.battery.decrease(MOVEMENT_ENERGY_COST);
            this.moving = true;
            
            double targetX = this.position.getX();
            double targetY = this.position.getY();
            
            switch (dir) {
                case UP: targetY += 1.0; break;
                case DOWN: targetY -= 1.0; break;
                case LEFT: targetX -= 1.0; break;
                case RIGHT: targetX += 1.0; break;
            }
            this.targetPosition = new Position(targetX, targetY);
            return true;
        }
        return false;
    }

    @Override
    public void updateState(double deltaTime) {
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
    public void plow() {
        if (!this.battery.isDead()) {
            this.battery.decrease(ACTION_ENERGY_COST);
            //todo
            //inserisci la logica di interazione col terreno 
        }
    }

    @Override
    public void harvest() {
        if (!this.battery.isDead()) {
            this.battery.decrease(ACTION_ENERGY_COST);
            // La logica effettiva di raccolta risorsa verrà inserita qui
        }
    }

    @Override
    public boolean irrigate() {
        double IRRIGATION_WATER_COST = 10.0;
        if (!this.battery.isDead() && this.waterTank.getLevel() >= IRRIGATION_WATER_COST) {
            this.battery.decrease(ACTION_ENERGY_COST);
            this.waterTank.remove(IRRIGATION_WATER_COST);
            return true;
        }
        return false;
    }

    @Override
    public boolean isDead() {
        return this.battery.isDead();
    }

    @Override
    public void rechargeAtHangar() {
        this.battery.recharge();
    }

    @Override
    public void rechargeWaterAtLake() {
        this.waterTank.fill();
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}

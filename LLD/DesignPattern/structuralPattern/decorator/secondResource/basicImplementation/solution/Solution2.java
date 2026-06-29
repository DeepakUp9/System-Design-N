public class Solution2 {


abstract class Coffee {
    public abstract int getPrice();
    public abstract String getDescription();
}

// ===== Base Coffee =====
class Bru extends Coffee {
    public int getPrice() {
        return 10;
    }

    public String getDescription() {
        return "Bru";
    }
}

class NesCafe extends Coffee {
    public int getPrice() {
        return 20;
    }

    public String getDescription() {
        return "NesCafe";
    }
}

// ===== Base Decorator =====
abstract class BaseDecorator extends Coffee {
    protected Coffee coffee;

    public BaseDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// ===== Milk ===========
abstract class Milk extends BaseDecorator {
    public Milk(Coffee coffee) {
        super(coffee);
    }
}

class AlmondMilk extends Milk {
    public AlmondMilk(Coffee coffee) {
        super(coffee);
    }

    public int getPrice() {
        return coffee.getPrice() + 40;
    }

    public String getDescription() {
        return coffee.getDescription() + ", Almond Milk";
    }
}

// ===== Sugar =============
abstract class Sugar extends BaseDecorator {
    public Sugar(Coffee coffee) {
        super(coffee);
    }
}

class BrownSugar extends Sugar {
    public BrownSugar(Coffee coffee) {
        super(coffee);
    }

    public int getPrice() {
        return coffee.getPrice() + 30;
    }

    public String getDescription() {
        return coffee.getDescription() + ", Brown Sugar";
    }
}

// ===== Main =============
class Main {
    public static void main(String[] args) {
        Coffee coffee = new NesCafe();

        coffee = new AlmondMilk(coffee);
        coffee = new BrownSugar(coffee);

        System.out.println(coffee.getPrice());        // 20 + 40 + 30 = 90
        System.out.println(coffee.getDescription());  // NesCafe, Almond Milk, Brown Sugar
    }
}
}

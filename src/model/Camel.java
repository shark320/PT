package model;

import java.util.Random;

/**
 * Represents the exact camel
 *
 * @author vpavlov
 */
public class Camel {

    /**
     * Global camel count
     */
    private static int camelCount = 0;

    /**
     * Camel id
     */
    private final int id = camelCount++;

    /**
     * Camel type {type}
     */
    private final CamelType type;

    /**
     * Camel speed {v}
     */
    private final double speed;

    /**
     * Camel distance {d}
     */
    private final double distance;

    /**
     * Constructor
     * @param type - type of Camel
     */
    public Camel(CamelType type) {
        Random rand = new Random();
        this.type = type;

        //speed random generating (Continuous uniform distribution)
        this.speed = type.getMinSpeed() + rand.nextDouble() * (type.getMaxSpeed() - type.getMinSpeed());

        //distance random generating (Normal distribution)
        double deviation = (type.getMaxSpeed() - type.getMinSpeed())/4;
        double mean = (type.getMaxSpeed() + type.getMinSpeed())/2;
        this.distance = rand.nextDouble() * deviation + mean;
    }

    @Override
    public String toString() {
        return "<CAMEL>[id=" + id +", type="+ this.type.getType() + ", v=" + speed + ", d=" + distance + "]";
    }
}

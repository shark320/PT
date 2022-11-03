package com.vpavlov.simulation.helpers;

import com.vpavlov.simulation.model.Warehouse;

/**
 * Record using for warehouses supply queue
 *
 * @param warehouse warehouse to supply
 * @author vpavlov
 */
public record WarehouseSupply(Warehouse warehouse) implements Comparable<WarehouseSupply> {

    /**
     * Getting the warehouse next supply time
     *
     * @return the warehouse next supply time
     */
    public double getNextSupplyTime() {
        return warehouse.getNextSupply();
    }

    @Override
    public int compareTo(WarehouseSupply o) {
        return Double.compare(this.warehouse.getNextSupply(), o.warehouse.getNextSupply());
    }
}

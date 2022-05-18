package com.amazon.ata.strategy;

import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CarbonCostStrategy implements CostStrategy {
    private final Map<Material, BigDecimal> carbonCostPerGram;

    public CarbonCostStrategy() {
        carbonCostPerGram = new HashMap<>();
        carbonCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(.017));
        carbonCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(.012));
    }

    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal carbonCost = this.carbonCostPerGram.get(packaging.getMaterial()).multiply(packaging.getMass());

        return new ShipmentCost(shipmentOption, carbonCost);
    }
}

package com.amazon.ata.strategy;

import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;

public class WeightedCostStrategy implements CostStrategy {
    MonetaryCostStrategy monetaryCostStrategy;
    CarbonCostStrategy carbonCostStrategy;

    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        this.monetaryCostStrategy = monetaryCostStrategy;
        this.carbonCostStrategy = carbonCostStrategy;
    }

    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        ShipmentCost monetaryCostStrategyCost = monetaryCostStrategy.getCost(shipmentOption);
        ShipmentCost carbonCostStrategyCost = carbonCostStrategy.getCost(shipmentOption);
        BigDecimal weightedCost = (monetaryCostStrategyCost.getCost().multiply(BigDecimal.valueOf(.8))).add(
                carbonCostStrategyCost.getCost().multiply(BigDecimal.valueOf(.2)));
        return new ShipmentCost(shipmentOption, weightedCost);
    }
}

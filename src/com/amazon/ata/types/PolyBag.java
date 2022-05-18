package com.amazon.ata.types;


import java.math.BigDecimal;
import java.util.Objects;

public class PolyBag extends Packaging {
    private BigDecimal volume;



    public PolyBag (Material material, BigDecimal volume) {
        super(material);
        this.volume = volume;
    }

    @Override
    public boolean canFitItem(Item item) {
        return this.volume.compareTo(item.getHeight().multiply(item.getLength()).multiply(item.getWidth())) > 0;
    }

    @Override
    public BigDecimal getMass() {
        double part = Math.ceil(Math.sqrt(volume.doubleValue() * 0.6));
        BigDecimal result = BigDecimal.valueOf(part);
     return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterial(), getMass());
    }
}


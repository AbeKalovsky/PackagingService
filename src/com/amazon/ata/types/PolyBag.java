package com.amazon.ata.types;

import com.amazonaws.services.dynamodbv2.xspec.B;

import java.math.BigDecimal;

public class PolyBag extends Packaging {
    private BigDecimal volume;
    /**
     * Instantiates a new Packaging object.
     *
     * @param volume- the volume of the package
     */
    public PolyBag(BigDecimal volume) {
        super(Material.LAMINATED_PLASTIC);
        this.volume = volume;
    }

    @Override
    public boolean canFitItem(Item item) {
        return this.volume.compareTo(item.getHeight().multiply(item.getLength()).multiply(item.getWidth())) >
                0;
    }

    @Override
    public BigDecimal getMass() {
        double part = Math.ceil(Math.sqrt(volume.doubleValue() * 0.6));
        BigDecimal result = BigDecimal.valueOf(part);
     return result;
    }
}


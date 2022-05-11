package com.amazon.ata.types;

import java.math.BigDecimal;

public class Box extends Packaging {

    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    /**
     * Instantiates a new Packaging object.
     *
     * @param corrugate
     * @param length   - the length of the package
     * @param width    - the width of the package
     * @param height   - the height of the package
     */
    public Box(Material corrugate, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(corrugate);
        this.length = length;
        this.width = width;
        this.height = height;
    }
    @Override
    public boolean canFitItem(Item item) {
        return this.length.compareTo(item.getLength()) > length.intValue() &&
                this.width.compareTo(item.getWidth()) > width.intValue() &&
                this.height.compareTo(item.getHeight()) > height.intValue();
    }
    @Override
    public BigDecimal getMass() {
        BigDecimal two = BigDecimal.valueOf(2);

        // For simplicity, we ignore overlapping flaps
        BigDecimal endsArea = length.multiply(width).multiply(two);
        BigDecimal shortSidesArea = length.multiply(height).multiply(two);
        BigDecimal longSidesArea = width.multiply(height).multiply(two);

        return endsArea.add(shortSidesArea).add(longSidesArea);
    }
}

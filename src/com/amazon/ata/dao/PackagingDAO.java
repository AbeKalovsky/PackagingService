package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FcPackagingOption;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.util.*;
// test
/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    /**
     * A list of fulfillment centers with a packaging options they provide.
     */

    private Map<FulfillmentCenter, Set<FcPackagingOption>> fcPackagingOptions;

    /**
     * Instantiates a PackagingDAO object.
     *
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {
        List<FcPackagingOption> object = new ArrayList<>(datastore.getFcPackagingOptions());

        this.fcPackagingOptions = new HashMap<>();
        for (FcPackagingOption data : object) {
            if (fcPackagingOptions.containsKey(data.getFulfillmentCenter())) {
                fcPackagingOptions.get(data.getFulfillmentCenter()).add(data);
            } else {
                Set<FcPackagingOption> set = new HashSet<>();
                set.add(data);
                fcPackagingOptions.put(data.getFulfillmentCenter(), set);
            }
        }
    }

    /**
     * Returns the packaging options available for a given item at the specified fulfillment center. The API
     * used to call this method handles null inputs, so we don't have to.
     *
     * @param item              the item to pack
     * @param fulfillmentCenter fulfillment center to fulfill the order from
     * @return the shipping options available for that item; this can never be empty, because if there is no
     * acceptable option an exception will be thrown
     * @throws UnknownFulfillmentCenterException if the fulfillmentCenter is not in the fcPackagingOptions list
     * @throws NoPackagingFitsItemException      if the item doesn't fit in any packaging at the FC
     */
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        // Check all FcPackagingOptions for a suitable Packaging in the given FulfillmentCenter
        List<ShipmentOption> result = new ArrayList<>();
//        boolean fcFound = false;
        if (fcPackagingOptions.get(fulfillmentCenter) == null) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }
            for (FcPackagingOption option : fcPackagingOptions.get(fulfillmentCenter)) {
                Packaging packaging = option.getPackaging();
                if (packaging.canFitItem(item)) {
                    result.add(ShipmentOption.builder()
                            .withItem(item)
                            .withPackaging(packaging)
                            .withFulfillmentCenter(fulfillmentCenter)
                            .build());
                }


            }

            if (result.isEmpty()) {
                throw new NoPackagingFitsItemException(
                        String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
            }



        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PackagingDAO that = (PackagingDAO) o;
        return Objects.equals(fcPackagingOptions, that.fcPackagingOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fcPackagingOptions);
    }
}




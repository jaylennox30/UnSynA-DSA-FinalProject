
package service;

import java.util.ArrayList;

import model.Facility;

public class FacilityManager {

    private ArrayList<Facility> facilities;

    public FacilityManager() {
        facilities = new ArrayList<>();
    }

    // Add facility
    public boolean addFacility(Facility facility) {

        if (facility == null || findFacility(facility.getFacilityId()) != null) {
            return false;
        }

        facilities.add(facility);
        return true;
    }

    // Find facility
    public Facility findFacility(String facilityId) {

        for (Facility facility : facilities) {

            if (facility.getFacilityId().equalsIgnoreCase(facilityId)) {
                return facility;
            }
        }

        return null;
    }

    // Update facility status
    public boolean updateFacilityStatus(
            String facilityId, boolean active) {

        Facility facility = findFacility(facilityId);

        if (facility == null) {
            return false;
        }

        facility.setActive(active);
        return true;
    }

    // Remove facility
    public boolean removeFacility(String facilityId) {

        Facility facility = findFacility(facilityId);

        if (facility == null) {
            return false;
        }

        facilities.remove(facility);
        return true;
    }

    // Display facilities
    public void displayFacilities() {

        for (Facility facility : facilities) {
            facility.displayFacility();
        }
    }

    public ArrayList<Facility> getFacilities() {
        return new ArrayList<>(facilities);
    }
}

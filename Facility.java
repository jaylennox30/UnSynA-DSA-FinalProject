
package model;

public class Facility {

    private String facilityId;
    private String facilityName;
    private FacilityType facilityType;
    private boolean active;

    public Facility(String facilityId, String facilityName,
                    FacilityType facilityType) {

        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.active = true;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void displayFacility() {
        System.out.println(
            facilityId + " - " + facilityName +
            " (" + facilityType + ")"
        );
    }
}

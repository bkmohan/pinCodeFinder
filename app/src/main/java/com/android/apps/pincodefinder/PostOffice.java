package com.android.apps.pincodefinder;

/**
 * A simple java object to create individual Post Office instances.
 */
public class PostOffice {

    private String name;
    private String pincode;
    private String branchType;
    private String deliveryStatus;
    private String circle;
    private String division;
    private String region;
    private String district;
    private String state;

    private PostOffice() {
    }

    public String getName() {
        return name;
    }

    public String getPincode() {
        return pincode;
    }

    public String getBranchType() {
        return branchType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getCircle() {
        return circle;
    }

    public String getDivision() {
        return division;
    }

    public String getRegion() {
        return region;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public String getAddress() {
        return this.district + ", " + this.state;
    }

    /**
     * This is a Builder pattern which aims to separate the construction of PostOffice object from its representation so
     * that the same construction process can create different representations.
     * It is used to construct a complex object step by step and the final step will return the object.
     */
    public static class PostOfficeBuilder {
        private String name;
        private String pincode;
        private String branchType;
        private String deliveryStatus;
        private String circle;
        private String division;
        private String region;
        private String district;
        private String state;

        public PostOfficeBuilder(String officeName) {
            this.name = officeName;
        }

        public PostOfficeBuilder addPincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public PostOfficeBuilder addBranchType(String branchType) {
            this.branchType = branchType;
            return this;
        }

        public PostOfficeBuilder addDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
            return this;
        }

        public PostOfficeBuilder addCircle(String circle) {
            this.circle = circle;
            return this;
        }

        public PostOfficeBuilder addDivision(String division) {
            this.division = division;
            return this;
        }

        public PostOfficeBuilder addRegion(String region) {
            this.region = region;
            return this;
        }

        public PostOfficeBuilder addDistrict(String district) {
            this.district = district;
            return this;
        }

        public PostOfficeBuilder addState(String state) {
            this.state = state;
            return this;
        }

        public PostOffice build() {
            PostOffice office = new PostOffice();
            office.name = this.name;
            office.pincode = this.pincode;
            office.branchType = this.branchType;
            office.deliveryStatus = this.deliveryStatus;
            office.circle = this.circle;
            office.division = this.division;
            office.region = this.region;
            office.district = this.district;
            office.state = this.state;

            return office;
        }
    }

}

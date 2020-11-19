package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferenceDetails
{
        @SerializedName("ReferenceName")
        @Expose
        private String referenceName;
        @SerializedName("Relationship")
        @Expose
        private String relationship;
        @SerializedName("RefMobileNumber")
        @Expose
        private String refMobileNumber;

        public String getReferenceName() {
            return referenceName;
        }

        public void setReferenceName(String referenceName) {
            this.referenceName = referenceName;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getRefMobileNumber() {
            return refMobileNumber;
        }

        public void setRefMobileNumber(String refMobileNumber) {
            this.refMobileNumber = refMobileNumber;
        }

    }


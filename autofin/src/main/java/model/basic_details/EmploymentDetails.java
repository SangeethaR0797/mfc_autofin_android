package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmploymentDetails {


        @SerializedName("EmploymentType")
        @Expose
        private String employmentType;
        @SerializedName("CompanyName")
        @Expose
        private String companyName;
        @SerializedName("CompanyJoiningDate")
        @Expose
        private String companyJoiningDate;
        @SerializedName("TotalWorkExperience")
        @Expose
        private String totalWorkExperience;
        @SerializedName("SalaryMode")
        @Expose
        private String salaryMode;
        @SerializedName("SalaryAccount")
        @Expose
        private String salaryAccount;
        @SerializedName("EmploymentRole")
        @Expose
        private String employmentRole;
        @SerializedName("Profession")
        @Expose
        private String profession;
        @SerializedName("BusinessStartDate")
        @Expose
        private String businessStartDate;
        @SerializedName("LastYearTurnOver")
        @Expose
        private String lastYearTurnOver;
        @SerializedName("LastYearDepreciation")
        @Expose
        private String lastYearDepreciation;
        @SerializedName("IsLastestItraudited")
        @Expose
        private Boolean isLastestItraudited;
        @SerializedName("IndustryType")
        @Expose
        private String industryType;

        public String getEmploymentType() {
            return employmentType;
        }

        public void setEmploymentType(String employmentType) {
            this.employmentType = employmentType;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyJoiningDate() {
            return companyJoiningDate;
        }

        public void setCompanyJoiningDate(String companyJoiningDate) {
            this.companyJoiningDate = companyJoiningDate;
        }

        public String getTotalWorkExperience() {
            return totalWorkExperience;
        }

        public void setTotalWorkExperience(String totalWorkExperience) {
            this.totalWorkExperience = totalWorkExperience;
        }

        public String getSalaryMode() {
            return salaryMode;
        }

        public void setSalaryMode(String salaryMode) {
            this.salaryMode = salaryMode;
        }

        public String getSalaryAccount() {
            return salaryAccount;
        }

        public void setSalaryAccount(String salaryAccount) {
            this.salaryAccount = salaryAccount;
        }

        public String getEmploymentRole() {
            return employmentRole;
        }

        public void setEmploymentRole(String employmentRole) {
            this.employmentRole = employmentRole;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getBusinessStartDate() {
            return businessStartDate;
        }

        public void setBusinessStartDate(String businessStartDate) {
            this.businessStartDate = businessStartDate;
        }

        public String getLastYearTurnOver() {
            return lastYearTurnOver;
        }

        public void setLastYearTurnOver(String lastYearTurnOver) {
            this.lastYearTurnOver = lastYearTurnOver;
        }

        public String getLastYearDepreciation() {
            return lastYearDepreciation;
        }

        public void setLastYearDepreciation(String lastYearDepreciation) {
            this.lastYearDepreciation = lastYearDepreciation;
        }

        public Boolean getIsLastestItraudited() {
            return isLastestItraudited;
        }

        public void setIsLastestItraudited(Boolean isLastestItraudited) {
            this.isLastestItraudited = isLastestItraudited;
        }

        public String getIndustryType() {
            return industryType;
        }

        public void setIndustryType(String industryType) {
            this.industryType = industryType;
        }


}

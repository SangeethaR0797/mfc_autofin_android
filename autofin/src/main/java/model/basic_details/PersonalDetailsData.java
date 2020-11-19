package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalDetailsData {

        @SerializedName("BirthDate")
        @Expose
        private String birthDate;
        @SerializedName("SalaryPerMonth")
        @Expose
        private String salaryPerMonth;
        @SerializedName("TotalEMIPaid")
        @Expose
        private String totalEMIPaid;
        @SerializedName("PANNumber")
        @Expose
        private String pANNumber;
        @SerializedName("SavingsAccount")
        @Expose
        private String savingsAccount;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Education")
        @Expose
        private String education;

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        public String getSalaryPerMonth() {
            return salaryPerMonth;
        }

        public void setSalaryPerMonth(String salaryPerMonth) {
            this.salaryPerMonth = salaryPerMonth;
        }

        public String getTotalEMIPaid() {
            return totalEMIPaid;
        }

        public void setTotalEMIPaid(String totalEMIPaid) {
            this.totalEMIPaid = totalEMIPaid;
        }

        public String getPANNumber() {
            return pANNumber;
        }

        public void setPANNumber(String pANNumber) {
            this.pANNumber = pANNumber;
        }

        public String getSavingsAccount() {
            return savingsAccount;
        }

        public void setSavingsAccount(String savingsAccount) {
            this.savingsAccount = savingsAccount;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

    }



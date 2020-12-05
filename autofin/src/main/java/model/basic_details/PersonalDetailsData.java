package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class PersonalDetailsData {

    @SerializedName("birthDate")
    @Expose
    private String birthDate;
    @SerializedName("salaryPerMonth")
    @Expose
    private double salaryPerMonth;
    @SerializedName("totalEMIPaid")
    @Expose
    private double totalEMIPaid;
    @SerializedName("panNumber")
    @Expose
    private String panNumber;
    @SerializedName("savingsAccount")
    @Expose
    private String savingsAccount;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("education")
    @Expose
    private String education;

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public double getSalaryPerMonth() {
        return salaryPerMonth;
    }

    public void setSalaryPerMonth(double salaryPerMonth) {
        this.salaryPerMonth = salaryPerMonth;
    }

    public double getTotalEMIPaid() {
        return totalEMIPaid;
    }

    public void setTotalEMIPaid(double totalEMIPaid) {
        this.totalEMIPaid = totalEMIPaid;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
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




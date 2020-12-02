package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmploymentDetails {


    @SerializedName("employmentType")
    @Expose
    private String employmentType;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("companyJoiningDate")
    @Expose
    private String companyJoiningDate;
    @SerializedName("totalWorkExperience")
    @Expose
    private String totalWorkExperience;
    @SerializedName("salaryMode")
    @Expose
    private String salaryMode;
    @SerializedName("salaryAccount")
    @Expose
    private String salaryAccount;
    @SerializedName("employmentRole")
    @Expose
    private String employmentRole;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("businessStartDate")
    @Expose
    private String businessStartDate;
    @SerializedName("lastYearTurnOver")
    @Expose
    private String lastYearTurnOver;
    @SerializedName("lastYearDepreciation")
    @Expose
    private String lastYearDepreciation;
    @SerializedName("isLastestItraudited")
    @Expose
    private Boolean isLastestItraudited;
    @SerializedName("industryType")
    @Expose
    private String industryType;
    @SerializedName("ProfessionalQualification")
    @Expose
    private String professionalQualification;
    @SerializedName("lastYearProfit")
    @Expose
    private Double lastYearProfit;

    public Double getLastYearProfit() {
        return lastYearProfit;
    }

    public void setLastYearProfit(Double lastYearProfit) {
        this.lastYearProfit = lastYearProfit;
    }

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

    public String getProfessionalQualification() {
        return professionalQualification;
    }

    public void setProfessionalQualification(String professionalQualification) {
        this.professionalQualification = professionalQualification;
    }


}

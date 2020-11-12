package model.bank_models;

public class SelectBankData
{


    private String bankStatus="",
            loanAmount="",
            rateOfInterest="",
            emiAmount="",
            tenure="";

    public SelectBankData(String bankStatus, String loanAmount, String rateOfInterest, String emiAmount, String tenure) {
        this.bankStatus = bankStatus;
        this.loanAmount = loanAmount;
        this.rateOfInterest = rateOfInterest;
        this.emiAmount = emiAmount;
        this.tenure = tenure;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(String rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public String getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(String emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }


}

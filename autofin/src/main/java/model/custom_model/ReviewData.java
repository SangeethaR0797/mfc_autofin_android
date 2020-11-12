package model.custom_model;

public class ReviewData
{
    public ReviewData(String strTitleLbl, String strFieldValue) {
        this.strTitleLbl = strTitleLbl;
        this.strFieldValue = strFieldValue;
    }

    public String getStrTitleLbl() {
        return strTitleLbl;
    }

    public void setStrTitleLbl(String strTitleLbl) {
        this.strTitleLbl = strTitleLbl;
    }

    public String getStrFieldValue() {
        return strFieldValue;
    }

    public void setStrFieldValue(String strFieldValue) {
        this.strFieldValue = strFieldValue;
    }

    private String strTitleLbl="",
    strFieldValue="";

}

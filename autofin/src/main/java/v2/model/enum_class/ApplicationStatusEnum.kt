package v2.model.enum_class

enum class ApplicationStatusEnum(val value: String) {
    Registered("Registered"),
    KYC_Done("KYC Done"),
    Employment_Details_Submitted("Employment Details Submitted"),
    Lender_Selected("Lender Selected"),
    Bank_Form_Filled("Banks Form Filled"),
    Document_Uploaded("Document Uploaded"),
    Submitted_To_Bank("Submitted to Bank"),
    Approved("Approved"),
    Rejected("Rejected"),
    Disbursed("Disbursed"),
    Closed("Closed")
}
package v2.model.request

data class AddEmploymentDetailsRequest(
        var Data: AddEmploymentData? = null,
        var UserId: String? = null,
        var UserType: String? = null
)

data class AddEmploymentData(
        var CustomerId: Int? = null,
        var employmentDetails: AddEmploymentEmploymentDetails? = null,
        var personalDetails: AddEmploymentPersonalDetails? = null
)

data class AddEmploymentEmploymentDetails(
        var CurrentCompanyExpMoreThanOne: Boolean? = null,
        var EmploymentType: String? = null,
        var NetAnualIncome: Int? = null,
        var PrimaryAccount: String? = null,
        var SalaryAccount: String? = null,
        var TotalWorkExperience: String? = null
)

data class AddEmploymentPersonalDetails(
        var BirthDate: String? = null
)
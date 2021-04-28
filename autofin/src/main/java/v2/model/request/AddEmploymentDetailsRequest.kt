package v2.model.request

data class AddEmploymentDetailsRequest(
        var Data: AddEmploymentData? = null,
        var UserId: String? = null,
        var UserType: String? = null
)

data class AddEmploymentData(
        var CustomerId: Int? = null,
        var employmentDetails: EmploymentDetails? = null,
        var personalDetails: PersonalDetails? = null
)

data class EmploymentDetails(
        var CurrentCompanyExpMoreThanOne: Boolean? = null,
        var EmploymentType: String? = null,
        var NetAnualIncome: Int? = null,
        var PrimaryAccount: String? = null,
        var SalaryAccount: String? = null,
        var TotalWorkExperience: String? = null
)

data class PersonalDetails(
        var BirthDate: String? = null
)
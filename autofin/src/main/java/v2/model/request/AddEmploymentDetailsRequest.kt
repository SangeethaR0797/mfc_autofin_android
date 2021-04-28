package v2.model.request

data class AddEmploymentDetailsRequest(
    var Data: AddEmploymentData?,
    var UserId: String?,
    var UserType: String?
)

data class AddEmploymentData(
    var CustomerId: Int?,
    var employmentDetails: EmploymentDetails?,
    var personalDetails: PersonalDetails?
)

data class EmploymentDetails(
    var CurrentCompanyExpMoreThanOne: Boolean?,
    var EmploymentType: String?,
    var NetAnualIncome: Int?,
    var PrimaryAccount: String?,
    var SalaryAccount: String?,
    var TotalWorkExperience: String?
)

data class PersonalDetails(
    var BirthDate: String?
)
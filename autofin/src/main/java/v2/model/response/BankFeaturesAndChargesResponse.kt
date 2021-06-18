package v2.model.response

import v2.model.dto.KeyValueDTO

data class BankFeaturesAndChargesResponse(
    var `data`: DataDetails?,
    var message: String?,
    var status: Boolean?,
    var statusCode: String?
)

data class DataDetails(
    var charges: List<Charge>?,
    var descriptions: List<Description>?,
    var features: List<Feature>?,
    var otherPartners: List<RuleEngineBankData>?
)

data class Charge(
    var header: String?,
    var subHeader: String?,
    var values: List<KeyValueDTO>?
)

data class Description(
    var header: String?,
    var subHeader: Any?,
    var values: String?
)

data class Feature(
    var header: String?,
    var subHeader: Any?,
    var values: List<String>?
)




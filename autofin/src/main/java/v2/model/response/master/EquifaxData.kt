package v2.model.response.master

data class EquifaxData(
        var address: List<Addres>? = null,
        var birthDate: List<String>? = null,
        var panNumber: List<String>? = null
)

data class Addres(
        var address: String? = null,
        var pincode: String? = null
)
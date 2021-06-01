package v2.model.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class AdditionalFields
(

        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : AdditionalFieldsData )

data class ApiDetails (

        @SerializedName("apiKey") var apiKey : String,
        @SerializedName("url") var url : String,
        @SerializedName("methodType") var methodType : String,
        @SerializedName("valueKey") var valueKey : String,
        @SerializedName("displayKey") var displayKey : String,
        @SerializedName("parameterKey") var parameterKey : String

)

data class Fields (

        @SerializedName("fieldId") var fieldId : Int,
        @SerializedName("label") var label : String,
        @SerializedName("placeHolder") var placeHolder : String,
        @SerializedName("fieldType") var fieldType : String,
        @SerializedName("valueType") var valueType : String,
        @SerializedName("isMandatory") var isMandatory : Boolean,
        @SerializedName("regexValidation") var regexValidation:String,
        @SerializedName("apiDetails") var apiDetails : ApiDetails
        )

data class Sections (

        @SerializedName("sectionId") var sectionId : Int,
        @SerializedName("sectionName") var sectionName : String,
        @SerializedName("displayName") var displayName : Boolean,
        @SerializedName("type") var type : String,
        @SerializedName("fields") var fields : List<Fields>

)

data class AdditionalFieldsData (

        @SerializedName("sections") var sections : List<Sections>

)
package v2.model.response

data class AwsS3DetailsResponse(
    var `data`: s3Data?,
    var message: String?,
    var status: Boolean?,
    var statusCode: String?
)

data class s3Data(
    var accesskey: String?,
    var bucketname: String?,
    var secretkey: String?,
    var staticfolder: String?
)
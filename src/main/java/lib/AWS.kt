package lib

import com.amazonaws.AmazonWebServiceClient
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient
import com.amazonaws.services.sqs.AmazonSQSClient
import kotlin.concurrent.fixedRateTimer


object AWS {
    val credentialsProvider = AWSCredentialsProviderChain(
            ProfileCredentialsProvider("ophan"),
            InstanceProfileCredentialsProvider()
    )

    val region = Region.getRegion(Regions.EU_WEST_1)

    inline fun <reified T : AmazonWebServiceClient> createClient(): T =
            region.createClient(T::class.java, credentialsProvider, null)

    val ec2 = createClient<AmazonEC2Client>()
    val elb = createClient<AmazonElasticLoadBalancingClient>()
    val autoscaling = createClient<AmazonAutoScalingClient>()
    val cloudwatch = createClient<AmazonCloudWatchClient>()
    val sqs = createClient<AmazonSQSClient>()


}
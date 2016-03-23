package spot

import ninja.Result
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer
import kotlin.jvm.internal.FunctionReference
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

fun doSomething(method: KFunction<Result>) {
  println(method)
    method.javaMethod

    println(method.name)

    val m2 = method as FunctionReference
    println(m2.owner.javaClass.name)

}

inline fun <reified T> doSomethingElse(noinline f: T.() -> Result) {

    val className = T::class.simpleName
    val functionName = f.javaClass.canonicalName

    println("classname = $className, function name = $functionName")

}



// what do I want?

//  - a list of all the autoscaling groups and the instances inside them (and their state)

//  - a list of all the instances that aren't inside an autoscaling group

//  - for each instance, work out their cost

//  - for each instance, find its version

//  (this needs to be done as a whole:)
//  - by applying the list of reservations to the set of running instances, find:

//     - which ones are reserved
//     - which reservations are not in use



fun main(args: Array<String>) {
    println("Hello Again!!")

    fixedRateTimer(period = TimeUnit.SECONDS.toMillis(1), daemon = false) {
        println("hello timer?")
    }

//    val result = AWS.ec2.describeInstances()
//
//    println("nextToken = ${result.nextToken}")
//
//    val instances = result.reservations.flatMap { it.instances }
//
//
//    for (instance in instances) {
//        println(instance)
//    }
//    val x = ApplicationController::index
//
//    doSomethingElse(x)

//    System.setProperty("ninja.mode", "dev")
//    NinjaJetty.main(args)

    println("done")
}



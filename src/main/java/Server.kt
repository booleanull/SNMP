import spark.Spark
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>) {
    initServer(6565)

    val controllers = listOf(SNMPController())
    controllers.forEach { it.start() }
}

private fun initServer(port: Int) {
    val sdf = SimpleDateFormat("hh:mm dd/MM/yyyy")
    val date = sdf.format(Date())
    val ip = InetAddress.getLocalHost()
    Spark.port(port)

    println("=======================================")
    println("Port: $port")
    println("IP address: ${ip.hostAddress}")
    println("Date: $date")
    println("=======================================\n")
}
import com.google.gson.Gson
import spark.Spark.post

class SNMPController {

    private val gson = Gson()

    fun start() {
        initSNMP()
    }

    private fun initSNMP() {
        post("/configuration", { req, res ->
            val map = mutableMapOf<String, String?>()
            try {
                val data = gson.fromJson(req.body(), SNMPRequest::class.java)
                data.address.forEach {
                    val client = SNMPManager("udp:${it}/161")
                    map[it] = try {
                        client.getAsString(client.oids)
                    } catch (e: Exception) {
                        null
                    }
                }
            } catch (e: Exception) {
                print(e.message)
            }
            map
        }, gson::toJson)
    }
}
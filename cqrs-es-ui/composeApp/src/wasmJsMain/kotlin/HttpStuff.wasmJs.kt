import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

actual val httpClient: HttpClient
    get() = HttpClient(Js)

actual val localHost = "localhost"
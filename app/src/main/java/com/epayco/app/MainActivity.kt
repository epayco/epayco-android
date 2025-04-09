package com.epayco.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.epayco.android.Epayco
import com.epayco.android.models.Authentication
import com.epayco.android.models.PlanUpdate
import com.epayco.android.util.EpaycoCallback
import com.epayco.app.ui.theme.AppTheme
import org.json.JSONException
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val auth = Authentication()

        auth.apiKey = "a1f2bdbe22c70b092e69e1f2cb7b7ea4"
        auth.privateKey = "9314c11e93925e91acd2bab9651849c0"
        auth.lang = "ES"
        auth.test = true

        val epayco = Epayco(auth)
 /*       val client = ClientList()
        client.setPage("6")
        client.setPerPage("6")
        epayco.getCustomerList(client, object : EpaycoCallback {
            @Throws(JSONException::class)
            override fun onSuccess(data: JSONObject) {
                Log.d("epayco", data.optString("name"))
            }

            override fun onError(error: Exception) {
                Log.d("epaycoError", error.message.toString())
            }
        })
*/
       /* val plan = Plan()

        plan.idPlan = "reactcourse_001233"
        plan.name = "Course React v02"
        plan.description = "Course react advanced v02"
        plan.amount = "30000"
        plan.currency = "COP"
        plan.interval = "month"
        plan.intervalCount = "1"
        plan.trialDays = "30"
        plan.ip = "127.0.0.1"
        plan.iva = 0.0F
        plan.ico = 0.0F
        plan.planLink = "https://ejemplo.com/plan"
        plan.greetMessage = "Gracias por preferirnos"
        plan.linkExpirationDate = "2025-03-11"
        plan.subscriptionLimit = "10"
        plan.discountValue = 5000.0F
        plan.discountPercentage = 19
        plan.firstPaymentAdditionalCost = 45700.0F
        epayco.createPlan(plan, object : EpaycoCallback {
            @Throws(JSONException::class)
            override fun onSuccess(data: JSONObject) {
                Log.d("epayco", data.toString())
            }

            override fun onError(error: java.lang.Exception) {
                Log.d("epaycoError", error.message.toString())
            }
        })*/
/*
        val plan = PlanUpdate()
        plan.name = "Course React v02 name"
        plan.description = "Course react advanced v02 description"
        plan.amount = "30000"
        plan.currency = "COP"
        plan.interval = "month"
        plan.intervalCount = "1"
        plan.trialDays = "0"
        plan.ip = "127.0.0.1"
        plan.iva = 19.0F
        plan.ico = 0.0F
        plan.afterPayment ="10"
        epayco.updatePlan("reactcourse_001233", plan, object : EpaycoCallback {
            @Throws(JSONException::class)
            override fun onSuccess(data: JSONObject) {
                Log.d("epayco", data.toString())
            }

            override fun onError(error: java.lang.Exception) {
                Log.d("epaycoError", error.message.toString())
            }
        })
*/

        epayco.getPlan("reactcourse_001233", object : EpaycoCallback {
            @Throws(JSONException::class)
            override fun onSuccess(data: JSONObject) {
                Log.d("epayco", data.toString())
            }

            override fun onError(error: Exception) {
                Log.d("epaycoError", error.message.toString())
            }
        })
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}
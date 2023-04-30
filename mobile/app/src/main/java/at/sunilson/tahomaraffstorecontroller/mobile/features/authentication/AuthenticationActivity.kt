package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.presentation.login.LoginDestination
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.TahomaRaffstoreTheme

class AuthenticationActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TahomaRaffstoreTheme {
                Scaffold {
                    LoginDestination()
                }
            }
        }
    }

}
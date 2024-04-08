package com.example.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.businesscard.ui.theme.BusinessCardTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BusinessCard("Full Name", "Title")
                }
            }
        }
    }
}

@Composable
fun MyLogo(){
    val image= painterResource(id = R.drawable.profile_large)
    Image(
        painter = image,
        contentDescription = "Logo",
        modifier= Modifier
            .width(100.dp)
            .height(100.dp),
    )
}
@Composable
fun ContactLogo(){
    Image(
        painter= painterResource(id = R.drawable.logo),
        contentDescription = null,
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
    )
}
@Composable
fun BusinessCard(name:String,title:String) {
    Box(
        modifier=Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){
            MyLogo()
            Text(
                text="$name",
                modifier = Modifier  ,
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp,
                //color = Color(0xFF3ddc84)
             
            )
            Text(
                text="$title",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        contentAlignment = Alignment.BottomCenter,
    ){
        Column(
            //horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
            ){
                ContactLogo()
                Text(
                    text="+00 (00) 000 000",
                )
            }
            Row(
            ){    
                ContactLogo()
                Text(
                    text="@Socialmediahandle"
                )

            }
            Row{
                 ContactLogo()
                 Text(
                     text="Email@domain.com"
                 )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BusinessCardTheme {
        BusinessCard("Full Name", title = "Title")
    }
}
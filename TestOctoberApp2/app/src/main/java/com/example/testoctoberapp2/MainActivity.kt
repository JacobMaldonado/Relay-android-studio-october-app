package com.example.testoctoberapp2

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.testoctoberapp2.button.Button
import com.example.testoctoberapp2.button.Theme
import com.example.testoctoberapp2.cargando.Cargando
import com.example.testoctoberapp2.icon.Icon
import com.example.testoctoberapp2.icon.Property1
import com.example.testoctoberapp2.logo.Logo
import com.example.testoctoberapp2.noframe.Idioma
import com.example.testoctoberapp2.noframe.NoFrame
import com.example.testoctoberapp2.ui.theme.TestOctoberApp2Theme
import com.google.relay.compose.BoxScopeInstance.columnWeight
import com.google.relay.compose.BoxScopeInstance.rowWeight
import com.google.relay.compose.RelayContainer
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.schedule
import com.example.testoctoberapp2.button.State as ButtonState
import com.example.testoctoberapp2.noframe.Theme as NoFrameTheme
import com.example.testoctoberapp2.cargando.Property1 as CargandoProperty

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestOctoberApp2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var currentScreen by remember {mutableStateOf("SplashScreen")}

                    if (currentScreen == "Cargando"){
                        LoadingScreen(){
                            currentScreen = "ItsOctober"
                        }
                    }else if(currentScreen == "SplashScreen"){
                        SplashScreeen(){
                            currentScreen = "Cargando"
                        }
                    }
                    else {
                        ItsOctoberScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ItsOctoberScreen(){
    var currentTheme by remember { mutableStateOf(Theme.Dark)}
    var currentLanguage by remember { mutableStateOf(Idioma.English)}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = if (currentTheme == Theme.Light) Color.White else Color.Black)
    )
    {
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 32.dp),
            horizontalArrangement = Arrangement.End
        ){
            Icon(
                modifier= Modifier
                    .size(36.dp)
                    .clickable {
                        currentTheme = if (currentTheme == Theme.Light) {
                            Theme.Dark
                        } else {
                            Theme.Light
                        }
                    },
                property1 = if (currentTheme == Theme.Light) Property1.Moon else Property1.Sun,
            )
        }
        Box(modifier= Modifier
            .fillMaxWidth()
            .weight(1f), contentAlignment = Alignment.Center){
            val cal: Calendar = Calendar.getInstance()
            val currentMonth: Int = cal.get(Calendar.MONTH) + 1
            if (currentMonth == 10){
                NoFrame(
                    modifier = Modifier.height(200.dp).width(250.dp),
                    theme= if(currentTheme == Theme.Light) NoFrameTheme.Dark else NoFrameTheme.Light,
                    idioma = Idioma.Si // Cambiar por si
                )
            }else{
                NoFrame(
                    modifier = Modifier.height(200.dp).width(250.dp),
                    theme= if(currentTheme == Theme.Light) NoFrameTheme.Dark else NoFrameTheme.Light,
                    idioma = currentLanguage
                )
            }
        }
        CustomButton(currentTheme){
            currentLanguage = getNextLanguage(currentLanguage)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CustomButton(
    currentTheme: Theme,
    callback: () -> Unit = {}
) {
    var pressedState by remember { mutableStateOf(ButtonState.Default) }
    Row(modifier = Modifier
        .height(80.dp)
        .pointerInteropFilter {
            when (it.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    pressedState = ButtonState.Pressed
                    Log.e("TAG", "CustomButton: Pressed")
                }
                android.view.MotionEvent.ACTION_UP -> {
                    pressedState = ButtonState.Default
                    Log.e("TAG", "CustomButton: Released")
                    callback()
                }
            }
            true
        }){
        Button(
            modifier = Modifier.height(80.dp),
            theme = currentTheme,
            state = pressedState,
            textLabel = "Comprobar Otra Vez"
        )
    }
}

fun getNextLanguage(currentLanguage: Idioma): Idioma{
    return when(currentLanguage){
        Idioma.English -> Idioma.French
        Idioma.French -> Idioma.German
        Idioma.German -> Idioma.Portuguese
        Idioma.Portuguese -> Idioma.Chinease
        else -> Idioma.English
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
        ItsOctoberScreen()
}

@Composable
fun LoadingScreen(
    onFinished: ()-> Unit = {}
){
    Box(
        modifier= Modifier.fillMaxSize().background(color = Color.Black),
        contentAlignment = Alignment.Center
    ){
        var currentLoadingState by remember { mutableStateOf(CargandoProperty.Default) }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progressbar))
        val progress by animateLottieCompositionAsState(composition)
        val arrayLoadingStates = listOf(
            CargandoProperty.Default,
            CargandoProperty.Variant2,
            CargandoProperty.Variant3,
            CargandoProperty.Variant4,
            CargandoProperty.Variant5,
            CargandoProperty.Variant6
        )
        LaunchedEffect(progress) {
            val progressInt = (progress * 100).toInt()
            currentLoadingState = arrayLoadingStates[progressInt / 18]
            if (progressInt == 100){
                onFinished()
            }
            Log.e("TAG", "LoadingScreen: $progress")
        }
        Column{

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(100.dp)
                    .padding(horizontal = 56.dp)
            )
            Cargando(
                property1 = currentLoadingState,
                modifier = Modifier.height(50.dp)
                )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun LoadingScreenPreview() {
    TestOctoberApp2Theme {
        LoadingScreen()
    }
}

@Composable
fun SplashScreeen(onFinished: () -> Unit = {}){

    var isBlackLogoVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit){
        delay(1000)
        isBlackLogoVisible = true
        delay(2000)
        onFinished()
    }
    AnimatedVisibility(
        visible = isBlackLogoVisible,
        enter = fadeIn(),
        exit = fadeOut()
    )
    {
        BlackLogo()
    }
    AnimatedVisibility(
        visible = !isBlackLogoVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        WhiteLogo()
    }
}

@Composable
private fun BlackLogo() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column{
            Logo(
                property1 = com.example.testoctoberapp2.logo.Property1.IoyGradient,
                modifier = Modifier
                    .width(275.dp)
                    .height(365.dp)
            )
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun WhiteLogo() {
    Box(modifier=Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.gradient_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column{
            Logo(
                property1 = com.example.testoctoberapp2.logo.Property1.IoySolid,
                modifier = Modifier
                    .width(275.dp)
                    .height(365.dp)
            )
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreeenPreview(){
    TestOctoberApp2Theme() {
        SplashScreeen()
    }
}

@Preview(widthDp = 275, heightDp = 365)
@Composable
private fun LogoProperty1SolidPreview() {
    MaterialTheme {
        RelayContainer {
            Logo(
                property1 = com.example.testoctoberapp2.logo.Property1.IoySolid,
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
        }
    }
}
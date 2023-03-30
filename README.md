# Documentación curso

Status: Not started

# Instalar android studio

Android studio 5 [Download Android Studio & App Tools - Android Developers](https://developer.android.com/studio?gclid=CjwKCAjw5pShBhB_EiwAvmnNV-JQfv_BPJ_yVb-b-gSxy9fhbukQTMhVNmVPL7SOmrOR0XCEGkzO9RoCByAQAvD_BwE&gclsrc=aw.ds)

# Instalar Relay plugin

[Cómo instalar Relay  |  Jetpack Compose  |  Android Developers](https://developer.android.com/jetpack/compose/tooling/relay/install-relay?hl=es-419#download-and)

## Agregar Relay a android studio

Copia los faltantes al gradle de nivel aplicación

```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'com.google.relay' version '0.3.02'
}
```

## Agregar lotties para animaciones

Está libreria nos ayudara a renderizar nuestro lottie

```kotlin
implementation "com.airbnb.android:lottie-compose:5.2.0"
```

```kotlin
LottieAnimation(
    composition = composition,
    progress = { progress },
    modifier = Modifier
        .height(100.dp)
        .padding(horizontal = 56.dp)
)
```

# Agregar efecto al botón

```kotlin
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
                callback() // llamada a la función
            }
        }
        true
    })
{
	//Boton aqui
}
```

# Recursos (lottie y fondo)

[https://drive.google.com/drive/folders/1TcHvhqtuDyQ9cc6bHRgNEivVKIi2jLFV?usp=sharing](https://drive.google.com/drive/folders/1TcHvhqtuDyQ9cc6bHRgNEivVKIi2jLFV?usp=sharing)
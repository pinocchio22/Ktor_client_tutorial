package com.example.ktor_client_tutorial

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.ktor_client_tutorial.ui.theme.Ktor_client_tutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ktor_client_tutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    UserListView()
                }
            }
        }
    }
}

@Composable
fun UserListView(userVM: UserVM = viewModel()) {
    
//    val users: State<List<User>> = userVM.userFlow.collectAsState()
    val users by userVM.userFlow.collectAsState()

    if(users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn() {
            items(users) {
                UserView(data = it)
            }
        }
    }
}

@Composable
fun UserView(data: User) {
    val typography = MaterialTheme.typography
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(12.dp)
            ){
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileImg(imgUrl = data.avatar)
            Column() {
                Text(text = data.name, style = typography.body1)
            }
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProfileImg(imgUrl: String, modifier: Modifier = Modifier) {

    val bitmap : MutableState<Bitmap?> = mutableStateOf(null)

    val imageModifier = modifier
        .size(50.dp, 50.dp)
        .clip(CircleShape)

    Glide.with(LocalContext.current)
        .asBitmap()
        .load(imgUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
    bitmap.value?.asImageBitmap()?.let {
        Image(bitmap = it,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = imageModifier
        )
    } ?: Image(painter = painterResource(id = R.drawable.ic_person),
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = imageModifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Ktor_client_tutorialTheme {
        UserListView()
    }
}
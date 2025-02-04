package com.example.calculator_idk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculator_idk.ui.theme.Calculator_idkTheme
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Calculator_idkTheme {
                Main()
            }
        }
    }
}

// global vars
object global {
    val default_acc = "locked out of heaven"
    var account = "locked out of heaven"
    var usernames = arrayOf<String>()
    var passwords = arrayOf<String>()

    var cart = arrayOf<Int>()
    var total_cost = 0
    var item_idx = 0
    var item_select = ""
    val item_names = arrayOf("erm", "what", "the", "skibidi", "toilet", "balls")
    val item_cost = arrayOf(100, 200, 400, 300, 600, 550)
    val item_desc = arrayOf(
        "the start of something",
        "what????",
        "descriptor",
        "our company: the most skibidi",
        "toilets",
        "stuffs"
    )
}

private fun new_account() {
    empty_cart()
    global.item_idx = 0
    global.item_select = ""
}
private fun empty_cart() {
    global.cart = arrayOf<Int>()
    global.total_cost = 0
}

@Composable
fun Main() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("details") { DetailsScreen(navController) }
        composable("login") {LoginScreen(navController)}
        composable("info") {InfoScreen(navController)}
        composable("cart") {CartScreen(navController)}
    }
}

fun MakeToast(context : Context, text : String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Details Screen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                global.cart += global.item_idx
                global.total_cost += global.item_cost.get(global.item_idx)

                // make toast
                MakeToast(context, "Added ${global.item_select} to cart!")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        // Your details screen content
        LazyColumn {
            item {
                Text(
                    text = "${global.item_select}: ",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                )
            }
            item {
                Text(
                    text = "${global.item_desc.get(global.item_idx)}\nPrice: $${global.item_cost.get(global.item_idx)}"
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(global.account)
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("login")}) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {navController.navigate("info")}) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("cart")
            }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "cart")
            }
        }
    ) {innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(global.item_names.size) { idx ->
                ElevatedButton (
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        global.item_idx = idx
                        global.item_select = global.item_names.get(idx)
                        navController.navigate("details")
                    }
                ) {
                    Text(
                        text = global.item_names.get(idx),
                        fontSize = 32.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

fun can_signup_acc(user: String, pwd: String): Boolean {
    for (u in global.usernames) {
        if (user.equals(u)) {
            return false
        }
    }
    return true
}
fun login_acc(user: String, pwd: String): Boolean {
    var idx = 0
    for (u in global.usernames) {
        if (user.equals(u)) {
            if (global.passwords.get(idx).equals(pwd)) {
                return true
            }
        }
        idx += 1
    }
    return false
}
fun signout() {
    global.account = global.default_acc
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Login")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) {innerPadding ->
        var user by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var log_txt by remember {mutableStateOf("")}
        var log_color by remember {mutableStateOf(Color(0, 0, 0))}

        Column {
            Text(
                text = "Username",
                Modifier.padding(
                    top = innerPadding.calculateTopPadding() + 4.dp,
                    bottom = 0.dp
                )
            )
            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                label = { Text("Username") },
                modifier = Modifier
                    .padding(
                        top = 0.dp,
                        bottom = 8.dp
                    )
            )
            Text(text = "Password")
            OutlinedTextField(
                value = "●".repeat(password.length),
                onValueChange = {password = it},
                label = { Text("Password") },
                modifier = Modifier
                    .padding(
                        top = 0.dp,
                        bottom = 8.dp
                    )
            )
            Row {
                Button(onClick = {
                    if (!(user.isEmpty())) {
                        if (login_acc(user, password)) {
                            global.account = user
                            new_account()
                            log_txt = "Logged in!"
                            log_color = Color(0, 255, 0)
                        } else {
                            log_txt = "Account does not exist / password is wrong"
                            log_color = Color(255, 0, 0)
                        }
                    }},
                    Modifier.padding(horizontal = 4.dp)
                    ) {
                    Text("Login")
                }
                Button(onClick = {
                    if (!(user.isEmpty())) {
                        if (can_signup_acc(user, password)) {
                            global.usernames += user
                            global.passwords += password
                            new_account()
                            log_txt = "Signed up!"
                            log_color = Color(0, 255, 0)
                        } else {
                            log_txt = "Error: Username already in use"
                            log_color = Color(255, 0, 0)
                        }
                    }
                }, Modifier.padding(horizontal = 4.dp)
                ) {
                    Text("Sign up")
                }
                Button(
                    onClick = {
                        signout()
                        new_account()
                        log_txt = "Logged out!"
                        log_color = Color(0, 0, 0)
                    },
                    Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(255, 127, 127)
                    )
                ) {
                    Text("Sign out")
                }
            }
            Text(
                text = log_txt,
                Modifier.padding(vertical = 4.dp),
                color = log_color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("About Us") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Your details screen content
        Text(
            text = "At skibidi industries, we prioritize the toilets over anything else\nPlease do not hesitate to call us if your toilet is not functional. Thank you!",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Your cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Your details screen content
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(global.cart.size) { idx ->
                Text(
                    text = "${global.item_names.get(global.cart.get(idx))}: ${global.item_cost.get(global.cart.get(idx))}"
                )
            }
            item {
                Row {
                    ElevatedButton(
                        onClick = {empty_cart()}
                    ) {
                        Text(
                            text = "Check out: $${global.total_cost}",
                            fontSize = 24.sp
                        )
                    }
                    ElevatedButton(
                        onClick = {
                            empty_cart()
                            MakeToast(context, "Emptied cart!")
                            navController.navigate("cart")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(255, 127, 127)
                        )
                    ) {
                        Text(
                            text = "Empty cart",
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}
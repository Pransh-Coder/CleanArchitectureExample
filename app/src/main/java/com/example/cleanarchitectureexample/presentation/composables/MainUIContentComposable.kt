package com.example.cleanarchitectureexample.presentation.composables

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitectureexample.Constants.USERS_DETAIL_SCREEN
import com.example.cleanarchitectureexample.Constants.USERS_LIST_SCREEN
import com.example.cleanarchitectureexample.presentation.UsersData
import com.example.cleanarchitectureexample.presentation.uiState.UserState

private const val TAG = "MainUIContentComposable"

@Composable
fun MainUIContentComposable(
    state: UserState,
    onBackPress: () -> Unit = {},
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarComposable(
                onBackPress = {
                    onBackPress.invoke()
                },
                toolbarText = "Users Data"
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator(
                    color = Color.Black
                )
            }
        } else {
            NavHost(navController = navController, startDestination = USERS_LIST_SCREEN) {
                composable(
                    route = USERS_LIST_SCREEN,
                    /*arguments = listOf(navArgument("user") {
                        type = NavType.ParcelableType(MappedUsersData::class.java)
                    }),*/
                    enterTransition = {
                        slideInHorizontally(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        ) { it }
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        ) { -it }
                    }
                ) {
                    UsersListComposable(
                        modifier = Modifier.padding(innerPadding),
                        userList = state.usersList,
                        onUserCardClicked = {
                            //navigate to user detail screen
                            navController.currentBackStackEntry?.savedStateHandle?.set("user", it)
                            navController.navigate(USERS_DETAIL_SCREEN)
                        }
                    )
                }
                composable(
                    route = USERS_DETAIL_SCREEN,
                    enterTransition = {
                        slideInHorizontally(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        ) { it }
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        ) { -it }
                    }
                ){
                    val user = navController.previousBackStackEntry?.savedStateHandle?.get<UsersData>("user")

                    UserDetailScreenComposable(
                        modifier = Modifier.padding(innerPadding),
                        mappedUser = user
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarComposable(
    toolbarText:String,
    onBackPress: () -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2C66E3)),
        title = {
            Text(
                text = toolbarText,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }, navigationIcon = {
            IconButton(
                onClick = {
                    onBackPress.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "BackIcon",
                    tint = Color.White
                )
            }
        }
    )
}


@Composable
private fun UsersListComposable(
    modifier: Modifier = Modifier,
    userList: List<UsersData>? = listOf(),
    onUserCardClicked: (UsersData) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(all = 5.dp)
    ) {
        items(userList?: emptyList()) {
            UserItem(
                usersData = it,
                onUserCardClicked = {
                //navigate to user detail screen
                    onUserCardClicked.invoke(it)
            })
        }
    }
}

@Composable
fun UserItem(usersData: UsersData, onUserCardClicked: (UsersData) -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(color = Color.White),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onUserCardClicked.invoke(usersData)
                }
                .fillMaxWidth()
                .background(color = Color(0xFFFFEFEE))
                .padding(5.dp)) {
            Text(
                text = "Name: ${usersData.name}",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            )
            Text(
                text = "Email: ${usersData.email}",
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            )
            Text(
                text = "Phone No: ${usersData.phone}",
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            )
            Text(
                text = "Company: ${usersData.company}",
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            )
            Text(
                text = "Address: ${usersData.address}",
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
            )
        }
    }
}

@Composable
fun UserDetailScreenComposable(
    modifier: Modifier = Modifier,
    mappedUser: UsersData?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFEFEE)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Face,
            contentDescription = "User Icon",
            modifier = modifier
                .size(100.dp)
        )

        Text(
            text = "Name: ${mappedUser?.name ?: "Arvind"}",
            color = Color.Black,
            fontSize = 21.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Email: ${mappedUser?.email ?: "arvind@gmail.com"}",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 5.dp)
        )

        Text(
            text = "PhoneNum: ${mappedUser?.phone ?: "9008765443"}",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 5.dp)
        )

        Text(
            text = "Address: ${mappedUser?.address ?: "45/65 Alshire, Dubai, UAE 26004"}",
            color = Color.Black,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 5.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )

        Text(
            text = "Company: ${mappedUser?.company ?: "Vogo Automotive"}",
            color = Color.Black,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun PreviewUserItem() {
    val mockData = UsersData(
        id = 1,
        name = "Pransh",
        email = "abc@12.com",
        phone = "9008765443",
        address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
        company = "BluSmart"
    )

    UserItem(mockData, onUserCardClicked = {})
}

@Preview
@Composable
private fun PreviewToolbarComposable() {
    ToolbarComposable(toolbarText = "Users Data")
}

@Preview
@Composable
private fun PreviewUsersListComposable() {
    val usersList = listOf(
        UsersData(
            id = 1,
            name = "Pransh",
            email = "abc@12.com",
            phone = "9008765443",
            address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
            company = "BluSmart"
        ),
        UsersData(
            id = 2,
            name = "Ashutosh",
            email = "xzw@12.com",
            phone = "90087657111",
            address = "Suite 847 Kulas Light, McKenziehaven, 92998-3874",
            company = "Gensol"
        )
    )

    UsersListComposable(userList = usersList, onUserCardClicked = {})
}

@Preview
@Composable
private fun PreviewUserDetailScreenComposable() {
    val mockData = UsersData(
        id = 1,
        name = "Pransh",
        email = "abc@12.com",
        phone = "9008765443",
        address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
        company = "BluSmart"
    )

    UserDetailScreenComposable(mappedUser = mockData)
}

@Preview
@Composable
private fun PreviewMainUIContentComposable() {
    val usersList = listOf(
        UsersData(
            id = 1,
            name = "Pransh",
            email = "abc@12.com",
            phone = "9008765443",
            address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
            company = "BluSmart"
        ),
        UsersData(
            id = 2,
            name = "Ashutosh",
            email = "xzw@12.com",
            phone = "90087657111",
            address = "Suite 847 Kulas Light, McKenziehaven, 92998-3874",
            company = "Gensol"
        )
    )

    MainUIContentComposable(state = UserState(usersList = usersList))
}

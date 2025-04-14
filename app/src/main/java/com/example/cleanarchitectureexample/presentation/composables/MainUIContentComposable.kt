package com.example.cleanarchitectureexample.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cleanarchitectureexample.data.remote.MappedUsersData
import com.example.cleanarchitectureexample.presentation.uiState.UserState

@Composable
fun MainUIContentComposable(
    state: UserState,
    onBackPress: () -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
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
            UsersListComposable(
                modifier = Modifier.padding(innerPadding),
                userList = state.usersList
            )
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
                }) {
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
    userList: List<MappedUsersData>? = listOf()) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(all = 5.dp)
    ) {
        items(userList?: emptyList()) {
            UserItem(it)
        }
    }
}

@Composable
fun UserItem(usersData: MappedUsersData) {
    Card (modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .background(color = Color.White),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier
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

@Preview
@Composable
private fun PreviewUserItem() {
    val mockData = MappedUsersData(
        id = 1,
        name = "Pransh",
        email = "abc@12.com",
        phone = "9008765443",
        address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
        company = "BluSmart"
    )

    UserItem(mockData)
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
        MappedUsersData(
            id = 1,
            name = "Pransh",
            email = "abc@12.com",
            phone = "9008765443",
            address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
            company = "BluSmart"
        ),
        MappedUsersData(
            id = 2,
            name = "Ashutosh",
            email = "xzw@12.com",
            phone = "90087657111",
            address = "Suite 847 Kulas Light, McKenziehaven, 92998-3874",
            company = "Gensol"
        )
    )

    UsersListComposable(userList = usersList)
}

@Preview
@Composable
private fun PreviewMainUIContentComposable() {
    val usersList = listOf(
        MappedUsersData(
            id = 1,
            name = "Pransh",
            email = "abc@12.com",
            phone = "9008765443",
            address = "Apt. 556 Kulas Light, Gwenborough, 92998-3874",
            company = "BluSmart"
        ),
        MappedUsersData(
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

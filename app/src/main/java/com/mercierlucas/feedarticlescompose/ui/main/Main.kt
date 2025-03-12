package com.mercierlucas.feedarticlescompose.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.model.ItemClicked
import com.mercierlucas.feedarticlescompose.data.network.dtos.ArticleDto
import com.mercierlucas.feedarticlescompose.ui.custom.RadioButtonSingleSelectionHorizontal
import com.mercierlucas.feedarticlescompose.ui.navigation.Screen
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import com.mercierlucas.feedarticlescompose.ui.theme.MangaColor
import com.mercierlucas.feedarticlescompose.ui.theme.OthersColor
import com.mercierlucas.feedarticlescompose.ui.theme.SportColor
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_ALL
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_OTHERS
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlescompose.utils.showToast
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import com.mercierlucas.feedarticlescompose.ui.theme.UnknownCategoryColor


@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {


    val articlesList by mainViewModel.articleListFilteredStateFlow.collectAsState()
    val currentCategory by mainViewModel.currentCategoryStateFlow.collectAsState()
    val itemClickedForDetails by mainViewModel.itemClickedForDetailsStateFlow.collectAsState()
    val isSwipeRefreshing by mainViewModel.isSwipeDisplayedStateFlow.collectAsState()
    val userId by mainViewModel.userIdStateFlow.collectAsState()
    val isDeleteBackgroundVisible by mainViewModel.isDeleteBackGroundVisibleStateFlow.collectAsState()


    val context = LocalContext.current

    MainView(
        articlesList,
        onClickAddButton = {navController.navigate(Screen.CreateArticle.route)},
        onClickLogoutButton = {
            mainViewModel.clearMySharedPreferences()
            navController.navigate(Screen.Login.route){
            popUpTo(Screen.Main.route){
                inclusive = true
            }
        } },
        onCategoryChange = { categorySelected ->
            mainViewModel.setCurrentCategory(categorySelected)
        },
        onClickOnItem = { idArticle, idUser ->
            mainViewModel.checkDestinationForItemClicked(idArticle,idUser)
        },
        currentCategory,
        itemClickedForDetails,
        isSwipeRefreshing,
        onSwipeRefresh = {
            mainViewModel.apply {
                getAllArticlesAndRefreshFilter()
                resetItemClicked()
            }

        },
        onSwipeToDelete = { idArticle ->
           if(isDeleteBackgroundVisible)
               mainViewModel.deleteArticle(idArticle)
        },
        userId,
        isDeleteBackgroundVisible,

    )

    LaunchedEffect(true) {
        mainViewModel.messageSharedFlow.collect { messageId ->
            with(context){
                showToast(getString(messageId))
            }
        }
    }

    LaunchedEffect(true) {
        mainViewModel.goToEditSharedFlow.collect{ idArticleToEdit ->
            navController.navigate(Screen.EditArticle.route + "/$idArticleToEdit")
        }
    }

    LaunchedEffect(true) {
        mainViewModel.getAllArticlesAndRefreshFilter()
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    articlesList: List<ArticleDto>,
    onClickAddButton: () -> Unit,
    onClickLogoutButton: () -> Unit,
    onCategoryChange: (Int) -> Unit,
    onClickOnItem: (Long, Long) -> Unit,
    currentCategory: Int,
    itemClickedForDetails: ItemClicked,
    isSwipeRefreshing: Boolean,
    onSwipeRefresh: () -> Unit,
    onSwipeToDelete: (Long) -> Unit,
    userId: Long,
    isDeleteBackgroundVisible: Boolean,
){




    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.new_article),
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = { onClickAddButton.invoke() })
            )
            Icon(
                imageVector = Icons.Rounded.PowerSettingsNew,
                contentDescription = stringResource(id = R.string.new_article),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = { onClickLogoutButton.invoke() })
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            val state = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = isSwipeRefreshing,
                onRefresh = { onSwipeRefresh.invoke() },
                state = state,
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isSwipeRefreshing,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        color = MaterialTheme.colorScheme.primary,
                        state = state
                    )
                },
            ) {

                val lazyListState = rememberLazyListState()

                LaunchedEffect(articlesList) {
                    lazyListState.scrollToItem(0)
                }


                LazyColumn(
                    state = lazyListState) {
                    items(
                        items = articlesList,
                        key = { it }
                    ) { article ->

                        val swipeToDismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {state ->
                                if(state == SwipeToDismissBoxValue.EndToStart) {
                                    onSwipeToDelete.invoke(article.id)
                                    true
                                }
                                else
                                    false
                            }
                        )

                        SwipeToDismissBox(
                            enableDismissFromStartToEnd = false,
                            enableDismissFromEndToStart = article.idU == userId,
                            state = swipeToDismissState,
                            backgroundContent = {
                                if(isDeleteBackgroundVisible){
                                    Row(modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .background(Color.Red),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End){
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription =
                                                stringResource(id = R.string.new_article),
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(vertical = 10.dp),
                                            tint = Color.White
                                        )
                                    }
                                }

                            }) {
                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        onClickOnItem.invoke(article.id, article.idU)
                                    },
                                border = BorderStroke(1.dp, Color.Black),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {

                                when (article.category) {
                                    CATEGORY_SPORT -> ItemArticle(
                                        article = article,
                                        modifier = Modifier.background(SportColor),
                                        itemClickedForDetails = itemClickedForDetails,

                                    )

                                    CATEGORY_MANGA -> ItemArticle(
                                        article = article,
                                        modifier = Modifier.background(MangaColor),
                                        itemClickedForDetails = itemClickedForDetails,


                                    )

                                    CATEGORY_OTHERS -> ItemArticle(
                                        article = article,
                                        modifier = Modifier.background(OthersColor),
                                        itemClickedForDetails = itemClickedForDetails,


                                    )

                                    else -> ItemArticle(
                                        article = article,
                                        modifier = Modifier.background(UnknownCategoryColor),
                                        itemClickedForDetails = itemClickedForDetails,

                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Row (Modifier.padding(vertical = 20.dp)){
            RadioButtonSingleSelectionHorizontal(
                currentCategory = currentCategory,
                radioOptions = listOf(
                    CATEGORY_ALL,
                    CATEGORY_SPORT,
                    CATEGORY_MANGA,
                    CATEGORY_OTHERS),
                callbackRBSelected = { categorySelected ->
                    onCategoryChange.invoke(categorySelected) }
            )
        }
    }
}


@Composable
fun ItemArticle(
    article: ArticleDto,
    modifier: Modifier = Modifier,
    itemClickedForDetails: ItemClicked,

) {
    with(itemClickedForDetails){
        if(idItemClicked == article.id && selected)
            ItemDetailed(
                article = article,
                modifierForMainColumnOnly = modifier,
                itemClickedForDetails = itemClickedForDetails
                )
        else
            ItemNotDetailed(
                article = article,
                modifierForMainRowOnly = modifier
            )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    FeedArticlesComposeTheme(darkTheme = false,dynamicColor = false){

    }
}
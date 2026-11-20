package com.example.praktikum_6_245150207111063_faizalrezaabdillah.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.local.DatabaseProvider
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.preferences.UserPreferencesRepository
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.remote.RetrofitClient
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.remote.StudentRemoteDataSource
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.repository.DefaultStudentRepository
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.repository.FavoriteRepository
import com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.detail.DetailViewModel
import com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.favorites.FavoritesViewModel
import com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.home.HomeViewModel
import com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.home.HomeViewModelFactory
import com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.screen.AboutScreen
import com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.screen.ProfileScreen
import com.example.praktikum_6_245150207111063_faizalrezaabdillah.navigation.Routes
import com.example.praktikum_6_245150207111063_faizalrezaabdillah.ui.screen.DetailScreen
import com.example.praktikum_6_245150207111063_faizalrezaabdillah.ui.screen.FavoritesScreen
import com.example.praktikum_6_245150207111063_faizalrezaabdillah.ui.screen.HomeScreen

@Composable
fun NavigationLabApp() {
    val remoteDataSource = remember {
        StudentRemoteDataSource(RetrofitClient.apiService)
    }
    val repository = remember {
        DefaultStudentRepository(remoteDataSource)
    }
    val context = LocalContext.current
    val preferencesRepository = remember {
        UserPreferencesRepository(context.applicationContext)
    }
    val favoriteRepository = remember {
        FavoriteRepository(
            DatabaseProvider.getDatabase(context)
                .favoriteStudentDao()
        )
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository, preferencesRepository)
            )
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = homeUiState,
                searchQuery = searchQuery,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onFavoritesClick = {
                    navController.navigate(Routes.FAVORITES)
                },
                onRetry = homeViewModel::retry,
                onRefresh = homeViewModel::refresh
            )
        }

        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(favoriteRepository, preferencesRepository)
            )
            val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()
            FavoritesScreen(
                uiState = favoritesUiState,
                onSortToggle = favoritesViewModel::toggleSortOrder,
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId =
                backStackEntry.arguments?.getInt("studentId") ?: -1

            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )

            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }

            val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

            DetailScreen(
                uiState = detailUiState,
                onToggleFavorite = detailViewModel::toggleFavorite,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

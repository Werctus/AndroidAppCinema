package com.example.cinemaclient.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaclient.data.model.GenreDto
import com.example.cinemaclient.data.model.MovieDto
import com.example.cinemaclient.data.repository.AuthRepository
import com.example.cinemaclient.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MoviesUiState {
    object Loading : MoviesUiState
    data class Success(
        val allMovies: List<MovieDto>,
        val filteredMovies: List<MovieDto>,
        val genres: List<GenreDto>,
        val selectedGenre: String? = null,
        val searchQuery: String = ""
    ) : MoviesUiState
    data class Error(val message: String) : MoviesUiState
}

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = MoviesUiState.Loading

            val moviesResult = movieRepository.getMovies()
            val genresResult = movieRepository.getGenres()

            moviesResult.onSuccess { movies ->
                val genres = genresResult.getOrDefault(emptyList())
                _uiState.value = MoviesUiState.Success(
                    allMovies = movies,
                    filteredMovies = movies,
                    genres = genres
                )
            }.onFailure { exception ->
                _uiState.value = MoviesUiState.Error(
                    exception.message ?: "Не удалось загрузить афишу"
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val current = _uiState.value as? MoviesUiState.Success ?: return
        _uiState.value = applyFilters(current.copy(searchQuery = query))
    }

    fun onGenreSelected(genreName: String?) {
        val current = _uiState.value as? MoviesUiState.Success ?: return
        val newGenre = if (current.selectedGenre == genreName) null else genreName
        _uiState.value = applyFilters(current.copy(selectedGenre = newGenre))
    }

    private fun applyFilters(state: MoviesUiState.Success): MoviesUiState.Success {
        val filtered = state.allMovies.filter { movie ->
            val matchesQuery = state.searchQuery.isBlank() ||
                    movie.title.contains(state.searchQuery.trim(), ignoreCase = true)
            val matchesGenre = state.selectedGenre == null ||
                    (movie.genres?.any { it.equals(state.selectedGenre, ignoreCase = true) } == true)
            matchesQuery && matchesGenre
        }
        return state.copy(filteredMovies = filtered)
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}

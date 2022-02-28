package com.cavigna.movieapp.utils


import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

//To reduce BiolerPlate.
//Needs to use viewLifecyleOwner 'cause it's a fragment.
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true },
    coroutineDispatcher: CoroutineDispatcher = IO
) = flow<Resource<ResultType>> {

    // check for data in database
    val data = query().firstOrNull()

    if (data != null) {
        Log.v("EMITIDO DE", " DB")
        // data is not null -> update loading status
        emit(Resource.Loading(data))
    }else{
        // Need to fetch data -> call backend
        val fetchResult = fetch()
        // got data from backend, store it in database
        try {
            saveFetchResult(fetchResult)
        }catch (e: Exception){

        }

        Log.v("EMITIDO DE", "API")
    }

    // load updated data from database (must not return null anymore)
    val updatedData = query().first()

    // emit updated data
    emit(Resource.Success(updatedData))

}.onStart {
    Log.v("networkBoundResource", "Loading")
    emit(Resource.Loading(null))

}.catch { exception ->
    Log.v("networkBoundResource", "erro")
    emit(Resource.Error(exception, null))


}.flowOn(coroutineDispatcher)

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}


fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun internetCheck(c: Context): Boolean {
    val cmg = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10+
        cmg.getNetworkCapabilities(cmg.activeNetwork)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    } else {
        return cmg.activeNetworkInfo?.isConnectedOrConnecting == true
    }

    return false
}

fun  fillPathTMDB(string: String) = "https://image.tmdb.org/t/p/w500${string}"


fun parseDate(fecha: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        val localDate =  LocalDate.parse(fecha, format);

        //val formatter = DateTimeFormatter.ofPattern("d MMM  yyyy", Locale.forLanguageTag("es-CL"))

        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
        localDate.format(formatter)

    }else{
        fecha
    }
}


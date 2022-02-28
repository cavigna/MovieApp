# MovieApp



<p align= "center">
<image src = "./images/movie_app.gif"/>
</p>

<h3>Home with Endless Recycler</h3>

> 👉La app deberá tener una pantalla principal en donde se muestre una lista de películas populares (ver The Movie Database API) con sus respectivos títulos y portadas.

#### Adicional:
>Dado que el endpoint de películas populares es paginado, la aplicación podrá consultar por nuevas a medida que el usuario navegue entre estas. Es decir, que al llegar al final de la lista que se muestra en pantalla, la aplicación busque nuevo contenido en la API.

<p align= "center">
<image src = "./images/movie_home.gif"/>
</p>

<h3>Movie Details</h3>

>👉Cada una de esas películas podrá ser seleccionada y se desplegará una nueva vista con los detalles de la misma (género, idioma original, popularidad,fecha de estreno, entre otras).

<p align= "center">
<image src = "./images/movie_app_details_view.gif"/>
</p>

#### Adicional:
>Para aumentar la performance de la aplicación, se busca evitar la consulta continua de una misma película. Por este motivo, la aplicación deberá almacenar los detalles de las películas ya vistas. En caso de que el usuario seleccione una de ellas se consultará dicha información guardada en el dispositivo, caso contrario, deberá consultar a la API correspondiente

```kotlin
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
        
        // data is not null -> update loading status
        emit(Resource.Loading(data))
    }else{
        // Need to fetch data -> call backend
        val fetchResult = fetch()
        // got data from backend, store it in database
        try {
            saveFetchResult(fetchResult)
        }catch (e: Exception){}
        
    }
    // load updated data from database (must not return null anymore)
    val updatedData = query().first()

    // emit updated data
    emit(Resource.Success(updatedData))

}.onStart {
    
    emit(Resource.Loading(null))

}.catch { exception ->
    
    emit(Resource.Error(exception, null))


}.flowOn(coroutineDispatcher)
``` 


<h3>Offline Support</h3>
<p align= "center">
<image src = "./images/movie_offline.gif"/>
</p>

<h3>Search Movie(also handling movies not in api)</h3>

>👉En caso de que alguna consulta falle o algún listado esté vacío deberán mostrarse los correspondientes errores en pantalla

#### Adicional:
>● Agregar un campo de búsqueda, que permita filtrar aquellas películas que contengan dicho texto. La búsqueda deberá realizarse entre el listado que se encuentra visible, si no hay resultado satisfactorio, deberá mostrarse su error correspondiente

<p align= "center">
<image src = "./images/movie_search.gif"/>
</p>

<h3>Favorite Movies(Stored in DB)</h3>
<p align= "center">
<image src = "./images/movie_favorite.gif"/>
</p>


<h3>Post Rating</h3>

> 👉La API posee un endpoint para evaluar una película determinada (ver The Movie Database API). Agregar en la vista de detalle la posibilidad de evaluar una película y actualizar la API con dicha información.

Según la documentación de [TMDB](https://developers.themoviedb.org/3/movies/rate-movie), se requiere una id de sesión, de forma obligatoria. Entonces, necsitamos:
 
 1. Una llamada GET para obtener el id de sesión.
 2. Una llamada de Tipo POST con el valor de la película.
 3. La llamada POST, debe contener un cuerpo con el valor de la película.

```kotlin
interface Service{
// .....
     @GET("authentication/guest_session/new")
    suspend fun fetchGuestSession(
        @Query(value= "api_key") apiKey:String = APIKEY,
    ): GuestSession // 1. 

    @POST("movie/{id}/rating")
    suspend fun postRating( //2.
        @Path("id")id:Int,
        @Body rating : JsonObject, //3.
        @Query("guest_session_id") guestSessionId: String,
        @Query(value= "api_key") apiKey:String = APIKEY,

    ): RespuestaPostRating
}
//*****//
@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repositorio) : ViewModel() {
//.....

    val puntaje: MutableLiveData<Double> = MutableLiveData(0.0)

    fun postRating(id: Int, rating: Double = puntaje.value!!) {
        viewModelScope.launch(IO) {


            val guestSessionId =
                withContext(this.coroutineContext) { repo.fetchGuestSession() }//1.
            Log.i("post", guestSessionId.toString())

            if (puntaje.value!! > 0.5 && guestSessionId != null) {
                val jsonObjectRating = JsonObject() //3.
                jsonObjectRating.addProperty("value", puntaje.value)
                repo.postRating(id, jsonObjectRating, guestSessionId.guestSessionId).also { //2.
                    Log.i("post", it.toString())
                }
            }
        }
    }
}

//*****//
@AndroidEntryPoint
class DetailsFragment : Fragment() {
    //....

        button.setOnClickListener {
        try {
            viewModel.postRating(movie.id)
            Snackbar.make(
                coordinatorLayoutDetails,

                "You rated ${movie.originalTitle} with ${viewModel.puntaje.value} stars",
                Snackbar.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "No hay conexión",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
```
![post](./images/rating_log.jpg)

<p align= "center">
<image src = "./images/movie_rate.gif"/>
</p>
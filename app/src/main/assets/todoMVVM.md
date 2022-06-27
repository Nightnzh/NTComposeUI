### Todo demo in MVVM Architecture
---
#### Todo model 
```kotlin
@Entity
data class TodoModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val completed: Boolean = false,
    val createdDateTime : String = DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),
    val completedDateTime : String? = null
)
```
---
#### Todo Repository(Where data from)
```kotlin
class TodoRepository(private val todoModelDao: TodoModelDao) {

    val todos = todoModelDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(todoModel: TodoModel) = todoModelDao.insert(todoModel)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(todoModel: TodoModel) = todoModelDao.delete(todoModel)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(todoModel: TodoModel) = todoModelDao.update(todoModel)

}
```
---
#### Local db and dao(Database access object)
```kotlin
// using android jetpack room database
@Dao
interface TodoModelDao {

    @Query("SELECT * FROM TodoModel ORDER BY createdDateTime DESC")
    fun getAll(): Flow<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoModel: TodoModel)

    @Delete
    suspend fun delete(todoModel: TodoModel)

    @Update
    suspend fun update(todoModel: TodoModel)

}

//create db
//Singleton
@Database(entities = [TodoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoModelDao(): TodoModelDao

    //在初始建立db時加入10筆資料
    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.todoModelDao()

                    (1..10).forEach {
                        dao.insert(TodoModel(title = "Todo $it"))
                    }
                }
            }
        }
    }
    
    companion object{
        const val DB_NAME = "NTC_AppDatabase"

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context,scope: CoroutineScope) : AppDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }
}
//Application 擴充
//記得在Manifest.xml <application/> 中新增或修改 android:name=".NTCApplication"
class NTCApplication : Application() {
    //--- data store
    val dataStore by preferencesDataStore("setting")
    //---
    private val applicationScope = CoroutineScope(SupervisorJob())
    val db by lazy { AppDatabase.getInstance(this,applicationScope) }
    val repository by lazy { TodoRepository(db.todoModelDao()) }
}

```
---
#### ViewModel
```kotlin
class TodosViewModel(private val todoRepository: TodoRepository) : ViewModel() {


    val todos = todoRepository.todos

    fun insertTodo(todo: TodoModel) = viewModelScope.launch {
        todoRepository.insert(todo)
    }

    fun deleteTodo(todo: TodoModel) = viewModelScope.launch {
        todoRepository.delete(todo)
    }

    fun updateTodo(todo: TodoModel) = viewModelScope.launch {
        todoRepository.update(todo)
    }

}


class TodoViewModelFactory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodosViewModel(todoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

```
---
#### Todo demos using compose ui 
```kotlin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodosView(
    todosViewModel: TodosViewModel = viewModel(
        factory = TodoViewModelFactory((LocalContext.current.applicationContext as NTCApplication).repository)
    )
) {

    val todos by todosViewModel.todos.collectAsState(initial = listOf<TodoModel>())

    var showAddTodoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.9f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            stickyHeader("Doing") {
                Text("Doing")
            }
            itemsIndexed(items = todos.filter { todo -> !todo.completed }) { _, todo ->
                TodoItem(todo,
                    onCheckedChange = {
                        todosViewModel.updateTodo(
                            todo.copy(
                                completed = it,
                                completedDateTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss")
                            )
                        )
                    },
                    onDelete = {
                        todosViewModel.deleteTodo(todo)
                    }
                )
            }

            stickyHeader("Completed") {
                Text("Completed")
            }
            itemsIndexed(items = todos.filter { todo -> todo.completed }) { _, todo ->
                TodoItem(todo,
                    onCheckedChange = {
                        todosViewModel.updateTodo(
                            todo.copy(
                                completed = it,
                                completedDateTime = null
                            )
                        )
                    },
                    onDelete = {
                        todosViewModel.deleteTodo(todo)
                    }
                )
            }

        }
        ExtendedFloatingActionButton(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = "ADD") },
            onClick = { showAddTodoDialog = true }
        )
    }
    AddTodoDialog(
        showAddTodoDialog = showAddTodoDialog,
        onDismissRequest = { showAddTodoDialog = false },
        addTodo = {
            todosViewModel.insertTodo(it)
            showAddTodoDialog = false
        }
    )

}

@Composable
private fun AddTodoDialog(
    showAddTodoDialog: Boolean,
    onDismissRequest: () -> Unit,
    addTodo: (TodoModel) -> Unit
) {

    var todoTitle by remember { mutableStateOf("") }

    if (showAddTodoDialog)
        Dialog(onDismissRequest = { onDismissRequest(); todoTitle = "" }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background, shape = Shapes.medium)
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Add Todo")
                OutlinedTextField(
                    value = todoTitle,
                    onValueChange = { todoTitle = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                )
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        addTodo(TodoModel(title = todoTitle.trim()))
                    }) {
                        Text("OK")
                    }
                }

            }
        }

}
```




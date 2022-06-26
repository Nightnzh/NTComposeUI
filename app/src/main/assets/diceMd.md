
### Dice

```kotlin

class Dice(val count: Int) {
    fun roll() = (1..count).random()
}

@Composable
fun DiceDemo() {

    var inputCount by remember { mutableStateOf<String>("") }

    val diceList = remember { mutableStateListOf<Dice>(Dice(4)) }

    fun addDice() {
        val newDice = Dice(inputCount.toInt())
        diceList.add(newDice)
    }


    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = inputCount, onValueChange = { inputCount = it },
                label = { Text(text = "Dice count") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                addDice()

            }) {
                Text(text = "Add Dice")
            }
        }
        LazyColumn() {

            itemsIndexed(items = diceList.toTypedArray()) { index: Int, item: Dice ->

                var rollNumber by remember {
                    mutableStateOf<Int?>(null)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Dice(${item.count})")
                    Text(text = "rolled: (${rollNumber})")
                    Button(onClick = { rollNumber = item.roll() }) {
                        Text(text = "roll")
                    }
                }
            }
        }
    }
}

```
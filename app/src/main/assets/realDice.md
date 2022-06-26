
### Real Dice 

```kotlin
@Composable
fun RealDiceRollerView(){

    val dice = Dice()
    var rolledNum by remember { mutableStateOf(1) }
    var diceImageId by remember { mutableStateOf(R.drawable.dice_1) }

    fun roll() {
        rolledNum = (1..dice.numSides).random()
        diceImageId = when(rolledNum){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(modifier = Modifier.fillMaxWidth(0.6f),painter = painterResource(id = diceImageId), contentDescription = "dice image")
        Button(onClick = { roll() }) {
            Text(text = "roll")
        }
    }
}
```
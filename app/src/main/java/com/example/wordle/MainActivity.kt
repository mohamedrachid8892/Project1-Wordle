package com.example.wordle

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.jinatonic.confetti.CommonConfetti
import com.github.jinatonic.confetti.ConfettiView


class MainActivity : AppCompatActivity() {
    private var numGuesses = 0
    private val wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    private var currentUserGuess = 1
    private var wordLength = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_main)

        val mPrefs = getSharedPreferences("streak", 0)
        var streak = mPrefs.getString("streak", "0")!!.toInt()
        findViewById<TextView>(R.id.streakNumber).text = streak.toString()


        val finalAnswer = findViewById<TextView>(R.id.finalAnswer)
        finalAnswer.text = wordToGuess
        val editText = findViewById<EditText>(R.id.input)
        val guessButton = findViewById<Button>(R.id.button)
        val resetButton = findViewById<Button>(R.id.resetButton)
        var userGuess = findViewById<TextView>(R.id.user_guess_1)
        var checkGuess = findViewById<TextView>(R.id.guess_1_check)

        // Handle input when using the physical "Enter" key on your keyboard
        editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (editText.length() < wordLength) {
                    displayLengthErrorMessage(editText)
                }
                else {
                    // Make submit guess function for all event listeners
                    userGuess = updateUserGuess(currentUserGuess)
                    checkGuess = updateGuessCheck(currentUserGuess)
                    numGuesses++
                    currentUserGuess++
                    editText.hideKeyboard()
                    userGuess.text = editText.text.toString().uppercase()
                    editText.text.clear()

                    checkGuess(userGuess.text.toString(), checkGuess)
                    if (checkIfWin(checkGuess.text.toString())) {
                        Toast.makeText(this, "Congratulations, you won!", Toast.LENGTH_SHORT).show()
                        streak++
                        updateStreak(true, streak)
                        finalAnswer.visibility = View.VISIBLE
                        resetButton.visibility = View.INVISIBLE
                        editText.isFocusable = false
                        guessButton.text = "Restart"
                        guessButton.setOnClickListener {
                            overridePendingTransition(0,0)
                            finish()
                            overridePendingTransition(0,0)
                            startActivity(intent)
                        }
                    }
                    else if (outOfGuesses(numGuesses)) {
                        finalAnswer.visibility = View.VISIBLE
                        resetButton.visibility = View.INVISIBLE
                        editText.isFocusable = false

                        streak = 0
                        updateStreak(false, streak)

                        guessButton.text = "Restart"
                        guessButton.setOnClickListener {
                            overridePendingTransition(0,0)
                            finish()
                            overridePendingTransition(0,0)
                            startActivity(intent)
                            Toast.makeText(applicationContext, "Game Restarted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return@OnKeyListener true
                }
            }
            false
        })

        // Handle input when using the "Go" key on the keyboard.
        editText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (editText.length() < wordLength) {
                    displayLengthErrorMessage(editText)
                }
                else {
                    userGuess = updateUserGuess(currentUserGuess)
                    checkGuess = updateGuessCheck(currentUserGuess)
                    numGuesses++
                    currentUserGuess++
                    editText.hideKeyboard()
                    userGuess.text = editText.text.toString().uppercase()
                    editText.text.clear()

                    checkGuess(userGuess.text.toString(), checkGuess)
                    if (checkIfWin(checkGuess.text.toString())) {
                        Toast.makeText(this, "Congratulations, you won!", Toast.LENGTH_SHORT).show()
                        streak++
                        updateStreak(true, streak)
                        finalAnswer.visibility = View.VISIBLE
                        resetButton.visibility = View.INVISIBLE
                        editText.isFocusable = false
                        guessButton.text = "Restart"
                        guessButton.setOnClickListener {
                            overridePendingTransition(0,0)
                            finish()
                            overridePendingTransition(0,0)
                            startActivity(intent)
                        }
                    }
                    else if (outOfGuesses(numGuesses)) {
                        finalAnswer.visibility = View.VISIBLE
                        resetButton.visibility = View.INVISIBLE
                        editText.isFocusable = false

                        streak = 0
                        updateStreak(false, streak)

                        guessButton.text = "Restart"
                        guessButton.setOnClickListener {
                            overridePendingTransition(0,0)
                            finish()
                            overridePendingTransition(0,0)
                            startActivity(intent)
                            Toast.makeText(applicationContext, "Game Restarted", Toast.LENGTH_SHORT).show()
                        }
                    }

                    return@OnEditorActionListener true
                }
            }
            false
        })

        // Handle input when using the "GUESS" button
        guessButton.setOnClickListener {
            if (editText.length() < wordLength) {
                displayLengthErrorMessage(editText)
            }
            else {
                userGuess = updateUserGuess(currentUserGuess)
                checkGuess = updateGuessCheck(currentUserGuess)
                numGuesses++
                currentUserGuess++
                editText.hideKeyboard()
                userGuess.text = editText.text.toString().uppercase()
                editText.text.clear()

                checkGuess(userGuess.text.toString(), checkGuess)
                if (checkIfWin(checkGuess.text.toString())) {
                    Toast.makeText(this, "Congratulations, you won!", Toast.LENGTH_SHORT).show()
                    streak++
                    updateStreak(true, streak)
                    finalAnswer.visibility = View.VISIBLE
                    resetButton.visibility = View.INVISIBLE
                    editText.isFocusable = false
                    guessButton.text = "Restart"
                    guessButton.setOnClickListener {
                        overridePendingTransition(0,0)
                        finish()
                        overridePendingTransition(0,0)
                        startActivity(intent)
                    }
                }
                else if (outOfGuesses(numGuesses)) {
                    finalAnswer.visibility = View.VISIBLE
                    resetButton.visibility = View.INVISIBLE
                    editText.isFocusable = false

                    streak = 0
                    updateStreak(true, streak)

                    guessButton.setText("Restart")
                    guessButton.setOnClickListener {
                        overridePendingTransition(0,0)
                        finish()
                        overridePendingTransition(0,0)
                        startActivity(intent)
                        Toast.makeText(applicationContext, "Game Restarted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        resetButton.setOnClickListener {
            val mEditor = mPrefs.edit()
            streak = 0
            mEditor.putString("streak", streak.toString()).apply()
            findViewById<TextView>(R.id.streakNumber).text = "0"
            overridePendingTransition(0,0)
            finish()
            overridePendingTransition(0,0)
            startActivity(intent)
            Toast.makeText(applicationContext, "Game Restarted", Toast.LENGTH_SHORT).show()
        }
    }

    // author: calren
    object FourLetterWordList {
        // List of most common 4 letter words from: https://7esl.com/4-letter-words/
        val fourLetterWords =
            "Area,Army,Baby,Back,Ball,Band,Bank,Base,Bill,Body,Book,Call,Card,Care,Case,Cash,City,Club,Cost,Date,Deal,Door,Duty,East,Edge,Face,Fact,Farm,Fear,File,Film,Fire,Firm,Fish,Food,Foot,Form,Fund,Game,Girl,Goal,Gold,Hair,Half,Hall,Hand,Head,Help,Hill,Home,Hope,Hour,Idea,Jack,John,Kind,King,Lack,Lady,Land,Life,Line,List,Look,Lord,Loss,Love,Mark,Mary,Mind,Miss,Move,Name,Need,News,Note,Page,Pain,Pair,Park,Part,Past,Path,Paul,Plan,Play,Post,Race,Rain,Rate,Rest,Rise,Risk,Road,Rock,Role,Room,Rule,Sale,Seat,Shop,Show,Side,Sign,Site,Size,Skin,Sort,Star,Step,Task,Team,Term,Test,Text,Time,Tour,Town,Tree,Turn,Type,Unit,User,View,Wall,Week,West,Wife,Will,Wind,Wine,Wood,Word,Work,Year,Bear,Beat,Blow,Burn,Call,Care,Cast,Come,Cook,Cope,Cost,Dare,Deal,Deny,Draw,Drop,Earn,Face,Fail,Fall,Fear,Feel,Fill,Find,Form,Gain,Give,Grow,Hang,Hate,Have,Head,Hear,Help,Hide,Hold,Hope,Hurt,Join,Jump,Keep,Kill,Know,Land,Last,Lead,Lend,Lift,Like,Link,Live,Look,Lose,Love,Make,Mark,Meet,Mind,Miss,Move,Must,Name,Need,Note,Open,Pass,Pick,Plan,Play,Pray,Pull,Push,Read,Rely,Rest,Ride,Ring,Rise,Risk,Roll,Rule,Save,Seek,Seem,Sell,Send,Shed,Show,Shut,Sign,Sing,Slip,Sort,Stay,Step,Stop,Suit,Take,Talk,Tell,Tend,Test,Turn,Vary,View,Vote,Wait,Wake,Walk,Want,Warn,Wash,Wear,Will,Wish,Work,Able,Back,Bare,Bass,Blue,Bold,Busy,Calm,Cold,Cool,Damp,Dark,Dead,Deaf,Dear,Deep,Dual,Dull,Dumb,Easy,Evil,Fair,Fast,Fine,Firm,Flat,Fond,Foul,Free,Full,Glad,Good,Grey,Grim,Half,Hard,Head,High,Holy,Huge,Just,Keen,Kind,Last,Late,Lazy,Like,Live,Lone,Long,Loud,Main,Male,Mass,Mean,Mere,Mild,Nazi,Near,Neat,Next,Nice,Okay,Only,Open,Oral,Pale,Past,Pink,Poor,Pure,Rare,Real,Rear,Rich,Rude,Safe,Same,Sick,Slim,Slow,Soft,Sole,Sore,Sure,Tall,Then,Thin,Tidy,Tiny,Tory,Ugly,Vain,Vast,Very,Vice,Warm,Wary,Weak,Wide,Wild,Wise,Zero,Ably,Afar,Anew,Away,Back,Dead,Deep,Down,Duly,Easy,Else,Even,Ever,Fair,Fast,Flat,Full,Good,Half,Hard,Here,High,Home,Idly,Just,Late,Like,Live,Long,Loud,Much,Near,Nice,Okay,Once,Only,Over,Part,Past,Real,Slow,Solo,Soon,Sure,That,Then,This,Thus,Very,When,Wide"

        // Returns a list of four letter words as a list
        fun getAllFourLetterWords(): List<String> {
            return fourLetterWords.split(",")
        }

        // Returns a random four letter word from the list in all caps
        fun getRandomFourLetterWord(): String {
            val allWords = getAllFourLetterWords()
            val randomNumber = (0..allWords.size).shuffled().last()
            return allWords[randomNumber].uppercase()
        }
    }
    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *   textView : guess string with colored letters showing guess correctness
     * Returns nothing. Adjusts the guess and modifies the guess TextView according to the following colors:
     *   Green letter represents right letter right places
     *   Blue letter represents the right letter in the wrong place
     *   Red letter represents a letter not in the target word
     */
    private fun checkGuess(guess: String, textView : TextView) {

        val sb = SpannableString(guess.toString())

        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                val fColor = ForegroundColorSpan(Color.GREEN)
                sb.setSpan(fColor,i, i+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            else if (guess[i] in wordToGuess) {
                val fColor = ForegroundColorSpan(Color.BLUE)
                sb.setSpan(fColor,i, i+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            else {
                val fColor = ForegroundColorSpan(Color.RED)
                sb.setSpan(fColor,i, i+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
        textView.text = sb
    }

    // If the input word is not of the required length, clear the EditText field and display an error message
    private fun displayLengthErrorMessage(editText : EditText) {
        editText.hideKeyboard()
        editText.text.clear()
        Toast.makeText(applicationContext, "Your guess has to be four letters long", Toast.LENGTH_SHORT).show()
    }

    // Updates the guess TextView to the next guess that is to be updated
    private fun updateUserGuess(currentUserGuess: Int): TextView? {
        val guessID = resources.getIdentifier("user_guess_$currentUserGuess", "id", packageName)
        return findViewById<TextView>(guessID)
    }

    //Updates the guess feedback TextView to the next feedback that is to be updated
    private fun updateGuessCheck(currentUserGuess: Int): TextView? {
        val guessCheckID = resources.getIdentifier("guess_feedback_$currentUserGuess", "id", packageName)
        return findViewById<TextView>(guessCheckID)
    }

    // Check if the guess is correct
    private fun checkIfWin(string : String): Boolean {
        if (string == wordToGuess) {
            CommonConfetti.rainingConfetti(findViewById(R.id.main),  intArrayOf(Color.RED, Color.BLUE,Color.GREEN, Color.CYAN, Color.YELLOW, Color.MAGENTA))
                .infinite();
            return true
        }
        return false
    }

    // Check if the user is out of guesses.
    private fun outOfGuesses(numGuesses: Int): Boolean {
        if (numGuesses == 3) {
            Toast.makeText(this, "You are out of guesses!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun updateStreak(didWin : Boolean, streak : Int) {
        val mPrefs = getSharedPreferences("streak", 0)
        if (didWin) {
            val mEditor = mPrefs.edit()
            mEditor.putString("streak", streak.toString()).apply()
            findViewById<TextView>(R.id.streakNumber).text = streak.toString()
        }
        else {
            val mEditor = mPrefs.edit()
            mEditor.putString("streak", streak.toString()).apply()
            findViewById<TextView>(R.id.streakNumber).text = "0"
        }
    }

    // Function to force hide the soft keyboard.
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

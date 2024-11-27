package id.co.mentalhealth.ui.quest.optional

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityQuestionsBinding

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}
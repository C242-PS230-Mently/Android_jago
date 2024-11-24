package id.co.mentalhealth.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.R
import id.co.mentalhealth.databinding.ActivityRegistrasiBinding
import id.co.mentalhealth.ui.registrasi.RegistFragment1

class Registrasi_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mFragmentManager = supportFragmentManager
        val fragment1 = RegistFragment1()

        val fragment = mFragmentManager.findFragmentByTag(RegistFragment1::class.java.simpleName)
        if (fragment !is RegistFragment1) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.container_register, fragment1, RegistFragment1::class.java.simpleName)
                .commit()
        }
    }
}
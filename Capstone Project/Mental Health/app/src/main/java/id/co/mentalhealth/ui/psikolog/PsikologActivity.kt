package id.co.mentalhealth.ui.psikolog

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.mentalhealth.databinding.ActivityPsikologBinding
import id.co.mentalhealth.ui.adapter.PsikologAdapter

class PsikologActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPsikologBinding

    private lateinit var psikologAdapter: PsikologAdapter

    private val viewModel by viewModels<PsikologViewModel> {
        PsikologViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPsikologBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getDoctor()

        viewModel.doctor.observe(this){ result ->
            result.onSuccess { doctorResponse ->
                val doctorList = doctorResponse.doctor
                Log.d("Doctor", doctorList.toString())
                if (!doctorList.isNullOrEmpty()) {
                    psikologAdapter.submitList(doctorList)
                }else{
                    binding.tvDoctor.text = "Tidak ada psikolog"
                }
            }
            result.onFailure {
                binding.tvDoctor.text = "Gagal mengambil psikolog."
            }
        }

        psikologAdapter = PsikologAdapter()
        binding.listDoctorRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,  false)
        binding.listDoctorRecycler.adapter = psikologAdapter

        binding.icBack.setOnClickListener {
            finish()
        }
    }
}
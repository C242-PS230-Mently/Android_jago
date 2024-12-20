package id.co.mentalhealth.ui.psikolog

import android.os.Bundle
import android.util.Log
import android.view.View
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

        showLoading(true)

        viewModel.getDoctor()

        viewModel.doctor.observe(this){ result ->
            result.onSuccess { doctorResponse ->
                val doctorList = doctorResponse.doctor
                Log.d("Doctor", doctorList.toString())
                showLoading(false)
                if (!doctorList.isNullOrEmpty()) {
                    psikologAdapter.submitList(doctorList)
                }else{
                    binding.tvDoctor.text = "Tidak ada psikolog"
                }
            }
            result.onFailure {
                showLoading(false)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
}
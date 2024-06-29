package br.edu.utfpr.fastfuels

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.fastfuels.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fuel1: String
    private lateinit var fuel2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        binding.compareButton.setOnClickListener {
            btCompareButtonOnClick()
        }

        binding.listFuel1.setOnClickListener {
            btListFuel1OnClick()
        }

        binding.listFuel2.setOnClickListener {
            btListFuel2OnClick()
        }
    }

    private fun btListFuel1OnClick() {
        val intent = Intent(this, ListActivity::class.java)

        getFuel1.launch(intent)
    }

    private fun btListFuel2OnClick() {
        val intent = Intent(this, ListActivity::class.java)

        getFuel2.launch(intent)
    }

    private val getFuel1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data != null) {
                val returnFuel = it.data?.getStringExtra("returnFuel")
                fuel1 = returnFuel.toString()
                binding.consumptionFuelEditText1.hint = fuel1
                binding.listFuel1.error = null
            }
        }
    }

    private val getFuel2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data != null) {
                val returnFuel = it.data?.getStringExtra("returnFuel")
                fuel2 = returnFuel.toString()
                binding.consumptionFuelEditText2.hint = fuel2
                binding.listFuel2.error = null
            }
        }
    }

    private fun btCompareButtonOnClick() {
        val addressConsumptionPerDistanceFuel1 = binding.consumptionFuel1
        val addressConsumptionPerDistanceFuel2 = binding.consumptionFuel2
        val addressPriceFuel1 = binding.pricePerGallon1
        val addressPriceFuel2 = binding.pricePerGallon2
        
        if (addressConsumptionPerDistanceFuel1.text.toString().isEmpty()) {
            addressConsumptionPerDistanceFuel1.error = getString(R.string.field_error)
            addressConsumptionPerDistanceFuel1.requestFocus()
            return
        }

        if (addressConsumptionPerDistanceFuel2.text.toString().isEmpty()) {
            addressConsumptionPerDistanceFuel2.error = getString(R.string.field_error)
            addressConsumptionPerDistanceFuel2.requestFocus()
            return
        }

        if (addressPriceFuel1.text.toString().isEmpty()) {
            addressPriceFuel1.error = getString(R.string.field_error)
            addressPriceFuel1.requestFocus()
            return
        }

        if (addressPriceFuel2.text.toString().isEmpty()) {
            addressPriceFuel2.error = getString(R.string.field_error)
            addressPriceFuel2.requestFocus()
            return
        }

        if (!this::fuel1.isInitialized) {
            binding.listFuel1.error = getString(R.string.list_error)
            binding.listFuel1.requestFocus()
            return
        }

        if (!this::fuel2.isInitialized) {
            binding.listFuel2.error = getString(R.string.list_error)
            binding.listFuel2.requestFocus()
            return
        }

        val consumptionPerDistanceFuel1 = addressConsumptionPerDistanceFuel1.text.toString().toDoubleOrNull()
        val consumptionPerDistanceFuel2 = addressConsumptionPerDistanceFuel2.text.toString().toDoubleOrNull()
        val priceFuel1 = addressPriceFuel1.text.toString().toDoubleOrNull()
        val priceFuel2 = addressPriceFuel2.text.toString().toDoubleOrNull()

        val costBenefitFuel1 = consumptionPerDistanceFuel1?.div(priceFuel1!!)
        val costBenefitFuel2 = consumptionPerDistanceFuel2?.div(priceFuel2!!)

        val result = if (costBenefitFuel1!! > costBenefitFuel2!!) {
            "$fuel1 is better!"
        } else if (costBenefitFuel1 < costBenefitFuel2) {
            "$fuel2 is better!"
        } else {
            "Its a draw, choose which you prefer!"
        }

        binding.resultTextView.text = result
    }
}
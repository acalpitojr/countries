package com.calpito.walmart.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calpito.walmart.presentation.viewmodels.MainViewModelFactory
import com.calpito.walmart.R
import com.calpito.walmart.databinding.ActivityMainBinding
import com.calpito.walmart.di.Dependencies
import com.calpito.walmart.domain.model.RecyclerData
import com.calpito.walmart.domain.model.RecyclerData.CountryData
import com.calpito.walmart.domain.model.UIState
import com.calpito.walmart.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.launch


/*TODO
*  1. add search feature
*  2. implement random backoff for api issues
* */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: recyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModelFactory = MainViewModelFactory(Dependencies.getCountriesUseCase)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]


        initView()


    }

    /**
     * Initializes the view components and sets up data bindings and user interactions.
     * This function performs the following operations:
     *
     * 1. Initializes and sets up the RecyclerView:
     *    - Creates and assigns an adapter to handle the display of country data.
     *    - Sets a LinearLayoutManager to manage the layout of items in a linear fashion.
     *
     * 2. Manages UI states through a coroutine that observes the countries StateFlow from the ViewModel:
     *    - Handles different UI states such as Loading, Success, and Error.
     *    - Updates the UI accordingly by showing loading indicators, data, or error messages.
     *
     * 3. Sets up interaction for the retry button to re-fetch country data when an error occurs:
     *    - Assigns a click listener to the retry button which triggers the fetching of countries from the ViewModel.
     */
    private fun initView() {
        //recycler view init
        adapter = recyclerViewAdapter()
        binding.rvCountries.layoutManager = LinearLayoutManager(this)
        binding.rvCountries.adapter = adapter

        //VIEW STATES
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.recyclerData.collect { state ->
                    when (state) {
                        is UIState.Loading -> showLoading()
                        is UIState.Success -> showData(state.data)
                        is UIState.Error -> showError(state.message)
                    }
                }
            }
        }

        binding.btnRetry.setOnClickListener { mainViewModel.getCountries() }


    }

    /**
     * Displays the loading indicator and hides the retry button.
     * This function is responsible for updating the UI during a loading state,
     * specifically:
     *
     * - Making the progress bar visible to indicate that data loading is in progress.
     * - Hiding the retry button as it's not needed during data loading.
     */
    private fun showLoading() {
        // Display loading indicator
        binding.progress.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.GONE
    }

    /**
     * Updates the UI to display the list of countries and hides any UI elements associated with loading or errors.
     * Specifically, this function:
     *
     * - Hides the progress bar used to indicate loading, as the data loading is complete.
     * - Hides the retry button, since it is only necessary when there is an error in data retrieval.
     * - Updates the adapter for the RecyclerView with the new list of countries, causing the UI to refresh the displayed data.
     *
     * @param countries The list of CountryData to be displayed in the RecyclerView.
     */
    private fun showData(countries: List<RecyclerData>) {
        // Update adapter and hide loading indicator
        binding.progress.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        adapter.submitList(countries)
    }

    /**
     * Displays an error message to the user via a Toast and updates the UI to reflect an error state.
     * This function takes care of the following:
     *
     * - Hiding the progress bar, as the loading process has concluded with an error.
     * - Displaying a toast notification with the error message provided, informing the user of what went wrong.
     * - Making the retry button visible, allowing the user to attempt to reload the data.
     *
     * @param errorMsg The error message that will be shown to the user in the toast notification.
     */
    private fun showError(errorMsg: String) {
        binding.progress.visibility = View.GONE
        // Show error message
        Toast.makeText(this, "Error: ${errorMsg}", Toast.LENGTH_LONG).show()
        binding.btnRetry.visibility = View.VISIBLE
    }

    class recyclerViewAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //views to be filled
            val nameTextView: TextView? = itemView.findViewById(R.id.tv_name)
            val regionTextView: TextView? = itemView.findViewById(R.id.tv_region)
            val codeTextView: TextView? = itemView.findViewById(R.id.tv_code)
            val capitalTextView: TextView? = itemView.findViewById(R.id.tv_capital)

            fun bind(country: RecyclerData.CountryData) {
                var countryName = country.name ?: ""
                if (countryName.isNotEmpty()) {
                    countryName = "$countryName,"
                }

                nameTextView?.text = countryName
                regionTextView?.text = country.region ?: ""
                codeTextView?.text = country.code ?: ""
                capitalTextView?.text = country.capital ?: ""
            }
        }

        inner class HeaderViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
            val tvLine1 = itemView.findViewById<TextView>(R.id.tv_line1)

            fun bind(text:String){
                tvLine1?.text = text
            }

        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        //we will use the id of the layout depending on which type of item we have
        override fun getItemViewType(position: Int): Int {
            val recyclerData = differ.currentList[position]
            val viewType = when(recyclerData){
                is CountryData -> {
                    R.layout.county_item
                }
                is RecyclerData.CountryHeader -> {
                    R.layout.header_item
                }
            }

            return viewType
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //create view holder depending on which layout we have
           val viewHolder = when(viewType){
                R.layout.county_item->{
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.county_item, parent, false)
                    MyViewHolder(view)
                }
                R.layout.header_item->{
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.header_item, parent, false)
                    HeaderViewHolder(view)
                }

               else -> {
                   val view = LayoutInflater.from(parent.context).inflate(R.layout.county_item, parent, false)
                   MyViewHolder(view)
                   //OR WE CAN THROW
                   /*throw IllegalStateException("Unknown view type $viewType")*/
               }
           }

            return viewHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            //get the data, then determine which view holder to create
            val recyclerData = differ.currentList[position]
            when (recyclerData) {
                is RecyclerData.CountryData -> {
                    //bind data to country holder
                    (holder as MyViewHolder).bind(recyclerData)
                }

                is RecyclerData.CountryHeader -> {
                    //bind data to header holder
                    (holder as HeaderViewHolder).bind(recyclerData.text)

                }

            }


        }


        /*this code sets up a mechanism for efficiently handling updates to a list of
        objects in a RecyclerView. It uses DiffUtil for calculating differences between lists,
        and AsyncListDiffer for handling these differences asynchronously and efficiently
        updating the UI. The submitList method is a convenient way to submit a
        new list for calculation and update.*/
        private val diffCallback = object : DiffUtil.ItemCallback<RecyclerData>() {
            override fun areItemsTheSame(oldItem: RecyclerData, newItem: RecyclerData): Boolean {
                var result = false
                when {
                    oldItem is RecyclerData.CountryData && newItem is RecyclerData.CountryData -> {
                        if (oldItem.name == newItem.name) {
                            result = true
                        }

                    }

                    oldItem is RecyclerData.CountryHeader && newItem is RecyclerData.CountryHeader -> {
                        if (oldItem.text == newItem.text) {
                            result = true
                        }
                    }

                    else -> {}

                }

                return result
            }

            override fun areContentsTheSame(oldItem: RecyclerData, newItem: RecyclerData): Boolean {
                var result = false
                when {
                    oldItem is RecyclerData.CountryData && newItem is RecyclerData.CountryData -> {
                        result = oldItem.name == newItem.name

                    }

                    oldItem is RecyclerData.CountryHeader && newItem is RecyclerData.CountryHeader -> {
                        result = oldItem.text == newItem.text
                    }

                    else -> {}

                }


                return result
            }
        }

        private val differ = AsyncListDiffer(this, diffCallback)

        fun submitList(list: List<RecyclerData>) = differ.submitList(list)
    }
}


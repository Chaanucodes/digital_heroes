package com.example.digitalheroes.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.digitalheroes.R
import com.example.digitalheroes.databinding.FragmentSearchBinding
import kotlin.math.abs

class SearchFragment : Fragment() {

    private val sharedPrefFileName = "com.example.digitalheroes"
    private val keyH = "SJO00"
    private val searchViewModel: SearchViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, SearchViewModel.Factory(activity.application))
            .get(SearchViewModel::class.java)
    }
    private var adapter: HeroListAdapter? = null
    private lateinit var binding: FragmentSearchBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel.heroList.observe(viewLifecycleOwner, Observer {
            adapter?.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search, container, false
        )


        // Implementing ViewModel
        binding.viewModelInXml = searchViewModel


        // Shared Preferences
        var bol = true
        val sharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE)
        bol = sharedPreferences.getBoolean(keyH, true)
        if (bol) {
            callTheFragment(DialogSwipeFragment())
            binding.searchHeroEditText.setText("Batman")
            buttonAction()
            Log.i("Do bar", "Dos bars")
        }

        // Handling send button for EditText
        binding.searchHeroEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            buttonAction()
                            return true
                        }
                        else -> {
                        }
                    }
                }
                return false
            }
        })

        // Forced troubleshooting for Lottie
        binding.animView.enableMergePathsForKitKatAndAbove(true)

        // Setting up adapter
        adapter = HeroListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.clipToPadding = false
        binding.recyclerView.clipChildren = false
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { view: View, fl: Float ->
            val r = 1 - abs(fl)
            view.scaleY = r
        }
        binding.recyclerView.setPageTransformer(compositePageTransformer)

        // Listeners
        binding.searchHeroButton.setOnClickListener {
            buttonAction()
        }

        // Observer(s)

        searchViewModel.showRecycler.observe(viewLifecycleOwner, Observer {
            binding.recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })

        searchViewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadStatus.NOT_FOUND -> {
//                    if (binding.animView.isAnimating) {
//                        binding.animView.pauseAnimation()
//                    }
//                    if (binding.animView.visibility == View.GONE)
//                        binding.animView.visibility = View.VISIBLE
//                    binding.animView.setAnimation(R.raw.notfound)
//                    binding.animView.playAnimation()
//                    binding.animView.setMaxFrame(180)
//                    binding.animView.repeatCount = 0
                    Toast.makeText(activity, "NOT FOUND!!", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                    if (binding.animView.isAnimating) {
                        binding.animView.pauseAnimation()
                    }
                }

//                LoadStatus.ERROR -> {
//                    if (binding.animView.isAnimating) {
//                        binding.animView.pauseAnimation()
//                    }
//                    if (binding.animView.visibility == View.GONE)
//                        binding.animView.visibility = View.VISIBLE
//                    binding.animView.setAnimation(R.raw.noconnection)
//                    binding.animView.playAnimation()
//                    binding.animView.setMaxFrame(150)
//                    binding.animView.repeatCount = 0
//                }

                LoadStatus.LOADING -> {
                    if (binding.animView.isAnimating) {
                        binding.animView.pauseAnimation()
                    }
                    if (binding.animView.visibility == View.GONE)
                        binding.animView.visibility = View.VISIBLE
                    binding.animView.setAnimation(R.raw.loadinglottie)
                    binding.animView.playAnimation()
                    binding.animView.speed = 1.3f
                    binding.animView.loop(true)
                }

                LoadStatus.DONE -> {
                    if (binding.animView.isAnimating) {
                        binding.animView.pauseAnimation()
                    }
                    if (binding.animView.visibility != View.GONE)
                        binding.animView.visibility = View.GONE
                }
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    // Inflating menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.compare_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item!!,
            requireView().findNavController()
        )
                || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Superheroes"
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

    }


    private fun callTheFragment(dialogFragment: DialogFragment) {
        val fragmentManager = requireActivity().supportFragmentManager

        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        transaction
            .replace(android.R.id.content, dialogFragment)
            .disallowAddToBackStack()
            .commit()
    }

    private fun buttonAction() {
        val s = binding.searchHeroEditText.text.toString().trim()

        if (s.isNotEmpty()) {
            searchViewModel.refreshDataFromRepository(s)
            val mgr =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mgr.hideSoftInputFromWindow(binding.searchHeroEditText.windowToken, 0)
        }
    }


}
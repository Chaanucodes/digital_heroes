package com.example.digitalheroes.compare

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.airbnb.lottie.LottieAnimationView
import com.example.digitalheroes.R
import com.example.digitalheroes.databinding.CompareFragmentBinding
import com.example.digitalheroes.search.DialogSwipeFragment
import com.example.digitalheroes.search.HeroListAdapter
import com.example.digitalheroes.search.LoadStatus
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import kotlin.math.abs


class CompareFragment : Fragment() {

    private val keyH = "SJO00"
    private lateinit var viewModel: CompareViewModel
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    var showDragHint = false

    @RequiresApi(api = Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        //View model setup
        viewModel = ViewModelProviders.of(this).get(CompareViewModel::class.java)

        //Binding
        val binding: CompareFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.compare_fragment, container, false)
        binding.compareViewModel = viewModel

        // Forced troubleshooting for Lottie
        binding.compareAnimView2.enableMergePathsForKitKatAndAbove(true)


        //First launch
        val sharedPreferences = activity!!.getSharedPreferences("com.example.digitalheroes", Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean(keyH, true)){

        }
        val preferenceEditor : SharedPreferences.Editor = sharedPreferences.edit()
        preferenceEditor.putBoolean(keyH, false)
        preferenceEditor.apply()


        //1st View Setup
        // Setting up adapter
        val adapter1 = CompareListAdapter(false)
        binding.compareRecyclerView1.adapter = adapter1
        binding.compareRecyclerView1.clipToPadding = false
        binding.compareRecyclerView1.clipChildren = false
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { view: View, fl: Float ->
            val r = 1 - abs(fl)
            view.scaleY = r
        }
        binding.compareRecyclerView1.setPageTransformer(compositePageTransformer)


        // Listeners
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        setCustomPeekHeight()






        binding.compareHeroButton1.setOnClickListener {
            val s = binding.compareHeroEditText1.text.toString().trim()
            if (s.isNotEmpty()) {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                val mgr =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.hideSoftInputFromWindow(binding.compareHeroEditText1.windowToken, 0)
                viewModel.getFirstSuperHero(s)
                binding.compareHeroButton1.text = getString(R.string.search_above)
                if(!binding.compareHeroButton2.text.contains(getString(R.string.search_below))){
                    binding.compareHeroButton1.visibility = View.GONE
                    binding.compareHeroButton2.visibility = View.VISIBLE
                }
            }
        }


        // Observer(s)
        viewModel.superhero1.observe(this, Observer {
            adapter1.submitList(it)
        })

        viewModel.showRecycler1.observe(viewLifecycleOwner, Observer {
            binding.compareRecyclerView1.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.loadingStatus1.observe(this, Observer {
            changeAnim(binding.compareAnimView2, it)
        })


        //2nd View Setup
        // Setting up adapter
        val adapter2 = CompareListAdapter(false)
        binding.compareRecyclerView2.adapter = adapter2
        binding.compareRecyclerView2.clipToPadding = false
        binding.compareRecyclerView2.clipChildren = false
        val compositePageTransformer2 = CompositePageTransformer()
        compositePageTransformer2.addTransformer(MarginPageTransformer(40))
        compositePageTransformer2.addTransformer { view: View, fl: Float ->
            val r = 1 - abs(fl)
            view.scaleY = r
        }
        binding.compareRecyclerView2.setPageTransformer(compositePageTransformer2)

        KeyboardVisibilityEvent.setEventListener(
            activity!!,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                   if (isOpen){
                       if (binding.compareRecyclerView2.visibility != View.GONE){
                           binding.compareRecyclerView2.visibility = View.GONE
                           binding.compareRecyclerView1.visibility = View.GONE
                           binding.darken1.visibility = View.VISIBLE
                       }
                   }else{
                       if (binding.compareRecyclerView2.visibility != View.VISIBLE){
                           binding.compareRecyclerView2.visibility = View.VISIBLE
                           binding.compareRecyclerView1.visibility = View.VISIBLE
                           binding.darken1.visibility = View.GONE
                           mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                       }

                   }
                }
            })

        // Listeners
        binding.compareHeroButton2.setOnClickListener {
            val s = binding.compareHeroEditText1.text.toString().trim()

            if (s.isNotEmpty()) {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                val mgr =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.hideSoftInputFromWindow(binding.compareHeroEditText1.windowToken, 0)
                viewModel.getSecondSuperHero(s)
                if (binding.compareHeroButton1.visibility != View.VISIBLE) {
                    binding.compareHeroButton1.visibility = View.VISIBLE
                    binding.compareHeroButton2.text = getString(R.string.search_below)
                    showDragHint = true
                }
            }
        }

        // Observer(s)
        viewModel.superhero2.observe(this, Observer {
            adapter2.submitList(it)
        })

        viewModel.showRecycler2.observe(viewLifecycleOwner, Observer {
            binding.compareRecyclerView2.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.loadingStatus2.observe(this, Observer {
            changeAnim(binding.compareAnimView2, it)
        })

        setHasOptionsMenu(true)

        //Behavior of Bottom sheet
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (showDragHint){
                            binding.stylingEditText.hint = getString(R.string.drag_me_down)
                        }else{
                            binding.stylingEditText.hint = getString(R.string.pull_me_up)
                        }
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        binding.stylingEditText.hint = getString(R.string.enter_a_name)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
            }
        })


        return binding.root
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Compare"
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun changeAnim(
        binding: LottieAnimationView,
        it: LoadStatus
    ) {
        when (it) {
            LoadStatus.NOT_FOUND -> {
                if (binding.isAnimating) {
                    binding.pauseAnimation()
                }
                if (binding.visibility == View.GONE)
                    binding.visibility = View.VISIBLE
                binding.setAnimation(R.raw.notfound)
                binding.playAnimation()
                binding.setMaxFrame(180)
                binding.repeatCount = 0
            }

            LoadStatus.ERROR -> {
                if (binding.isAnimating) {
                    binding.pauseAnimation()
                }
                if (binding.visibility == View.GONE)
                    binding.visibility = View.VISIBLE
                binding.setAnimation(R.raw.noconnection)
                binding.playAnimation()
                binding.setMaxFrame(150)
                binding.repeatCount = 0
            }

            LoadStatus.LOADING -> {
                if (binding.isAnimating) {
                    binding.pauseAnimation()
                }
                if (binding.visibility == View.GONE)
                    binding.visibility = View.VISIBLE
                binding.setAnimation(R.raw.loadinglottie)
                binding.playAnimation()
                binding.speed = 1.3f
                binding.loop(true)
            }

            LoadStatus.DONE -> {
                if (binding.isAnimating) {
                    binding.pauseAnimation()
                }
                if (binding.visibility == View.VISIBLE)
                    binding.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        return super.onOptionsItemSelected(item)
    }

//    private fun callTheFragment(dialogFragment: DialogFragment) {
//        val fragmentManager = activity!!.supportFragmentManager
//
//        val transaction = fragmentManager.beginTransaction()
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//
//        transaction
//            .replace(android.R.id.content, dialogFragment)
//            .disallowAddToBackStack()
//            .commit()
//    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    fun setCustomPeekHeight(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            mBottomSheetBehavior.peekHeight = 65
        }
    }



}
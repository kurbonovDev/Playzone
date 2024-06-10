package playzone.tj.presentation.main_screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import playzone.tj.R
import playzone.tj.presentation.main_screen.adapters.ViewPagerAdapter
import playzone.tj.databinding.FragmentMainBinding
import playzone.tj.presentation.login.LoginFragment
import playzone.tj.utils.replaceFragment
import kotlin.math.abs


class MainFragment : Fragment() {


    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler

    @DrawableRes
    private var imageList = mutableListOf(R.drawable.img1, R.drawable.img2, R.drawable.img3)
    private lateinit var adapter: ViewPagerAdapter

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setUpTransformer()
        nextPageListener()
    }


    private fun nextPageListener() {
        binding.btnNext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnNext.setOnClickListener {
                    when (s.toString()) {
                        getString(R.string.second_text_btn) -> {
                            replaceFragment(LoginFragment(), false)
                        }

                        getString(R.string.first_text_btn) -> {
                            viewPager2.currentItem += 1
                        }
                    }
                }

            }
        })
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f


        }

        viewPager2.setPageTransformer(transformer)


        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                when (viewPager2.currentItem) {
                    0 -> {
                        binding.titleText.text = getString(R.string.first_text_title)
                        binding.btnNext.text = getString(R.string.first_text_btn)
                    }

                    1 -> {
                        binding.titleText.text = getString(R.string.second_text_title)
                        binding.btnNext.text = getString(R.string.first_text_btn)
                    }

                    2 -> {
                        binding.titleText.text = getString(R.string.third_text_title)
                        binding.btnNext.text = getString(R.string.second_text_btn)
                    }
                }
                handler.postDelayed(runnable, 2500)

            }
        })
    }


    private fun init() {
        viewPager2 = binding.viewPager2
        handler = Handler(Looper.myLooper()!!)
        adapter = ViewPagerAdapter(imageList, viewPager2)
        viewPager2.adapter = adapter

        viewPager2.offscreenPageLimit = 1
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.dotsIndicator.attachTo(viewPager2)


    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


}
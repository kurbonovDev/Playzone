package playzone.tj.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import playzone.tj.R

class ViewPagerAdapter(@DrawableRes private val imageList: MutableList<Int>, private val viewPager2: ViewPager2)
    :RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>() {

    class ViewPagerHolder(view: View):RecyclerView.ViewHolder(view){
        val imageView:ImageView = view.findViewById(R.id.image_viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_container_viewpager,parent,false)
        return ViewPagerHolder(itemView)
    }

    override fun getItemCount(): Int {
      return imageList.size
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
       holder.imageView.setImageResource(imageList[position])

       /* if (position==imageList.size-1){
            viewPager2.post(runnable)
        }*/
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}
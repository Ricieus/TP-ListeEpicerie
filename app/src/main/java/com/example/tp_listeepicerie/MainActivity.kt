package com.example.tp_listeepicerie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class GenericItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val textName: TextView
    val textQuantity: TextView
    val img: ImageView
    val btn: Button

    init {
        layout = itemView as ConstraintLayout
        textName = itemView.findViewById(R.id.genericName)
        textQuantity = itemView.findViewById(R.id.genericQuantity)
        img = itemView.findViewById(R.id.genericImg)
        btn = itemView.findViewById(R.id.genericBtn)
    }
}

class GenericItemAdaptor(val ctx: Context, val activity: MainActivity, var data: List<GenericItem>) : RecyclerView.Adapter<GenericItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.liste_epicerie_item, parent, false)
        return GenericItemHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: GenericItemHolder, position: Int) {
        val currentGenericItem = data[position]

        holder.textName.text = currentGenericItem.nom
        holder.textQuantity.text = currentGenericItem.quantite.toString()
        holder.img.setImageResource(R.drawable.baseline_emoji_food_beverage_24)
        holder.btn.setOnClickListener {
                currentGenericItem.quantite++
                holder.textQuantity.text = currentGenericItem.quantite.toString()
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        val genericList = listOf(
            GenericItem("name1", 1, R.drawable.img, "fruits", R.id.buttonAcheter),
            GenericItem("name2", 1, R.drawable.img_1, "Legumes", R.id.buttonAcheter),
            GenericItem("name3", 1, R.drawable.img_1, "Legumes", R.id.buttonAcheter),
        )
        recyclerView.adapter = GenericItemAdaptor(applicationContext, this, genericList)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)}

}

data class GenericItem(var nom: String, var quantite: Int, var image: Int, var categorie: String, var bouton: Int)
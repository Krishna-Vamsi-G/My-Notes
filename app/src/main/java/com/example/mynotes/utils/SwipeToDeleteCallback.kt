package com.example.mynotes.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.example.mynotes.R


abstract class SwipeToDeleteCallback(private val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.delete)!!
    private val background: Drawable
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewHolder: RecyclerView.ViewHolder

    init {
        val backgroundColor = Color.parseColor("#FF6347") // Example color: Tomato
        val cornerRadius = 32f // Example corner radius: 32dp
        background = GradientDrawable().apply {
            setColor(backgroundColor)
            setCornerRadius(cornerRadius)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        this.recyclerView = recyclerView
        this.viewHolder = viewHolder

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom.toFloat() - itemView.top.toFloat()
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the delete background with rounded corners
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - deleteIcon.intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - deleteIcon.intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - deleteIcon.intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft.toInt(), deleteIconTop.toInt(), deleteIconRight.toInt(), deleteIconBottom.toInt())
        deleteIcon.draw(c)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        val paint = Paint()
        paint.color = Color.WHITE
        c.drawRect(left, top, right, bottom, paint)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Show confirmation dialog before deleting
        confirmDelete(viewHolder, direction)
    }

    private fun confirmDelete(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete, null)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Customize dialog buttons and actions
        dialogView.findViewById<TextView>(R.id.btn_delete).setOnClickListener {
            // Perform the deletion
            onDeleteConfirmed(viewHolder)
            alertDialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            // Dismiss the dialog and restore item state
            alertDialog.dismiss()
            recyclerView.adapter?.notifyItemChanged(viewHolder.adapterPosition)
        }

        alertDialog.show()
    }



    abstract fun onDeleteConfirmed(viewHolder: RecyclerView.ViewHolder)
}

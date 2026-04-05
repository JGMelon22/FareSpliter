package com.example.farespliter.ui.friends

import android.app.AlertDialog
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.example.farespliter.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.data.model.Friend
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FriendsActivity : AppCompatActivity() {

    private lateinit var viewModel: FriendsViewModel
    private lateinit var adapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        viewModel = ViewModelProvider(this)[FriendsViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupAddFriend()
        observeFriends()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_friends)
    }

    private fun setupRecyclerView() {
        adapter = FriendsAdapter(
            onEditClick = { friend ->
                showEditDialog(friend)
            },
            onDeleteClick = { friend ->
                viewModel.deleteFriend(friend.id)
            }
        )
        val rv = findViewById<RecyclerView>(R.id.rvFriends)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun showEditDialog(friend: Friend) {
        val input = TextInputEditText(this)
        input.setText(friend.name)
        input.selectAll()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_edit_friend))
            .setView(input)
            .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                val newName = input.text?.toString() ?: return@setPositiveButton
                if (newName.isBlank()) return@setPositiveButton

                // .copy() cria um novo Friend com o mesmo id, mas nome atualizado
                viewModel.updateFriend(friend.copy(name = newName.trim()))
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun setupAddFriend() {
        val etName = findViewById<TextInputEditText>(R.id.etFriendName)
        val btnAdd = findViewById<MaterialButton>(R.id.btnAddFriend)

        btnAdd.setOnClickListener {
            submitNewFriend(etName)
        }

        // Allow submitting by pressing Done on the keyboard
        etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitNewFriend(etName)
                true
            } else false
        }
    }

    private fun submitNewFriend(etName: TextInputEditText) {
        val name = etName.text?.toString() ?: return
        viewModel.addFriend(name)
        etName.text?.clear()
    }

    private fun observeFriends() {
        viewModel.friends.observe(this) { friends ->
            adapter.submitList(friends)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
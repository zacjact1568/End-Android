package net.zackzhang.app.end.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_thanks.*
import net.zackzhang.app.end.R
import net.zackzhang.app.end.view.adapter.LibraryListAdapter

class ThanksFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_thanks, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_library.adapter = LibraryListAdapter(activity!!)
        list_library.setHasFixedSize(true)
    }

    override fun onDetach() {
        super.onDetach()
    }
}

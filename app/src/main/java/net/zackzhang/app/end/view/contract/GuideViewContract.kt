package net.zackzhang.app.end.view.contract

interface GuideViewContract : BaseViewContract {

    fun showInitialView()

    fun exitWithResult(isNormally: Boolean)
}
